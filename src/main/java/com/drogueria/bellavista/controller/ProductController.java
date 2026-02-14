package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.ProductDTO;
import com.drogueria.bellavista.application.mapper.ProductUseCaseMapper;
import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.domain.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Controlador REST para la gestión de productos.
 * <p>
 * Expone endpoints para crear, actualizar, consultar, buscar, ajustar stock,
 * activar/desactivar y eliminar productos. Actúa como puerto de entrada (Input Adapter)
 * en la arquitectura hexagonal, delegando la lógica de negocio a {@link ProductService}.
 * </p>
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {
    
    private final ProductService productService;
    private final ProductUseCaseMapper mapper;
    /**
     * Crea un nuevo producto.
     * POST /products
     *
     * @param request DTO con los datos del producto a crear
     * @return {@link ResponseEntity} con {@link ProductDTO.Response} del producto creado
     */
    @PostMapping
    public ResponseEntity<ProductDTO.Response> createProduct(
            @Valid @RequestBody ProductDTO.CreateRequest request) {
        
        Product product = mapper.toDomain(request);
        Product createdProduct = productService.createProduct(product);
        ProductDTO.Response response = mapper.toResponse(createdProduct);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    /**
     * Actualiza un producto existente.
     * PUT /products/{id}
     *
     * @param id ID del producto a actualizar
     * @param request DTO con los datos a actualizar
     * @return {@link ResponseEntity} con {@link ProductDTO.Response} del producto actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO.Response> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO.UpdateRequest request) {
        
        Product product = mapper.toDomain(request);
        Product updatedProduct = productService.updateProduct(id, product);
        ProductDTO.Response response = mapper.toResponse(updatedProduct);
        
        return ResponseEntity.ok(response);
    }
    /**
     * Obtiene un producto por su ID.
     * GET /products/{id}
     *
     * @param id ID del producto
     * @return {@link ResponseEntity} con {@link ProductDTO.Response} del producto
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO.Response> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        ProductDTO.Response response = mapper.toResponse(product);
        return ResponseEntity.ok(response);
    }
    /**
     * Obtiene un producto por su código.
     * GET /products/code/{code}
     *
     * @param code Código único del producto
     * @return {@link ResponseEntity} con {@link ProductDTO.Response} del producto
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ProductDTO.Response> getProductByCode(@PathVariable String code) {
        Product product = productService.getProductByCode(code);
        ProductDTO.Response response = mapper.toResponse(product);
        return ResponseEntity.ok(response);
    }
    /**
     * Lista todos los productos.
     * GET /products
     *
     * @param active (Opcional) Filtrar solo productos activos si es true
     * @return {@link ResponseEntity} con lista de {@link ProductDTO.Response}
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO.Response>> getAllProducts(
            @RequestParam(required = false) Boolean active) {
        
        List<Product> products = active != null && active 
            ? productService.getActiveProducts() 
            : productService.getAllProducts();
        
        List<ProductDTO.Response> response = products.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    /**
     * Busca productos por nombre.
     * GET /products/search?name=xxx
     *
     * @param name Nombre (o parte del nombre) del producto a buscar
     * @return {@link ResponseEntity} con lista de {@link ProductDTO.Response} que coinciden
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO.Response>> searchProducts(
            @RequestParam String name) {
        
        List<Product> products = productService.searchProductsByName(name);
        List<ProductDTO.Response> response = products.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    /**
     * Lista productos por categoría.
     * GET /products/category/{category}
     *
     * @param category Categoría del producto
     * @return {@link ResponseEntity} con lista de {@link ProductDTO.Response}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDTO.Response>> getProductsByCategory(
            @PathVariable String category) {
        
        List<Product> products = productService.getProductsByCategory(category);
        List<ProductDTO.Response> response = products.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    /**
     * Obtiene productos que necesitan reabastecimiento.
     * GET /products/restock-needed
     *
     * @return {@link ResponseEntity} con lista de {@link ProductDTO.Response}
     */
    @GetMapping("/restock-needed")
    public ResponseEntity<List<ProductDTO.Response>> getProductsNeedingRestock() {
        List<Product> products = productService.getProductsNeedingRestock();
        List<ProductDTO.Response> response = products.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Reduce el stock de un producto.
     * POST /products/{id}/reduce-stock
     *
     * @param id ID del producto
     * @param adjustment DTO con cantidad a reducir
     * @return {@link ResponseEntity} con {@link ProductDTO.Response} actualizado
     */
    @PostMapping("/{id}/reduce-stock")
    public ResponseEntity<ProductDTO.Response> reduceStock(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO.StockAdjustment adjustment) {
        
        Product product = productService.reduceStock(id, adjustment.getQuantity());
        ProductDTO.Response response = mapper.toResponse(product);
        
        return ResponseEntity.ok(response);
    }
    /**
     * Aumenta el stock de un producto.
     * POST /products/{id}/increase-stock
     *
     * @param id ID del producto
     * @param adjustment DTO con cantidad a aumentar
     * @return {@link ResponseEntity} con {@link ProductDTO.Response} actualizado
     */
    @PostMapping("/{id}/increase-stock")
    public ResponseEntity<ProductDTO.Response> increaseStock(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO.StockAdjustment adjustment) {
        
        Product product = productService.increaseStock(id, adjustment.getQuantity());
        ProductDTO.Response response = mapper.toResponse(product);
        
        return ResponseEntity.ok(response);
    }
    /**
     * Activa o desactiva un producto.
     * PATCH /products/{id}/toggle-status
     *
     * @param id ID del producto
     * @return {@link ResponseEntity} con {@link ProductDTO.Response} actualizado
     */
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ProductDTO.Response> toggleProductStatus(@PathVariable Long id) {
        Product product = productService.toggleProductStatus(id);
        ProductDTO.Response response = mapper.toResponse(product);
        return ResponseEntity.ok(response);
    }
    /**
     * Elimina un producto.
     * DELETE /products/{id}
     *
     * @param id ID del producto a eliminar
     * @return {@link ResponseEntity} vacío con status NO_CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
