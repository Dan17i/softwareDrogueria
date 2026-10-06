package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.ProductDTO;
import com.drogueria.bellavista.application.mapper.ProductUseCaseMapper;
import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.domain.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock private ProductService productService;
    @Mock private ProductUseCaseMapper mapper;

    @InjectMocks
    private ProductController controller;

    private Product product;
    private ProductDTO.Response response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = Product.builder().id(1L).code("P-001").name("Acetaminofén").build();
        response = mock(ProductDTO.Response.class);
        when(mapper.toResponse(any(Product.class))).thenReturn(response);
    }

    @Test
    @DisplayName("createProduct - 201 con producto creado")
    void shouldCreateProduct() {
        when(mapper.toDomain(any(ProductDTO.CreateRequest.class))).thenReturn(product);
        when(productService.createProduct(product)).thenReturn(product);

        var result = controller.createProduct(mock(ProductDTO.CreateRequest.class));

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertSame(response, result.getBody());
    }

    @Test
    @DisplayName("updateProduct - 200 con producto actualizado")
    void shouldUpdateProduct() {
        when(mapper.toDomain(any(ProductDTO.UpdateRequest.class))).thenReturn(product);
        when(productService.updateProduct(1L, product)).thenReturn(product);

        var result = controller.updateProduct(1L, mock(ProductDTO.UpdateRequest.class));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertSame(response, result.getBody());
    }

    @Test
    @DisplayName("getProductById / ByCode - 200")
    void shouldGetSingleProduct() {
        when(productService.getProductById(1L)).thenReturn(product);
        when(productService.getProductByCode("P-001")).thenReturn(product);

        assertSame(response, controller.getProductById(1L).getBody());
        assertSame(response, controller.getProductByCode("P-001").getBody());
    }

    @Test
    @DisplayName("getAllProducts - active=true usa solo activos; otro caso lista todos")
    void shouldFilterByActive() {
        when(productService.getActiveProducts()).thenReturn(List.of(product));
        when(productService.getAllProducts()).thenReturn(List.of(product, product));

        assertEquals(1, controller.getAllProducts(true).getBody().size());
        assertEquals(2, controller.getAllProducts(false).getBody().size());
        assertEquals(2, controller.getAllProducts(null).getBody().size());
    }

    @Test
    @DisplayName("búsquedas - por nombre, categoría y reposición")
    void shouldSearchProducts() {
        when(productService.searchProductsByName("ace")).thenReturn(List.of(product));
        when(productService.getProductsByCategory("ANALGESICO")).thenReturn(List.of(product));
        when(productService.getProductsNeedingRestock()).thenReturn(List.of());

        assertEquals(1, controller.searchProducts("ace").getBody().size());
        assertEquals(1, controller.getProductsByCategory("ANALGESICO").getBody().size());
        assertTrue(controller.getProductsNeedingRestock().getBody().isEmpty());
    }

    @Test
    @DisplayName("reduceStock / increaseStock - delegan con la cantidad")
    void shouldAdjustStock() {
        when(productService.reduceStock(1L, 5)).thenReturn(product);
        when(productService.increaseStock(1L, 7)).thenReturn(product);

        assertSame(response, controller.reduceStock(1L, new ProductDTO.StockAdjustment(5)).getBody());
        assertSame(response, controller.increaseStock(1L, new ProductDTO.StockAdjustment(7)).getBody());
        verify(productService).reduceStock(1L, 5);
        verify(productService).increaseStock(1L, 7);
    }

    @Test
    @DisplayName("toggleProductStatus - 200")
    void shouldToggleStatus() {
        when(productService.toggleProductStatus(1L)).thenReturn(product);

        assertEquals(HttpStatus.OK, controller.toggleProductStatus(1L).getStatusCode());
    }

    @Test
    @DisplayName("deleteProduct - 204 sin contenido")
    void shouldDeleteProduct() {
        var result = controller.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(productService).deleteProduct(1L);
    }

    @Test
    @DisplayName("seguridad - los endpoints que modifican exigen @PreAuthorize")
    void writeEndpointsShouldBeProtected() {
        Set<String> writeMethods = Set.of("createProduct", "updateProduct", "reduceStock",
            "increaseStock", "toggleProductStatus", "deleteProduct");
        for (Method m : ProductController.class.getDeclaredMethods()) {
            if (writeMethods.contains(m.getName())) {
                assertNotNull(m.getAnnotation(PreAuthorize.class), "Sin @PreAuthorize: " + m.getName());
            }
        }
    }

    @Test
    @DisplayName("seguridad - deleteProduct solo para ADMIN")
    void deleteShouldBeAdminOnly() throws NoSuchMethodException {
        Method m = ProductController.class.getMethod("deleteProduct", Long.class);

        assertEquals("hasRole('ADMIN')", m.getAnnotation(PreAuthorize.class).value());
    }
}
