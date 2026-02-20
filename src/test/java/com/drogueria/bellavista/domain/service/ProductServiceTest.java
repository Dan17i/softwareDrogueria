package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.domain.repository.ProductRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ProductService
 * 
 * Nota: Estos tests NO necesitan Spring Boot ni base de datos
 * Solo prueban la lógica de negocio usando mocks
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Tests")
class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductService productService;
    
    private Product sampleProduct;
    
    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .id(1L)
                .code("MED001")
                .name("Acetaminofén 500mg")
                .description("Analgésico")
                .price(new BigDecimal("5000.00"))
                .stock(100)
                .minStock(20)
                .category("Medicamentos")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    @Test
    @DisplayName("Debe crear un producto exitosamente")
    void shouldCreateProductSuccessfully() {
        // Given
        when(productRepository.existsByCode(anyString())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);
        
        // When
        Product createdProduct = productService.createProduct(sampleProduct);
        
        // Then
        assertNotNull(createdProduct);
        assertEquals("MED001", createdProduct.getCode());
        assertEquals("Acetaminofén 500mg", createdProduct.getName());
        assertTrue(createdProduct.getActive());
        
        verify(productRepository).existsByCode("MED001");
        verify(productRepository).save(any(Product.class));
    }
    
    @Test
    @DisplayName("No debe crear producto con código duplicado")
    void shouldNotCreateProductWithDuplicateCode() {
        // Given
        when(productRepository.existsByCode("MED001")).thenReturn(true);
        
        // When & Then
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> productService.createProduct(sampleProduct)
        );
        
        assertTrue(exception.getMessage().contains("Ya existe un producto"));
        verify(productRepository).existsByCode("MED001");
        verify(productRepository, never()).save(any(Product.class));
    }
    
    @Test
    @DisplayName("Debe obtener producto por ID")
    void shouldGetProductById() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        
        // When
        Product foundProduct = productService.getProductById(1L);
        
        // Then
        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getId());
        assertEquals("MED001", foundProduct.getCode());
        
        verify(productRepository).findById(1L);
    }
    
    @Test
    @DisplayName("Debe lanzar excepción si producto no existe")
    void shouldThrowExceptionWhenProductNotFound() {
        // Given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> productService.getProductById(999L)
        );
        
        assertTrue(exception.getMessage().contains("no encontrado"));
        verify(productRepository).findById(999L);
    }
    
    @Test
    @DisplayName("Debe reducir stock exitosamente")
    void shouldReduceStockSuccessfully() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);
        
        // When
        Product updatedProduct = productService.reduceStock(1L, 10);
        
        // Then
        assertEquals(90, updatedProduct.getStock());
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }
    
    @Test
    @DisplayName("No debe reducir stock si es insuficiente")
    void shouldNotReduceStockIfInsufficient() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        
        // When & Then
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> productService.reduceStock(1L, 150)
        );
        
        assertTrue(exception.getMessage().contains("Stock insuficiente"));
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }
    
    @Test
    @DisplayName("Debe aumentar stock exitosamente")
    void shouldIncreaseStockSuccessfully() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);
        
        // When
        Product updatedProduct = productService.increaseStock(1L, 50);
        
        // Then
        assertEquals(150, updatedProduct.getStock());
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }
    
    @Test
    @DisplayName("Debe obtener todos los productos activos")
    void shouldGetAllActiveProducts() {
        // Given
        Product product2 = Product.builder()
                .id(2L)
                .code("MED002")
                .name("Ibuprofeno")
                .active(true)
                .build();
        
        List<Product> activeProducts = Arrays.asList(sampleProduct, product2);
        when(productRepository.findAllActive()).thenReturn(activeProducts);
        
        // When
        List<Product> result = productService.getActiveProducts();
        
        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(Product::getActive));
        verify(productRepository).findAllActive();
    }
    
    @Test
    @DisplayName("Debe obtener productos que necesitan reabastecimiento")
    void shouldGetProductsNeedingRestock() {
        // Given
        Product lowStockProduct = Product.builder()
                .id(2L)
                .code("MED002")
                .name("Aspirina")
                .stock(10)
                .minStock(20)
                .build();
        
        when(productRepository.findProductsNeedingRestock())
                .thenReturn(Arrays.asList(lowStockProduct));
        
        // When
        List<Product> result = productService.getProductsNeedingRestock();
        
        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).needsRestock());
        verify(productRepository).findProductsNeedingRestock();
    }
    
    @Test
    @DisplayName("Debe activar/desactivar producto")
    void shouldToggleProductStatus() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);
        
        // When
        Product toggledProduct = productService.toggleProductStatus(1L);
        
        // Then
        assertFalse(toggledProduct.getActive());
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }
    
    @Test
    @DisplayName("Debe actualizar producto exitosamente")
    void shouldUpdateProductSuccessfully() {
        // Given
        Product updateData = Product.builder()
                .code("MED001")
                .name("Acetaminofén 500mg Actualizado")
                .price(new BigDecimal("5500.00"))
                .stock(120)
                .minStock(25)
                .category("Medicamentos")
                .active(true)
                .build();
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);
        
        // When
        Product updatedProduct = productService.updateProduct(1L, updateData);
        
        // Then
        assertEquals("Acetaminofén 500mg Actualizado", updatedProduct.getName());
        assertEquals(new BigDecimal("5500.00"), updatedProduct.getPrice());
        assertEquals(120, updatedProduct.getStock());
        
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }
    
    @Test
    @DisplayName("Debe eliminar producto exitosamente")
    void shouldDeleteProductSuccessfully() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        doNothing().when(productRepository).deleteById(1L);
        
        // When
        productService.deleteProduct(1L);
        
        // Then
        verify(productRepository).findById(1L);
        verify(productRepository).deleteById(1L);
    }
}
