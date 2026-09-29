package com.drogueria.bellavista.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoodsReceiptTest {

    private GoodsReceiptItem itemWithReceived(int ordered, int received) {
        return GoodsReceiptItem.builder()
            .orderedQuantity(ordered)
            .receivedQuantity(received)
            .build();
    }

    @Test
    @DisplayName("✅ receive - todas las cantidades completas → RECEIVED")
    void shouldMarkAsReceivedWhenFullyReceived() {
        GoodsReceipt receipt = GoodsReceipt.builder()
            .status("PENDING")
            .items(List.of(itemWithReceived(5, 5)))
            .build();

        receipt.receive();

        assertEquals("RECEIVED", receipt.getStatus());
        assertNotNull(receipt.getActualDeliveryDate());
    }

    @Test
    @DisplayName("✅ receive - cantidad parcial → PARTIALLY_RECEIVED")
    void shouldMarkAsPartiallyReceived() {
        GoodsReceipt receipt = GoodsReceipt.builder()
            .status("PENDING")
            .items(List.of(itemWithReceived(5, 2)))
            .build();

        receipt.receive();

        assertEquals("PARTIALLY_RECEIVED", receipt.getStatus());
    }

    @Test
    @DisplayName("❌ receive - todas las cantidades en 0 → lanza excepción, no queda PENDING silencioso")
    void shouldRejectReceiveWhenNothingWasReceived() {
        GoodsReceipt receipt = GoodsReceipt.builder()
            .status("PENDING")
            .items(List.of(itemWithReceived(5, 0)))
            .build();

        assertThrows(IllegalStateException.class, receipt::receive);
        assertEquals("PENDING", receipt.getStatus());
    }
}
