package com.drogueria.bellavista.application.mapper;

import com.drogueria.bellavista.application.dto.GoodsReceiptDTO;
import com.drogueria.bellavista.domain.model.GoodsReceipt;
import com.drogueria.bellavista.domain.model.GoodsReceiptItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoodsReceiptUseCaseMapperTest {

    private final GoodsReceiptUseCaseMapper mapper = new GoodsReceiptUseCaseMapper();

    @Test
    @DisplayName("toResponse - recepción sin lista de ítems devuelve lista vacía")
    void shouldReturnEmptyItemsWhenReceiptItemsAreNull() {
        GoodsReceipt receipt = GoodsReceipt.builder().id(1L).receiptNumber("GR-1").status("PENDING").build();
        receipt.setItems(null);

        GoodsReceiptDTO.Response response = mapper.toResponse(receipt);

        assertEquals("GR-1", response.receiptNumber());
        assertNotNull(response.items());
        assertTrue(response.items().isEmpty());
    }

    @Test
    @DisplayName("toResponse - mapea cada ítem de la recepción")
    void shouldMapItems() {
        List<GoodsReceiptItem> items = new ArrayList<>();
        items.add(GoodsReceiptItem.builder().productId(5L).productCode("P-1").productName("Producto").build());
        GoodsReceipt receipt = GoodsReceipt.builder().id(1L).receiptNumber("GR-2").status("PENDING").items(items).build();

        GoodsReceiptDTO.Response response = mapper.toResponse(receipt);

        assertEquals(1, response.items().size());
        assertEquals("P-1", response.items().get(0).productCode());
    }
}
