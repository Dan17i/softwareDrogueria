package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.repository.OrderRepository;
import com.drogueria.bellavista.infrastructure.mapper.OrderMapper;
import com.drogueria.bellavista.infrastructure.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * <h2>OrderRepositoryAdapter</h2>
 *
 * <p>
 * Implementación del puerto {@link OrderRepository}
 * utilizando Spring Data JPA como tecnología de persistencia.
 * </p>
 *
 * <p>
 * Este adaptador forma parte de la capa de infraestructura
 * dentro de la arquitectura hexagonal (Ports & Adapters) y
 * actúa como puente entre:
 * </p>
 *
 * <ul>
 *     <li>El modelo de dominio {@link Order}</li>
 *     <li>Las entidades JPA ({@link OrderEntity}, {@link OrderItemEntity})</li>
 *     <li>Los repositorios Spring Data</li>
 * </ul>
 *
 * <p>
 * Debido a que {@code Order} es un agregado que contiene múltiples ítems,
 * esta clase se encarga de:
 * </p>
 * <ul>
 *     <li>Persistir la cabecera de la orden.</li>
 *     <li>Persistir sus ítems asociados.</li>
 *     <li>Reconstruir el agregado completo al consultar.</li>
 * </ul>
 *
 * <p>
 * La conversión entre dominio y entidades JPA se delega al
 * {@link OrderMapper}, garantizando el desacoplamiento
 * del núcleo de negocio respecto a la infraestructura.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {
    /**
     * Repositorio JPA para la entidad principal (orden).
     */
    private final JpaOrderRepository jpaRepository;
    /**
     * Repositorio JPA para los ítems asociados a la orden.
     */
    private final JpaOrderItemRepository jpaItemRepository;
    /**
     * Mapper encargado de convertir entre dominio y entidades JPA.
     */
    private final OrderMapper mapper;
    /**
     * Guarda o actualiza una orden junto con sus ítems.
     *
     * @param order agregado de dominio a persistir
     * @return orden persistida y reconstruida desde base de datos
     */
    @Override
    public Order save(Order order) {
        // Persistir cabecera
        OrderEntity orderEntity = mapper.toEntity(order);
        OrderEntity savedOrderEntity = jpaRepository.save(orderEntity);

        // Persistir ítems asociados
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            order.getItems().forEach(item -> {
                OrderItemEntity itemEntity = mapper.itemToEntity(item, savedOrderEntity.getId());
                jpaItemRepository.save(itemEntity);
            });
        }
        // Recargar ítems para reconstruir el agregado
        List<OrderItemEntity> itemEntities = jpaItemRepository.findByOrderId(savedOrderEntity.getId());
        
        return mapper.toDomain(savedOrderEntity, itemEntities);
    }
    /**
     * Busca una orden por su identificador único.
     *
     * @param id identificador de la orden
     * @return orden encontrada si existe
     */
    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id)
            .map(orderEntity -> {
                List<OrderItemEntity> items = jpaItemRepository.findByOrderId(id);
                return mapper.toDomain(orderEntity, items);
            });
    }
    /**
     * Busca una orden por su número único.
     *
     * @param orderNumber número de la orden
     * @return orden encontrada si existe
     */
    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return jpaRepository.findByOrderNumber(orderNumber)
            .map(orderEntity -> {
                List<OrderItemEntity> items = jpaItemRepository.findByOrderId(orderEntity.getId());
                return mapper.toDomain(orderEntity, items);
            });
    }
    /**
     * Obtiene todas las órdenes registradas.
     *
     * @return lista de órdenes
     */
    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll().stream()
            .map(orderEntity -> {
                List<OrderItemEntity> items = jpaItemRepository.findByOrderId(orderEntity.getId());
                return mapper.toDomain(orderEntity, items);
            })
            .collect(Collectors.toList());
    }
    /**
     * Obtiene órdenes asociadas a un cliente.
     *
     * @param customerId identificador del cliente
     * @return lista de órdenes
     */
    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
            .map(orderEntity -> {
                List<OrderItemEntity> items = jpaItemRepository.findByOrderId(orderEntity.getId());
                return mapper.toDomain(orderEntity, items);
            })
            .collect(Collectors.toList());
    }
    /**
     * Obtiene órdenes por estado.
     *
     * @param status estado de la orden
     * @return lista de órdenes
     */
    @Override
    public List<Order> findByStatus(String status) {
        return jpaRepository.findByStatus(status).stream()
            .map(orderEntity -> {
                List<OrderItemEntity> items = jpaItemRepository.findByOrderId(orderEntity.getId());
                return mapper.toDomain(orderEntity, items);
            })
            .collect(Collectors.toList());
    }
    /**
     * Obtiene órdenes por cliente y estado.
     *
     * @param customerId identificador del cliente
     * @param status estado de la orden
     * @return lista de órdenes
     */
    @Override
    public List<Order> findByCustomerIdAndStatus(Long customerId, String status) {
        return jpaRepository.findByCustomerIdAndStatus(customerId, status).stream()
            .map(orderEntity -> {
                List<OrderItemEntity> items = jpaItemRepository.findByOrderId(orderEntity.getId());
                return mapper.toDomain(orderEntity, items);
            })
            .collect(Collectors.toList());
    }
    /**
     * Obtiene órdenes dentro de un rango de fechas.
     *
     * @param startDate fecha inicial
     * @param endDate fecha final
     * @return lista de órdenes
     */
    @Override
    public List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByDateRange(startDate, endDate).stream()
            .map(orderEntity -> {
                List<OrderItemEntity> items = jpaItemRepository.findByOrderId(orderEntity.getId());
                return mapper.toDomain(orderEntity, items);
            })
            .collect(Collectors.toList());
    }
    /**
     * Elimina una orden y sus ítems asociados.
     *
     * @param id identificador de la orden
     */
    @Override
    public void delete(Long id) {
        jpaItemRepository.deleteByOrderId(id);
        jpaRepository.deleteById(id);
    }
    /**
     * Verifica si existe una orden con el número indicado.
     *
     * @param orderNumber número de la orden
     * @return true si existe
     */
    @Override
    public boolean existsByOrderNumber(String orderNumber) {
        return jpaRepository.existsByOrderNumber(orderNumber);
    }
}
