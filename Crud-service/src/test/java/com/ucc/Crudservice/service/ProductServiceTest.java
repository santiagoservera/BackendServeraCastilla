package com.ucc.Crudservice.service;

import com.ucc.Crudservice.model.Product;
import com.ucc.Crudservice.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProducts() {
        Product product = new Product(1L, "SKU001", "Product1", "Description1", 100.0, true);
        List<Product> products = Collections.singletonList(product);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getProducts();
        assertEquals(1, result.size());
        assertEquals("SKU001", result.get(0).getSku());
    }

    @Test
    public void testAddProduct() {
        Product product = new Product(1L, "SKU001", "Product1", "Description1", 100.0, true);
        when(productRepository.save(product)).thenReturn(product);

        ResponseEntity<Object> response = productService.addProduct(product);

        // Verificar el comportamiento
        verify(productRepository, times(1)).save(product);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Verificar el contenido de la respuesta
        HashMap<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        assertEquals("Successfully saved", responseBody.get("message"));
        assertEquals(product, responseBody.get("data"));
    }

    @Test
    public void testDeleteProduct() {
        Long productId = 1L;
        doNothing().when(productRepository).deleteById(productId);

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testUpdateProduct() {
        Long productId = 1L;
        Product product = new Product(productId, "SKU001", "Product1", "Description1", 100.0, true);
        Product updatedProduct = new Product(productId, "SKU002", "Product2", "Description2", 200.0, false);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.updateProduct(productId, updatedProduct);

        verify(productRepository, times(1)).save(product);
        assertEquals("SKU002", product.getSku());
        assertEquals("Product2", product.getName());
        assertEquals("Description2", product.getDescription());
        assertEquals(200.0, product.getPrice());
        assertFalse(product.getStatus());
    }

    @Test
    public void testUpdateProductNotFound() {
        Long productId = 1L;
        Product updatedProduct = new Product(productId, "SKU002", "Product2", "Description2", 200.0, false);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        productService.updateProduct(productId, updatedProduct);

        verify(productRepository, never()).save(any(Product.class));
    }
}

