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
 * Servicio de Recepción de Mercancía
 * LÓGICA CRÍTICA:
 * - Crear recepción vinculada a una orden
 * - Validar que productos y cantidades coincidan
 * - Al recibir: actualizar stock de productos
 * - Si se rechaza: limpiar cambios
 */
@Service
@RequiredArgsConstructor
public class GoodsReceiptService {
    
    private final GoodsReceiptRepository goodsReceiptRepository;
    private final OrderService orderService;
    private final ProductService productService;
    
    /**
     * LÓGICA CRÍTICA: Crear recepción de mercancía
     * Validaciones complejas entre Order y Productos
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
     * Recibir mercancía - LÓGICA CRÍTICA
     * Actualiza stock de productos atomicamente
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
     * Rechazar recepción - No modifica stock si aún está PENDING
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
     * Obtener recepción por ID
     */
    public GoodsReceipt getGoodsReceiptById(Long id) {
        return goodsReceiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goods receipt not found with ID: " + id));
    }
    
    /**
     * Obtener por número de recepción
     */
    public GoodsReceipt getGoodsReceiptByNumber(String receiptNumber) {
        return goodsReceiptRepository.findByReceiptNumber(receiptNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goods receipt not found with number: " + receiptNumber));
    }
    
    /**
     * Obtener recepciones de una orden
     */
    public List<GoodsReceipt> getGoodsReceiptsByOrder(Long orderId) {
        return goodsReceiptRepository.findByOrderId(orderId);
    }
    
    /**
     * Obtener recepciones de un proveedor
     */
    public List<GoodsReceipt> getGoodsReceiptsBySupplier(Long supplierId) {
        return goodsReceiptRepository.findBySupplierId(supplierId);
    }
    
    /**
     * Obtener recepciones pendientes
     */
    public List<GoodsReceipt> getPendingGoodsReceipts() {
        return goodsReceiptRepository.findPendingReceipts();
    }
    
    /**
     * Obtener por estado
     */
    public List<GoodsReceipt> getGoodsReceiptsByStatus(String status) {
        return goodsReceiptRepository.findByStatus(status);
    }
    
    /**
     * Obtener todas
     */
    public List<GoodsReceipt> getAllGoodsReceipts() {
        return goodsReceiptRepository.findAll();
    }
    
    /**
     * Eliminar
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
