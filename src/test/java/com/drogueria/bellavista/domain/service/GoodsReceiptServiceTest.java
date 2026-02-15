package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.GoodsReceipt;
import com.drogueria.bellavista.domain.model.GoodsReceiptItem;
import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.domain.repository.GoodsReceiptRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoodsReceiptServiceTest {

    @Mock
    private GoodsReceiptRepository goodsReceiptRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private GoodsReceiptService goodsReceiptService;

    @Captor
    private ArgumentCaptor<GoodsReceipt> receiptCaptor;

    private Order sampleOrder;
    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleOrder = Order.builder()
                .id(10L)
                .orderNumber("ORD-TEST-10")
                .status("COMPLETED")
                .supplierId(5L)
                .build();

        sampleProduct = Product.builder()
                .id(100L)
                .code("MED-TEST")
                .name("Test Product")
                .active(true)
                .stock(50)
                .build();
    }

    @Test
    void createGoodsReceipt_success() {
        GoodsReceiptItem item = GoodsReceiptItem.builder()
                .productId(sampleProduct.getId())
                .productCode(sampleProduct.getCode())
                .productName(sampleProduct.getName())
                .orderedQuantity(10)
                .receivedQuantity(10)
                .build();

        GoodsReceipt receipt = GoodsReceipt.builder()
                .orderId(sampleOrder.getId())
                .items(Collections.singletonList(item))
                .notes("Test create")
                .build();

        when(orderService.getOrderById(sampleOrder.getId())).thenReturn(sampleOrder);
        when(goodsReceiptRepository.findByOrderId(sampleOrder.getId())).thenReturn(Collections.emptyList());
        when(productService.getProductById(sampleProduct.getId())).thenReturn(sampleProduct);
        when(goodsReceiptRepository.save(any())).thenAnswer(inv -> {
            GoodsReceipt g = (GoodsReceipt) inv.getArgument(0);
            g.setId(1L);
            return g;
        });
        when(goodsReceiptRepository.existsByReceiptNumber(anyString())).thenReturn(false);

        GoodsReceipt created = goodsReceiptService.createGoodsReceipt(receipt);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getStatus()).isEqualTo("PENDING");
        assertThat(created.getReceiptNumber()).isNotNull();
        verify(goodsReceiptRepository).save(receiptCaptor.capture());
    }

    @Test
    void createGoodsReceipt_orderNotCompleted_throws() {
        Order notCompleted = Order.builder().id(11L).status("PENDING").build();
        GoodsReceipt receipt = GoodsReceipt.builder().orderId(notCompleted.getId()).build();

        when(orderService.getOrderById(notCompleted.getId())).thenReturn(notCompleted);

        assertThrows(BusinessException.class, () -> goodsReceiptService.createGoodsReceipt(receipt));
    }

    @Test
    void receiveGoodsReceipt_updatesStockAndStatus() {
        GoodsReceiptItem item = GoodsReceiptItem.builder()
                .productId(sampleProduct.getId())
                .orderedQuantity(5)
                .receivedQuantity(5)
                .build();

        GoodsReceipt receipt = GoodsReceipt.builder()
                .id(99L)
                .status("PENDING")
                .items(Collections.singletonList(item))
                .build();

        when(goodsReceiptRepository.findById(99L)).thenReturn(Optional.of(receipt));
        when(goodsReceiptRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        GoodsReceipt result = goodsReceiptService.receiveGoodsReceipt(99L);

        verify(productService).increaseStockInternal(sampleProduct.getId(), item.getReceivedQuantity());
        verify(goodsReceiptRepository).save(any());
        assertThat(result.getStatus()).isIn("RECEIVED", "PARTIALLY_RECEIVED");
    }

    @Test
    void rejectGoodsReceipt_whenReceived_throws() {
        GoodsReceipt receipt = GoodsReceipt.builder().id(50L).status("RECEIVED").build();
        when(goodsReceiptRepository.findById(50L)).thenReturn(Optional.of(receipt));

        assertThrows(BusinessException.class, () -> goodsReceiptService.rejectGoodsReceipt(50L));
    }
}
