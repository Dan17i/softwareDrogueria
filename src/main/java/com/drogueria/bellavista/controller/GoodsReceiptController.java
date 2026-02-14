package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.GoodsReceiptDTO;
import com.drogueria.bellavista.application.mapper.GoodsReceiptUseCaseMapper;
import com.drogueria.bellavista.domain.model.*;
import com.drogueria.bellavista.domain.service.GoodsReceiptService;
import com.drogueria.bellavista.domain.service.OrderService;
import com.drogueria.bellavista.domain.service.SupplierService;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Controlador REST para la gestión de recepciones de mercancía (Goods Receipt).
 * <p>
 * Proporciona endpoints para crear, consultar, actualizar el estado y eliminar recepciones de mercancía.
 * Este controlador actúa como adaptador de entrada en la arquitectura hexagonal,
 * delegando la lógica de negocio a {@link GoodsReceiptService}, {@link OrderService} y {@link SupplierService}.
 * </p>
 */
@RestController
@RequestMapping("/goods-receipts")
@RequiredArgsConstructor
public class GoodsReceiptController {
    
    private final GoodsReceiptService goodsReceiptService;
    private final OrderService orderService;
    private final SupplierService supplierService;
    private final GoodsReceiptUseCaseMapper mapper;
    /**
     * Crea una nueva recepción de mercancía.
     *
     * @param request DTO con la información de la recepción a crear
     * @return {@link ResponseEntity} con {@link GoodsReceiptDTO.Response} de la recepción creada
     * @throws ResourceNotFoundException si no existe la orden o el proveedor asociado
     */
    @PostMapping
    public ResponseEntity<GoodsReceiptDTO.Response> createGoodsReceipt(
            @Valid @RequestBody GoodsReceiptDTO.CreateRequest request) {
        
        Order order = orderService.getOrderById(request.getOrderId());
        if (order == null) throw new ResourceNotFoundException("Order not found with ID: " + request.getOrderId());

        Supplier supplier = supplierService.getSupplierById(order.getSupplierId());
        if (supplier == null) throw new ResourceNotFoundException("Supplier not found with ID: " + order.getSupplierId());

        GoodsReceipt receipt = mapper.toDomain(request, order, supplier);
        GoodsReceipt createdReceipt = goodsReceiptService.createGoodsReceipt(receipt);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(createdReceipt));
    }
    /**
     * Obtiene una recepción de mercancía por su ID.
     *
     * @param id Identificador de la recepción
     * @return {@link ResponseEntity} con {@link GoodsReceiptDTO.Response}
     * @throws ResourceNotFoundException si no se encuentra la recepción
     */
    @GetMapping("/{id}")
    public ResponseEntity<GoodsReceiptDTO.Response> getGoodsReceiptById(@PathVariable Long id) {
        GoodsReceipt receipt = goodsReceiptService.getGoodsReceiptById(id);
        if (receipt == null) throw new ResourceNotFoundException("Goods Receipt not found with ID: " + id);
        
        return ResponseEntity.ok(mapper.toResponse(receipt));
    }

    /**
     * Obtiene una recepción de mercancía por su número de recepción.
     *
     * @param receiptNumber Número único de la recepción
     * @return {@link ResponseEntity} con {@link GoodsReceiptDTO.Response}
     * @throws ResourceNotFoundException si no se encuentra la recepción
     */
    @GetMapping("/number/{receiptNumber}")
    public ResponseEntity<GoodsReceiptDTO.Response> getGoodsReceiptByNumber(@PathVariable String receiptNumber) {
        GoodsReceipt receipt = goodsReceiptService.getGoodsReceiptByNumber(receiptNumber);
        if (receipt == null) throw new ResourceNotFoundException("Goods Receipt not found with number: " + receiptNumber);
        
        return ResponseEntity.ok(mapper.toResponse(receipt));
    }
    /**
     * Obtiene todas las recepciones asociadas a una orden.
     *
     * @param orderId ID de la orden
     * @return {@link ResponseEntity} con lista de {@link GoodsReceiptDTO.Response}
     * @throws ResourceNotFoundException si la orden no existe
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<GoodsReceiptDTO.Response>> getGoodsReceiptsByOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) throw new ResourceNotFoundException("Order not found with ID: " + orderId);

        List<GoodsReceipt> receipts = goodsReceiptService.getGoodsReceiptsByOrder(orderId);
        return ResponseEntity.ok(receipts.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList()));
    }

    /**
     * Obtiene todas las recepciones asociadas a un proveedor.
     *
     * @param supplierId ID del proveedor
     * @return {@link ResponseEntity} con lista de {@link GoodsReceiptDTO.Response}
     * @throws ResourceNotFoundException si el proveedor no existe
     */
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<GoodsReceiptDTO.Response>> getGoodsReceiptsBySupplier(@PathVariable Long supplierId) {
        Supplier supplier = supplierService.getSupplierById(supplierId);
        if (supplier == null) throw new ResourceNotFoundException("Supplier not found with ID: " + supplierId);

        List<GoodsReceipt> receipts = goodsReceiptService.getGoodsReceiptsBySupplier(supplierId);
        return ResponseEntity.ok(receipts.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList()));
    }
    /**
     * Obtiene recepciones por estado.
     *
     * @param status Estado de la recepción (PENDING, RECEIVED, REJECTED)
     * @return {@link ResponseEntity} con lista de {@link GoodsReceiptDTO.Response}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<GoodsReceiptDTO.Response>> getGoodsReceiptsByStatus(@PathVariable String status) {
        List<GoodsReceipt> receipts = goodsReceiptService.getGoodsReceiptsByStatus(status);
        return ResponseEntity.ok(receipts.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList()));
    }
    /**
     * Obtiene todas las recepciones pendientes.
     *
     * @return {@link ResponseEntity} con lista de {@link GoodsReceiptDTO.Response} pendientes
     */
    @GetMapping("/pending")
    public ResponseEntity<List<GoodsReceiptDTO.Response>> getPendingGoodsReceipts() {
        List<GoodsReceipt> receipts = goodsReceiptService.getPendingGoodsReceipts();
        return ResponseEntity.ok(receipts.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList()));
    }
    /**
     * Obtiene todas las recepciones de mercancía.
     *
     * @return {@link ResponseEntity} con lista completa de {@link GoodsReceiptDTO.Response}
     */
    @GetMapping
    public ResponseEntity<List<GoodsReceiptDTO.Response>> getAllGoodsReceipts() {
        List<GoodsReceipt> receipts = goodsReceiptService.getAllGoodsReceipts();
        return ResponseEntity.ok(receipts.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList()));
    }
    /**
     * Confirma la recepción de mercancía y actualiza el stock de los productos.
     *
     * @param id ID de la recepción
     * @return {@link ResponseEntity} con {@link GoodsReceiptDTO.Response} de la recepción recibida
     * @throws ResourceNotFoundException si la recepción no existe
     * @throws BusinessException si la recepción no está en estado PENDING
     */
    @PatchMapping("/{id}/receive")
    public ResponseEntity<GoodsReceiptDTO.Response> receiveGoodsReceipt(@PathVariable Long id) {
        GoodsReceipt receipt = goodsReceiptService.getGoodsReceiptById(id);
        if (receipt == null) throw new ResourceNotFoundException("Goods Receipt not found with ID: " + id);

        if (!"PENDING".equals(receipt.getStatus())) {
            throw new BusinessException("Can only receive goods receipts in PENDING status");
        }

        GoodsReceipt receivedReceipt = goodsReceiptService.receiveGoodsReceipt(id);
        return ResponseEntity.ok(mapper.toResponse(receivedReceipt));
    }
    /**
     * Rechaza una recepción de mercancía. No afecta el stock.
     *
     * @param id ID de la recepción
     * @param reason Razón opcional del rechazo
     * @return {@link ResponseEntity} con {@link GoodsReceiptDTO.Response} de la recepción rechazada
     * @throws ResourceNotFoundException si la recepción no existe
     * @throws BusinessException si la recepción no está en estado PENDING
     */
    @PatchMapping("/{id}/reject")
    public ResponseEntity<GoodsReceiptDTO.Response> rejectGoodsReceipt(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        
        GoodsReceipt receipt = goodsReceiptService.getGoodsReceiptById(id);
        if (receipt == null) throw new ResourceNotFoundException("Goods Receipt not found with ID: " + id);

        if (!"PENDING".equals(receipt.getStatus())) {
            throw new BusinessException("Can only reject goods receipts in PENDING status");
        }

        GoodsReceipt rejectedReceipt = goodsReceiptService.rejectGoodsReceipt(id);
        return ResponseEntity.ok(mapper.toResponse(rejectedReceipt));
    }
    /**
     * Elimina una recepción de mercancía. Solo se pueden eliminar recepciones en estado PENDING.
     *
     * @param id ID de la recepción
     * @return {@link ResponseEntity} vacío con HTTP 204
     * @throws ResourceNotFoundException si la recepción no existe
     * @throws BusinessException si la recepción no está en estado PENDING
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoodsReceipt(@PathVariable Long id) {
        GoodsReceipt receipt = goodsReceiptService.getGoodsReceiptById(id);
        if (receipt == null) throw new ResourceNotFoundException("Goods Receipt not found with ID: " + id);

        if (!"PENDING".equals(receipt.getStatus())) {
            throw new BusinessException("Can only delete goods receipts in PENDING status");
        }

        goodsReceiptService.deleteGoodsReceipt(id);
        return ResponseEntity.noContent().build();
    }
}
