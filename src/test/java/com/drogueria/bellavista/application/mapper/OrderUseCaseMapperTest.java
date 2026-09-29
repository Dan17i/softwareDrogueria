package com.drogueria.bellavista.application.mapper;

import com.drogueria.bellavista.application.dto.OrderDTO;
import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.domain.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderUseCaseMapperTest {

    @Mock private ProductService productService;
    @InjectMocks private OrderUseCaseMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("✅ toDomain - copia supplierId del CreateRequest a la orden de dominio")
    void shouldCopySupplierIdToDomain() {
        Product product = Product.builder()
            .id(1L).code("P-1").name("Producto").price(BigDecimal.TEN).build();
        when(productService.getProductById(1L)).thenReturn(product);

        OrderDTO.CreateRequest request = OrderDTO.CreateRequest.builder()
            .customerId(10L)
            .supplierId(20L)
            .items(List.of(OrderDTO.OrderItemRequest.builder().productId(1L).quantity(2).build()))
            .build();

        Order order = mapper.toDomain(request);

        assertEquals(20L, order.getSupplierId());
    }
}
