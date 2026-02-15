package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.GoodsReceipt;

import java.util.List;
import java.util.Optional;
/**
 * <h2>Puerto de Dominio - GoodsReceiptRepository</h2>
 *
 * <p>
 * Define el contrato que debe cumplir cualquier mecanismo de persistencia
 * relacionado con la entidad {@link GoodsReceipt}.
 * </p>
 *
 * <p>
 * Esta interfaz actúa como un <b>puerto</b> dentro de la arquitectura
 * hexagonal (Ports & Adapters), permitiendo que la lógica de negocio
 * permanezca desacoplada de la tecnología de almacenamiento utilizada.
 * </p>
 *
 * <p>
 * Las implementaciones concretas deben ubicarse en la capa de infraestructura.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
public interface GoodsReceiptRepository {
    /**
     * Guarda o actualiza una recepción de mercancía.
     *
     * @param goodsReceipt entidad a persistir
     * @return recepción persistida con posibles datos actualizados
     */
    GoodsReceipt save(GoodsReceipt goodsReceipt);
    /**
     * Busca una recepción por su identificador único.
     *
     * @param id identificador de la recepción
     * @return {@link Optional} con la recepción si existe,
     *         o vacío si no se encuentra
     */
    Optional<GoodsReceipt> findById(Long id);
    /**
     * Busca una recepción por su número único de recepción.
     *
     * @param receiptNumber número de recepción
     * @return {@link Optional} con la recepción si existe,
     *         o vacío si no se encuentra
     */
    Optional<GoodsReceipt> findByReceiptNumber(String receiptNumber);
    /**
     * Obtiene todas las recepciones asociadas a una orden de compra.
     *
     * @param orderId identificador de la orden
     * @return lista de recepciones relacionadas con la orden
     */
    List<GoodsReceipt> findByOrderId(Long orderId);
    /**
     * Obtiene todas las recepciones realizadas a un proveedor específico.
     *
     * @param supplierId identificador del proveedor
     * @return lista de recepciones del proveedor
     */
    List<GoodsReceipt> findBySupplierId(Long supplierId);
    /**
     * Obtiene recepciones según su estado
     * (ej. PENDIENTE, COMPLETADA, PARCIAL).
     *
     * @param status estado de la recepción
     * @return lista de recepciones con el estado indicado
     */
    List<GoodsReceipt> findByStatus(String status);
    /**
     * Obtiene todas las recepciones que aún se encuentran pendientes
     * de completar o validar.
     *
     * @return lista de recepciones pendientes
     */
    List<GoodsReceipt> findPendingReceipts();
    /**
     * Obtiene todas las recepciones registradas en el sistema.
     *
     * @return lista completa de recepciones
     */
    List<GoodsReceipt> findAll();
    /**
     * Elimina una recepción por su identificador.
     *
     * @param id identificador de la recepción
     */
    void delete(Long id);
    /**
     * Verifica si ya existe una recepción con el número indicado.
     *
     * @param receiptNumber número de recepción
     * @return {@code true} si existe,
     *         {@code false} en caso contrario
     */
    /**
     * Validar que el número de recepción no exista
     */
    boolean existsByReceiptNumber(String receiptNumber);
}
