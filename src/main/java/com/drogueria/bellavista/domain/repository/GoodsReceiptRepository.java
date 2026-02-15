package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.GoodsReceipt;

import java.util.List;
import java.util.Optional;

/**
 * Puerto: Repositorio de Recepción de Mercancía
 */
public interface GoodsReceiptRepository {
    
    /**
     * Guardar recepción
     */
    GoodsReceipt save(GoodsReceipt goodsReceipt);
    
    /**
     * Buscar por ID
     */
    Optional<GoodsReceipt> findById(Long id);
    
    /**
     * Buscar por número de recepción
     */
    Optional<GoodsReceipt> findByReceiptNumber(String receiptNumber);
    
    /**
     * Buscar por orden ID
     */
    List<GoodsReceipt> findByOrderId(Long orderId);
    
    /**
     * Buscar todas las recepciones de un proveedor
     */
    List<GoodsReceipt> findBySupplierId(Long supplierId);
    
    /**
     * Buscar por estado
     */
    List<GoodsReceipt> findByStatus(String status);
    
    /**
     * Buscar recepciones pendientes
     */
    List<GoodsReceipt> findPendingReceipts();
    
    /**
     * Obtener todas
     */
    List<GoodsReceipt> findAll();
    
    /**
     * Eliminar
     */
    void delete(Long id);
    
    /**
     * Validar que el número de recepción no exista
     */
    boolean existsByReceiptNumber(String receiptNumber);
}
