package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.GoodsReceiptDTO;
import com.drogueria.bellavista.application.mapper.GoodsReceiptUseCaseMapper;
import com.drogueria.bellavista.domain.model.GoodsReceipt;
import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.model.Supplier;
import com.drogueria.bellavista.domain.service.GoodsReceiptService;
import com.drogueria.bellavista.domain.service.OrderService;
import com.drogueria.bellavista.domain.service.SupplierService;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoodsReceiptControllerTest {

    @Mock private GoodsReceiptService goodsReceiptService;
    @Mock private OrderService orderService;
    @Mock private SupplierService supplierService;
    @Mock private GoodsReceiptUseCaseMapper mapper;

    @InjectMocks
    private GoodsReceiptController controller;

    private GoodsReceipt pendingReceipt;
    private GoodsReceipt receivedReceipt;
    private GoodsReceiptDTO.Response pendingDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pendingReceipt = GoodsReceipt.builder()
            .id(10L)
            .receiptNumber("GR-001")
            .orderId(1L)
            .supplierId(5L)
            .status("PENDING")
            .createdAt(LocalDateTime.now())
            .build();

        receivedReceipt = GoodsReceipt.builder()
            .id(10L)
            .receiptNumber("GR-001")
            .status("RECEIVED")
            .build();

        pendingDTO = GoodsReceiptDTO.Response.builder()
            .id(10L)
            .receiptNumber("GR-001")
            .status("PENDING")
            .build();
    }

    @Test
    @DisplayName("✅ getGoodsReceiptById - retorna 200 con recepción")
    void shouldGetGoodsReceiptById() {
        when(goodsReceiptService.getGoodsReceiptById(10L)).thenReturn(pendingReceipt);
        when(mapper.toResponse(pendingReceipt)).thenReturn(pendingDTO);

        ResponseEntity<GoodsReceiptDTO.Response> response = controller.getGoodsReceiptById(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("GR-001", response.getBody().getReceiptNumber());
    }

    @Test
    @DisplayName("❌ getGoodsReceiptById - null lanza ResourceNotFoundException")
    void shouldThrowWhenReceiptNotFoundById() {
        when(goodsReceiptService.getGoodsReceiptById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> controller.getGoodsReceiptById(99L));
    }

    @Test
    @DisplayName("✅ getGoodsReceiptByNumber - retorna 200")
    void shouldGetByNumber() {
        when(goodsReceiptService.getGoodsReceiptByNumber("GR-001")).thenReturn(pendingReceipt);
        when(mapper.toResponse(pendingReceipt)).thenReturn(pendingDTO);

        ResponseEntity<GoodsReceiptDTO.Response> response = controller.getGoodsReceiptByNumber("GR-001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("❌ getGoodsReceiptByNumber - null lanza ResourceNotFoundException")
    void shouldThrowWhenNumberNotFound() {
        when(goodsReceiptService.getGoodsReceiptByNumber("GR-999")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> controller.getGoodsReceiptByNumber("GR-999"));
    }

    @Test
    @DisplayName("✅ getAllGoodsReceipts - retorna lista con 200")
    void shouldGetAllGoodsReceipts() {
        when(goodsReceiptService.getAllGoodsReceipts()).thenReturn(List.of(pendingReceipt));
        when(mapper.toResponse(pendingReceipt)).thenReturn(pendingDTO);

        ResponseEntity<List<GoodsReceiptDTO.Response>> response = controller.getAllGoodsReceipts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("✅ getPendingGoodsReceipts - retorna lista de pendientes")
    void shouldGetPendingGoodsReceipts() {
        when(goodsReceiptService.getPendingGoodsReceipts()).thenReturn(List.of(pendingReceipt));
        when(mapper.toResponse(pendingReceipt)).thenReturn(pendingDTO);

        ResponseEntity<List<GoodsReceiptDTO.Response>> response = controller.getPendingGoodsReceipts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("✅ getGoodsReceiptsByStatus - retorna lista filtrada por status")
    void shouldGetGoodsReceiptsByStatus() {
        when(goodsReceiptService.getGoodsReceiptsByStatus("PENDING")).thenReturn(List.of(pendingReceipt));
        when(mapper.toResponse(pendingReceipt)).thenReturn(pendingDTO);

        ResponseEntity<List<GoodsReceiptDTO.Response>> response = controller.getGoodsReceiptsByStatus("PENDING");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("✅ getGoodsReceiptsByOrder - retorna recepciones de la orden")
    void shouldGetGoodsReceiptsByOrder() {
        Order order = Order.builder().id(1L).orderNumber("ORD-001").supplierId(5L).build();
        when(orderService.getOrderById(1L)).thenReturn(order);
        when(goodsReceiptService.getGoodsReceiptsByOrder(1L)).thenReturn(List.of(pendingReceipt));
        when(mapper.toResponse(pendingReceipt)).thenReturn(pendingDTO);

        ResponseEntity<List<GoodsReceiptDTO.Response>> response = controller.getGoodsReceiptsByOrder(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("❌ getGoodsReceiptsByOrder - orden no encontrada lanza ResourceNotFoundException")
    void shouldThrowWhenOrderNotFoundForReceipts() {
        when(orderService.getOrderById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> controller.getGoodsReceiptsByOrder(99L));
    }

    @Test
    @DisplayName("✅ getGoodsReceiptsBySupplier - retorna recepciones del proveedor")
    void shouldGetGoodsReceiptsBySupplier() {
        Supplier supplier = Supplier.builder().id(5L).name("Proveedor Test").build();
        when(supplierService.getSupplierById(5L)).thenReturn(supplier);
        when(goodsReceiptService.getGoodsReceiptsBySupplier(5L)).thenReturn(List.of(pendingReceipt));
        when(mapper.toResponse(pendingReceipt)).thenReturn(pendingDTO);

        ResponseEntity<List<GoodsReceiptDTO.Response>> response = controller.getGoodsReceiptsBySupplier(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("❌ getGoodsReceiptsBySupplier - proveedor no encontrado lanza ResourceNotFoundException")
    void shouldThrowWhenSupplierNotFound() {
        when(supplierService.getSupplierById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> controller.getGoodsReceiptsBySupplier(99L));
    }

    @Test
    @DisplayName("✅ receiveGoodsReceipt - recepción PENDING → 200 RECEIVED")
    void shouldReceiveGoodsReceipt() {
        GoodsReceiptDTO.Response receivedDTO = GoodsReceiptDTO.Response.builder().id(10L).status("RECEIVED").build();
        when(goodsReceiptService.getGoodsReceiptById(10L)).thenReturn(pendingReceipt);
        when(goodsReceiptService.receiveGoodsReceipt(10L)).thenReturn(receivedReceipt);
        when(mapper.toResponse(receivedReceipt)).thenReturn(receivedDTO);

        ResponseEntity<GoodsReceiptDTO.Response> response = controller.receiveGoodsReceipt(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("RECEIVED", response.getBody().getStatus());
    }

    @Test
    @DisplayName("❌ receiveGoodsReceipt - no PENDING lanza BusinessException")
    void shouldThrowWhenReceivingNonPendingReceipt() {
        when(goodsReceiptService.getGoodsReceiptById(10L)).thenReturn(receivedReceipt);

        assertThrows(BusinessException.class, () -> controller.receiveGoodsReceipt(10L));
        verify(goodsReceiptService, never()).receiveGoodsReceipt(anyLong());
    }

    @Test
    @DisplayName("❌ receiveGoodsReceipt - no encontrada lanza ResourceNotFoundException")
    void shouldThrowWhenReceiptToReceiveNotFound() {
        when(goodsReceiptService.getGoodsReceiptById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> controller.receiveGoodsReceipt(99L));
    }

    @Test
    @DisplayName("✅ rejectGoodsReceipt - recepción PENDING → 200 REJECTED")
    void shouldRejectGoodsReceipt() {
        GoodsReceipt rejectedReceipt = GoodsReceipt.builder().id(10L).status("REJECTED").build();
        GoodsReceiptDTO.Response rejectedDTO = GoodsReceiptDTO.Response.builder().id(10L).status("REJECTED").build();
        when(goodsReceiptService.getGoodsReceiptById(10L)).thenReturn(pendingReceipt);
        when(goodsReceiptService.rejectGoodsReceipt(10L)).thenReturn(rejectedReceipt);
        when(mapper.toResponse(rejectedReceipt)).thenReturn(rejectedDTO);

        ResponseEntity<GoodsReceiptDTO.Response> response = controller.rejectGoodsReceipt(10L, "Mercancía dañada");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("❌ rejectGoodsReceipt - no PENDING lanza BusinessException")
    void shouldThrowWhenRejectingNonPendingReceipt() {
        when(goodsReceiptService.getGoodsReceiptById(10L)).thenReturn(receivedReceipt);

        assertThrows(BusinessException.class, () -> controller.rejectGoodsReceipt(10L, "razón"));
        verify(goodsReceiptService, never()).rejectGoodsReceipt(anyLong());
    }

    @Test
    @DisplayName("❌ rejectGoodsReceipt - no encontrada lanza ResourceNotFoundException")
    void shouldThrowWhenReceiptToRejectNotFound() {
        when(goodsReceiptService.getGoodsReceiptById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> controller.rejectGoodsReceipt(99L, "razón"));
    }

    @Test
    @DisplayName("✅ deleteGoodsReceipt - recepción PENDING → 204 No Content")
    void shouldDeleteGoodsReceipt() {
        when(goodsReceiptService.getGoodsReceiptById(10L)).thenReturn(pendingReceipt);
        doNothing().when(goodsReceiptService).deleteGoodsReceipt(10L);

        ResponseEntity<Void> response = controller.deleteGoodsReceipt(10L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(goodsReceiptService).deleteGoodsReceipt(10L);
    }

    @Test
    @DisplayName("❌ deleteGoodsReceipt - no PENDING lanza BusinessException")
    void shouldThrowWhenDeletingNonPendingReceipt() {
        when(goodsReceiptService.getGoodsReceiptById(10L)).thenReturn(receivedReceipt);

        assertThrows(BusinessException.class, () -> controller.deleteGoodsReceipt(10L));
        verify(goodsReceiptService, never()).deleteGoodsReceipt(anyLong());
    }

    @Test
    @DisplayName("❌ deleteGoodsReceipt - no encontrada lanza ResourceNotFoundException")
    void shouldThrowWhenReceiptToDeleteNotFound() {
        when(goodsReceiptService.getGoodsReceiptById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> controller.deleteGoodsReceipt(99L));
    }
}
