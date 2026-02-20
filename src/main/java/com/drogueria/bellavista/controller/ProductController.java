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
 * Controlador REST - Productos
 * Puerto de entrada (Input Adapter)
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {
    
    private final ProductService productService;
    private final ProductUseCaseMapper mapper;
    
    /**
     * Crear un nuevo producto
     * POST /api/products
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
     * Actualizar un producto existente
     * PUT /api/products/{id}
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
     * Obtener un producto por ID
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO.Response> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        ProductDTO.Response response = mapper.toResponse(product);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener un producto por código
     * GET /api/products/code/{code}
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ProductDTO.Response> getProductByCode(@PathVariable String code) {
        Product product = productService.getProductByCode(code);
        ProductDTO.Response response = mapper.toResponse(product);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Listar todos los productos
     * GET /api/products
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
     * Buscar productos por nombre
     * GET /api/products/search?name=xxx
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
     * Listar productos por categoría
     * GET /api/products/category/{category}
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
     * Obtener productos que necesitan reabastecimiento
     * GET /api/products/restock-needed
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
     * Reducir stock de un producto
     * POST /api/products/{id}/reduce-stock
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
     * Aumentar stock de un producto
     * POST /api/products/{id}/increase-stock
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
     * Activar/Desactivar producto
     * PATCH /api/products/{id}/toggle-status
     */
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ProductDTO.Response> toggleProductStatus(@PathVariable Long id) {
        Product product = productService.toggleProductStatus(id);
        ProductDTO.Response response = mapper.toResponse(product);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Eliminar un producto
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
