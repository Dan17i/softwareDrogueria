package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.GoodsReceipt;
import com.drogueria.bellavista.domain.model.GoodsReceiptItem;
import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.domain.repository.GoodsReceiptRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
/**
 * Servicio de dominio encargado de la gestión de recepciones de mercancía.
 *
 * <p>Responsabilidades principales:</p>
 * <ul>
 *     <li>Crear recepciones vinculadas a órdenes de compra</li>
 *     <li>Validar coherencia entre productos, estado de orden y cantidades</li>
 *     <li>Actualizar el stock de productos al confirmar recepción</li>
 *     <li>Permitir rechazo de recepciones sin afectar inventario</li>
 * </ul>
 *
 * <p>Este servicio contiene lógica crítica del negocio, por lo que las operaciones
 * que modifican datos están protegidas con transacciones.</p>
 */
@Service
@RequiredArgsConstructor
public class GoodsReceiptService {
    
    private final GoodsReceiptRepository goodsReceiptRepository;
    private final OrderService orderService;
    private final ProductService productService;
    /**
     * Crea una nueva recepción de mercancía asociada a una orden.
     *
     * <p>Validaciones realizadas:</p>
     * <ul>
     *     <li>La orden debe existir</li>
     *     <li>La orden debe estar en estado COMPLETED</li>
     *     <li>No debe existir otra recepción ya marcada como RECEIVED</li>
     *     <li>Todos los productos deben existir y estar activos</li>
     *     <li>Las cantidades recibidas no deben exceder las ordenadas</li>
     * </ul>
     *
     * @param goodsReceipt objeto de recepción a registrar
     * @return recepción guardada en base de datos
     * @throws ResourceNotFoundException si la orden o un producto no existe
     * @throws BusinessException si alguna regla de negocio se incumple
     */
    @Transactional
    public GoodsReceipt createGoodsReceipt(GoodsReceipt goodsReceipt) {
        // Validar que la orden existe
        Order order = orderService.getOrderById(goodsReceipt.getOrderId());
        if (order == null) {
            throw new ResourceNotFoundException("Order not found with ID: " + goodsReceipt.getOrderId());
        }
        
        if (!"COMPLETED".equals(order.getStatus())) {
            throw new BusinessException(
                    "Cannot create goods receipt for order not in COMPLETED status. " +
                    "Current status: " + order.getStatus());
        }
        
        // Validar que no hay recepción duplicada
        List<GoodsReceipt> existingReceipts = goodsReceiptRepository.findByOrderId(goodsReceipt.getOrderId());
        if (existingReceipts.stream().anyMatch(gr -> "RECEIVED".equals(gr.getStatus()))) {
            throw new BusinessException("Order already has a RECEIVED goods receipt");
        }
        
        // Validar cada item
        for (GoodsReceiptItem item : goodsReceipt.getItems()) {
            // Validar que el producto existe
            Product product = productService.getProductById(item.getProductId());
            if (product == null) {
                throw new ResourceNotFoundException("Product not found with ID: " + item.getProductId());
            }
            if (!product.getActive()) {
                throw new BusinessException(
                        "Cannot receive from inactive product: " + product.getCode());
            }
            
            // Validar que la cantidad recibida es válida
            if (!item.isValidQuantity()) {
                throw new BusinessException(
                        "Received quantity (" + item.getReceivedQuantity() + ") " +
                        "cannot exceed ordered quantity (" + item.getOrderedQuantity() + ") " +
                        "for product: " + item.getProductCode());
            }
        }
        
        // Generar número único de recepción
        String receiptNumber = "GR-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
        goodsReceipt.setReceiptNumber(receiptNumber);
        goodsReceipt.setStatus("PENDING");
        goodsReceipt.setReceiptDate(LocalDateTime.now());
        goodsReceipt.setCreatedAt(LocalDateTime.now());
        goodsReceipt.setUpdatedAt(LocalDateTime.now());
        
        // Guardar recepción
        GoodsReceipt saved = goodsReceiptRepository.save(goodsReceipt);
        
        // Validar que receiptNumber es único
        if (goodsReceiptRepository.existsByReceiptNumber(receiptNumber)) {
            throw new BusinessException("Goods receipt number already exists: " + receiptNumber);
        }
        
        return saved;
    }
    /**
     * Confirma la recepción de mercancía y actualiza el stock de productos.
     *
     * <p>Reglas:</p>
     * <ul>
     *     <li>Solo puede ejecutarse si la recepción está en estado PENDING</li>
     *     <li>Debe contener al menos un item</li>
     *     <li>El stock se incrementa de forma atómica por producto</li>
     * </ul>
     *
     * @param goodsReceiptId identificador de la recepción
     * @return recepción actualizada con estado final
     * @throws ResourceNotFoundException si la recepción no existe
     * @throws BusinessException si el estado no permite recibirla
     */
    @Transactional
    public GoodsReceipt receiveGoodsReceipt(Long goodsReceiptId) {
        GoodsReceipt goodsReceipt = goodsReceiptRepository.findById(goodsReceiptId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goods receipt not found with ID: " + goodsReceiptId));
        
        if (!"PENDING".equals(goodsReceipt.getStatus())) {
            throw new BusinessException(
                    "Cannot receive goods receipt with status: " + goodsReceipt.getStatus());
        }
        
        // Validar que hay items
        if (goodsReceipt.getItems() == null || goodsReceipt.getItems().isEmpty()) {
            throw new BusinessException("Cannot receive goods receipt without items");
        }
        
        // Recibir cada item y actualizar stock
        for (GoodsReceiptItem item : goodsReceipt.getItems()) {
            if (item.getReceivedQuantity() != null && item.getReceivedQuantity() > 0) {
                // Aumentar stock del producto ATOMICAMENTE
                productService.increaseStockInternal(item.getProductId(), item.getReceivedQuantity());
            }
        }
        
        // Determinar estado final
        goodsReceipt.receive();
        goodsReceipt.setUpdatedAt(LocalDateTime.now());
        
        return goodsReceiptRepository.save(goodsReceipt);
    }
    /**
     * Marca una recepción como rechazada.
     *
     * <p>No afecta inventario si la recepción aún no ha sido confirmada.</p>
     *
     * @param goodsReceiptId identificador de la recepción
     * @return recepción actualizada
     * @throws ResourceNotFoundException si no existe
     * @throws BusinessException si ya fue recibida previamente
     */
    @Transactional
    public GoodsReceipt rejectGoodsReceipt(Long goodsReceiptId) {
        GoodsReceipt goodsReceipt = goodsReceiptRepository.findById(goodsReceiptId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goods receipt not found with ID: " + goodsReceiptId));
        
        if ("RECEIVED".equals(goodsReceipt.getStatus())) {
            throw new BusinessException(
                    "Cannot reject a goods receipt that has already been received");
        }
        
        goodsReceipt.setStatus("REJECTED");
        goodsReceipt.setUpdatedAt(LocalDateTime.now());
        
        return goodsReceiptRepository.save(goodsReceipt);
    }
    /**
     * Obtiene una recepción por su identificador.
     *
     * @param id ID de la recepción
     * @return recepción encontrada
     * @throws ResourceNotFoundException si no existe
     */
    public GoodsReceipt getGoodsReceiptById(Long id) {
        return goodsReceiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goods receipt not found with ID: " + id));
    }
    /**
     * Obtiene una recepción por su número único.
     *
     * @param receiptNumber número de recepción
     * @return recepción encontrada
     * @throws ResourceNotFoundException si no existe
     */
    public GoodsReceipt getGoodsReceiptByNumber(String receiptNumber) {
        return goodsReceiptRepository.findByReceiptNumber(receiptNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goods receipt not found with number: " + receiptNumber));
    }
    /**
     * Lista todas las recepciones asociadas a una orden.
     */
    public List<GoodsReceipt> getGoodsReceiptsByOrder(Long orderId) {
        return goodsReceiptRepository.findByOrderId(orderId);
    }
    /**
     * Lista todas las recepciones asociadas a un proveedor.
     */
    public List<GoodsReceipt> getGoodsReceiptsBySupplier(Long supplierId) {
        return goodsReceiptRepository.findBySupplierId(supplierId);
    }
    /**
     * Obtiene todas las recepciones pendientes.
     */
    public List<GoodsReceipt> getPendingGoodsReceipts() {
        return goodsReceiptRepository.findPendingReceipts();
    }
    /**
     * Obtiene recepciones filtradas por estado.
     */
    public List<GoodsReceipt> getGoodsReceiptsByStatus(String status) {
        return goodsReceiptRepository.findByStatus(status);
    }
    /**
     * Obtiene todas las recepciones registradas.
     */
    public List<GoodsReceipt> getAllGoodsReceipts() {
        return goodsReceiptRepository.findAll();
    }
    /**
     * Elimina una recepción si aún no ha sido confirmada.
     *
     * @param id identificador de la recepción
     * @throws ResourceNotFoundException si no existe
     * @throws BusinessException si la recepción ya fue recibida
     */
    @Transactional
    public void deleteGoodsReceipt(Long id) {
        GoodsReceipt goodsReceipt = goodsReceiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goods receipt not found with ID: " + id));
        
        if ("RECEIVED".equals(goodsReceipt.getStatus())) {
            throw new BusinessException(
                    "Cannot delete a goods receipt that has already been received");
        }
        
        goodsReceiptRepository.delete(id);
    }
}
