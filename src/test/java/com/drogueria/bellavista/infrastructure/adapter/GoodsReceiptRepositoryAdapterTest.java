package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.infrastructure.mapper.GoodsReceiptMapper;
import com.drogueria.bellavista.infrastructure.persistence.JpaGoodsReceiptItemRepository;
import com.drogueria.bellavista.infrastructure.persistence.JpaGoodsReceiptRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class GoodsReceiptRepositoryAdapterTest {

    private final JpaGoodsReceiptRepository jpaRepository = mock(JpaGoodsReceiptRepository.class);
    private final JpaGoodsReceiptItemRepository jpaItemRepository = mock(JpaGoodsReceiptItemRepository.class);
    private final GoodsReceiptMapper mapper = mock(GoodsReceiptMapper.class);
    private final GoodsReceiptRepositoryAdapter adapter =
            new GoodsReceiptRepositoryAdapter(jpaRepository, jpaItemRepository, mapper);

    @Test
    @DisplayName("save - recepción null lanza NullPointerException sin tocar la BD")
    void shouldRejectNullReceipt() {
        assertThrows(NullPointerException.class, () -> adapter.save(null));

        verifyNoInteractions(jpaRepository, jpaItemRepository, mapper);
    }
}
