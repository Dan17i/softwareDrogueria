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
 * Controller: REST endpoints para Goods Receipt (Recepción de Mercancía)
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
     * POST /api/goods-receipts - Crear nueva recepción de mercancía
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
     * GET /api/goods-receipts/{id} - Obtener recepción por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<GoodsReceiptDTO.Response> getGoodsReceiptById(@PathVariable Long id) {
        GoodsReceipt receipt = goodsReceiptService.getGoodsReceiptById(id);
        if (receipt == null) throw new ResourceNotFoundException("Goods Receipt not found with ID: " + id);
        
        return ResponseEntity.ok(mapper.toResponse(receipt));
    }
    
    /**
     * GET /api/goods-receipts/number/{receiptNumber} - Obtener por número de recepción
     */
    @GetMapping("/number/{receiptNumber}")
    public ResponseEntity<GoodsReceiptDTO.Response> getGoodsReceiptByNumber(@PathVariable String receiptNumber) {
        GoodsReceipt receipt = goodsReceiptService.getGoodsReceiptByNumber(receiptNumber);
        if (receipt == null) throw new ResourceNotFoundException("Goods Receipt not found with number: " + receiptNumber);
        
        return ResponseEntity.ok(mapper.toResponse(receipt));
    }
    
    /**
     * GET /api/goods-receipts/order/{orderId} - Obtener recepciones de una orden
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
     * GET /api/goods-receipts/supplier/{supplierId} - Obtener recepciones de un proveedor
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
     * GET /api/goods-receipts/status/{status} - Obtener recepciones por estado
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<GoodsReceiptDTO.Response>> getGoodsReceiptsByStatus(@PathVariable String status) {
        List<GoodsReceipt> receipts = goodsReceiptService.getGoodsReceiptsByStatus(status);
        return ResponseEntity.ok(receipts.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList()));
    }
    
    /**
     * GET /api/goods-receipts/pending - Obtener recepciones pendientes
     */
    @GetMapping("/pending")
    public ResponseEntity<List<GoodsReceiptDTO.Response>> getPendingGoodsReceipts() {
        List<GoodsReceipt> receipts = goodsReceiptService.getPendingGoodsReceipts();
        return ResponseEntity.ok(receipts.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList()));
    }
    
    /**
     * GET /api/goods-receipts - Obtener todas las recepciones
     */
    @GetMapping
    public ResponseEntity<List<GoodsReceiptDTO.Response>> getAllGoodsReceipts() {
        List<GoodsReceipt> receipts = goodsReceiptService.getAllGoodsReceipts();
        return ResponseEntity.ok(receipts.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList()));
    }
    
    /**
     * PATCH /api/goods-receipts/{id}/receive - Confirmar recepción de mercancía
     * Actualiza stock de producto y cambia status de la recepción
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
     * PATCH /api/goods-receipts/{id}/reject - Rechazar recepción de mercancía
     * Cambia status a REJECTED sin afectar stock
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
     * DELETE /api/goods-receipts/{id} - Eliminar recepción de mercancía
     * Solo se pueden eliminar recepciones en estado PENDING
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
