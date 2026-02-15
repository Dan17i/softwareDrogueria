package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * <h2>Puerto de Dominio - OrderRepository</h2>
 *
 * <p>
 * Define el contrato que debe cumplir cualquier mecanismo de persistencia
 * relacionado con la entidad {@link Order}.
 * </p>
 *
 * <p>
 * Esta interfaz actúa como un <b>puerto</b> en la arquitectura hexagonal,
 * permitiendo desacoplar la lógica de negocio del detalle técnico
 * de almacenamiento (JPA, JDBC, API externa, memoria, etc.).
 * </p>
 *
 * <p>
 * Las implementaciones concretas deben ubicarse en la capa de infraestructura.
 * </p>
 * @author Daniel Jurado & equipo de dasarrollo
 * @since 1.0
 */
public interface OrderRepository {
    /**
     * Guarda o actualiza una orden en el sistema.
     *
     * @param order entidad a persistir
     * @return orden persistida con posibles datos actualizados
     */
    Order save(Order order);
    /**
     * Busca una orden por su identificador único.
     *
     * @param id identificador de la orden
     * @return {@link Optional} con la orden si existe,
     *         o vacío si no se encuentra
     */
    Optional<Order> findById(Long id);
    /**
     * Busca una orden por su número único de orden.
     *
     * @param orderNumber número de orden
     * @return {@link Optional} con la orden si existe,
     *         o vacío si no se encuentra
     */
    Optional<Order> findByOrderNumber(String orderNumber);
    /**
     * Obtiene todas las órdenes registradas en el sistema.
     *
     * @return lista completa de órdenes
     */
    List<Order> findAll();
    /**
     * Obtiene todas las órdenes asociadas a un cliente específico.
     *
     * @param customerId identificador del cliente
     * @return lista de órdenes del cliente
     */
    List<Order> findByCustomerId(Long customerId);
    /**
     * Obtiene órdenes según su estado
     * (ej. PENDIENTE, CONFIRMADA, CANCELADA, ENTREGADA).
     *
     * @param status estado de la orden
     * @return lista de órdenes con el estado indicado
     */
    List<Order> findByStatus(String status);
    /**
     * Obtiene órdenes filtradas por cliente y estado.
     *
     * @param customerId identificador del cliente
     * @param status estado de la orden
     * @return lista de órdenes que coinciden con ambos criterios
     */
    List<Order> findByCustomerIdAndStatus(Long customerId, String status);
    /**
     * Obtiene órdenes dentro de un rango de fechas.
     *
     * @param startDate fecha inicial del rango
     * @param endDate fecha final del rango
     * @return lista de órdenes registradas en el intervalo indicado
     */
    List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    /**
     * Elimina una orden por su identificador.
     *
     * @param id identificador de la orden
     */
    void delete(Long id);
    /**
     * Verifica si ya existe una orden con el número indicado.
     *
     * @param orderNumber número de orden
     * @return {@code true} si existe,
     *         {@code false} en caso contrario
     */
    boolean existsByOrderNumber(String orderNumber);
}
