package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.domain.model.GoodsReceipt;
import com.drogueria.bellavista.domain.repository.GoodsReceiptRepository;
import com.drogueria.bellavista.infrastructure.mapper.GoodsReceiptMapper;
import com.drogueria.bellavista.infrastructure.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter: Implementa el puerto GoodsReceiptRepository usando JPA
 */
@Component
@RequiredArgsConstructor
public class GoodsReceiptRepositoryAdapter implements GoodsReceiptRepository {
    
    private final JpaGoodsReceiptRepository jpaGoodsReceiptRepository;
    private final JpaGoodsReceiptItemRepository jpaGoodsReceiptItemRepository;
    private final GoodsReceiptMapper mapper;
    
    @Override
    public GoodsReceipt save(GoodsReceipt goodsReceipt) {
        // Convertir a Entity
        GoodsReceiptEntity entity = mapper.toEntity(goodsReceipt);
        GoodsReceiptEntity savedEntity = jpaGoodsReceiptRepository.save(entity);
        
        // Guardar items
        if (goodsReceipt.getItems() != null && !goodsReceipt.getItems().isEmpty()) {
            // Eliminar items antiguos si es actualización
            if (savedEntity.getId() != null) {
                jpaGoodsReceiptItemRepository.deleteByGoodsReceiptId(savedEntity.getId());
            }
            
            // Guardar items nuevos
            goodsReceipt.getItems().forEach(item -> {
                GoodsReceiptItemEntity itemEntity = mapper.itemToEntity(item, savedEntity.getId());
                jpaGoodsReceiptItemRepository.save(itemEntity);
            });
        }
        
        // Recargar items
        List<GoodsReceiptItemEntity> itemEntities = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(savedEntity.getId());
        
        return mapper.toDomain(savedEntity, itemEntities);
    }
    
    @Override
    public Optional<GoodsReceipt> findById(Long id) {
        return jpaGoodsReceiptRepository.findById(id).map(entity -> {
            List<GoodsReceiptItemEntity> items = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(id);
            return mapper.toDomain(entity, items);
        });
    }
    
    @Override
    public Optional<GoodsReceipt> findByReceiptNumber(String receiptNumber) {
        return jpaGoodsReceiptRepository.findByReceiptNumber(receiptNumber).map(entity -> {
            List<GoodsReceiptItemEntity> items = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(entity.getId());
            return mapper.toDomain(entity, items);
        });
    }
    
    @Override
    public List<GoodsReceipt> findByOrderId(Long orderId) {
        return jpaGoodsReceiptRepository.findByOrderId(orderId).stream().map(entity -> {
            List<GoodsReceiptItemEntity> items = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(entity.getId());
            return mapper.toDomain(entity, items);
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<GoodsReceipt> findBySupplierId(Long supplierId) {
        return jpaGoodsReceiptRepository.findBySupplierId(supplierId).stream().map(entity -> {
            List<GoodsReceiptItemEntity> items = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(entity.getId());
            return mapper.toDomain(entity, items);
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<GoodsReceipt> findByStatus(String status) {
        return jpaGoodsReceiptRepository.findByStatus(status).stream().map(entity -> {
            List<GoodsReceiptItemEntity> items = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(entity.getId());
            return mapper.toDomain(entity, items);
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<GoodsReceipt> findPendingReceipts() {
        return jpaGoodsReceiptRepository.findPendingReceipts().stream().map(entity -> {
            List<GoodsReceiptItemEntity> items = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(entity.getId());
            return mapper.toDomain(entity, items);
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<GoodsReceipt> findAll() {
        return jpaGoodsReceiptRepository.findAll().stream().map(entity -> {
            List<GoodsReceiptItemEntity> items = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(entity.getId());
            return mapper.toDomain(entity, items);
        }).collect(Collectors.toList());
    }
    
    @Override
    public void delete(Long id) {
        jpaGoodsReceiptItemRepository.deleteByGoodsReceiptId(id);
        jpaGoodsReceiptRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByReceiptNumber(String receiptNumber) {
        return jpaGoodsReceiptRepository.existsByReceiptNumber(receiptNumber);
    }
}
