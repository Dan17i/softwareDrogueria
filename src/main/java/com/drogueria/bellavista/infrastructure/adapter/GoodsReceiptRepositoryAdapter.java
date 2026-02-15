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
 * <h2>GoodsReceiptRepositoryAdapter</h2>
 *
 * <p>
 * Implementación del puerto {@link GoodsReceiptRepository}
 * utilizando Spring Data JPA como mecanismo de persistencia.
 * </p>
 *
 * <p>
 * Este adaptador pertenece a la capa de infraestructura dentro de la
 * arquitectura hexagonal (Ports & Adapters) y es responsable de:
 * </p>
 *
 * <ul>
 *     <li>Persistir la cabecera del comprobante de recepción.</li>
 *     <li>Gestionar la persistencia de sus ítems asociados.</li>
 *     <li>Convertir entre el modelo de dominio {@link GoodsReceipt}
 *     y las entidades JPA correspondientes.</li>
 * </ul>
 *
 * <p>
 * Debido a que {@code GoodsReceipt} contiene una relación con múltiples ítems,
 * este adaptador maneja explícitamente:
 * </p>
 * <ul>
 *     <li>Eliminación de ítems anteriores en caso de actualización.</li>
 *     <li>Persistencia individual de cada ítem.</li>
 *     <li>Reconstrucción completa del agregado al consultar.</li>
 * </ul>
 *
 * <p>
 * La conversión entre dominio y persistencia se delega al
 * {@link GoodsReceiptMapper}, manteniendo el dominio desacoplado
 * de detalles tecnológicos.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class GoodsReceiptRepositoryAdapter implements GoodsReceiptRepository {
    /**
     * Repositorio JPA para la entidad principal (cabecera).
     */
    private final JpaGoodsReceiptRepository jpaGoodsReceiptRepository;
    /**
     * Repositorio JPA para los ítems asociados al comprobante.
     */
    private final JpaGoodsReceiptItemRepository jpaGoodsReceiptItemRepository;
    /**
     * Mapper encargado de convertir entre dominio y entidades JPA.
     */
    private final GoodsReceiptMapper mapper;
    /**
     * Guarda o actualiza un comprobante de recepción junto con sus ítems.
     *
     * @param goodsReceipt agregado de dominio a persistir
     * @return agregado persistido reconstruido desde base de datos
     */
    @Override
    public GoodsReceipt save(GoodsReceipt goodsReceipt) {
        // Convertir agregado a entidad
        GoodsReceiptEntity entity = mapper.toEntity(goodsReceipt);
        GoodsReceiptEntity savedEntity = jpaGoodsReceiptRepository.save(entity);

        // Persistir ítems asociados
        if (goodsReceipt.getItems() != null && !goodsReceipt.getItems().isEmpty()) {
            // Si es actualización, eliminar ítems previos
            if (savedEntity.getId() != null) {
                jpaGoodsReceiptItemRepository.deleteByGoodsReceiptId(savedEntity.getId());
            }
            // Guardar nuevos ítems
            goodsReceipt.getItems().forEach(item -> {
                GoodsReceiptItemEntity itemEntity = mapper.itemToEntity(item, savedEntity.getId());
                jpaGoodsReceiptItemRepository.save(itemEntity);
            });
        }

        // Recargar ítems desde base de datos
        List<GoodsReceiptItemEntity> itemEntities = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(savedEntity.getId());
        
        return mapper.toDomain(savedEntity, itemEntities);
    }
    /**
     * Busca un comprobante por su ID.
     *
     * @param id identificador único
     * @return comprobante encontrado si existe
     */
    @Override
    public Optional<GoodsReceipt> findById(Long id) {
        return jpaGoodsReceiptRepository.findById(id).map(entity -> {
            List<GoodsReceiptItemEntity> items = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(id);
            return mapper.toDomain(entity, items);
        });
    }
    /**
     * Busca un comprobante por su número de recepción.
     *
     * @param receiptNumber número del comprobante
     * @return comprobante encontrado si existe
     */
    @Override
    public Optional<GoodsReceipt> findByReceiptNumber(String receiptNumber) {
        return jpaGoodsReceiptRepository.findByReceiptNumber(receiptNumber).map(entity -> {
            List<GoodsReceiptItemEntity> items = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(entity.getId());
            return mapper.toDomain(entity, items);
        });
    }
    /**
     * Obtiene comprobantes asociados a una orden.
     *
     * @param orderId identificador de la orden
     * @return lista de comprobantes relacionados
     */
    @Override
    public List<GoodsReceipt> findByOrderId(Long orderId) {
        return jpaGoodsReceiptRepository.findByOrderId(orderId).stream().map(entity -> {
            List<GoodsReceiptItemEntity> items = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(entity.getId());
            return mapper.toDomain(entity, items);
        }).collect(Collectors.toList());
    }
    /**
     * Obtiene comprobantes asociados a un proveedor.
     *
     * @param supplierId identificador del proveedor
     * @return lista de comprobantes relacionados
     */
    @Override
    public List<GoodsReceipt> findBySupplierId(Long supplierId) {
        return jpaGoodsReceiptRepository.findBySupplierId(supplierId).stream().map(entity -> {
            List<GoodsReceiptItemEntity> items = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(entity.getId());
            return mapper.toDomain(entity, items);
        }).collect(Collectors.toList());
    }
    /**
     * Obtiene comprobantes por estado.
     *
     * @param status estado del comprobante
     * @return lista de comprobantes
     */
    @Override
    public List<GoodsReceipt> findByStatus(String status) {
        return jpaGoodsReceiptRepository.findByStatus(status).stream().map(entity -> {
            List<GoodsReceiptItemEntity> items = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(entity.getId());
            return mapper.toDomain(entity, items);
        }).collect(Collectors.toList());
    }
    /**
     * Obtiene comprobantes pendientes de procesamiento.
     *
     * @return lista de comprobantes pendientes
     */
    @Override
    public List<GoodsReceipt> findPendingReceipts() {
        return jpaGoodsReceiptRepository.findPendingReceipts().stream().map(entity -> {
            List<GoodsReceiptItemEntity> items = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(entity.getId());
            return mapper.toDomain(entity, items);
        }).collect(Collectors.toList());
    }
    /**
     * Obtiene todos los comprobantes registrados.
     *
     * @return lista completa de comprobantes
     */
    @Override
    public List<GoodsReceipt> findAll() {
        return jpaGoodsReceiptRepository.findAll().stream().map(entity -> {
            List<GoodsReceiptItemEntity> items = jpaGoodsReceiptItemRepository.findByGoodsReceiptId(entity.getId());
            return mapper.toDomain(entity, items);
        }).collect(Collectors.toList());
    }
    /**
     * Elimina un comprobante y sus ítems asociados.
     *
     * @param id identificador del comprobante
     */
    @Override
    public void delete(Long id) {
        jpaGoodsReceiptItemRepository.deleteByGoodsReceiptId(id);
        jpaGoodsReceiptRepository.deleteById(id);
    }
    /**
     * Verifica si existe un comprobante con el número indicado.
     *
     * @param receiptNumber número del comprobante
     * @return true si existe
     */
    @Override
    public boolean existsByReceiptNumber(String receiptNumber) {
        return jpaGoodsReceiptRepository.existsByReceiptNumber(receiptNumber);
    }
}
