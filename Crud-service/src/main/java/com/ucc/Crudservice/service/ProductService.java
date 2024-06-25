package com.ucc.Crudservice.service;

import com.ucc.Crudservice.model.Product;
import com.ucc.Crudservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public ResponseEntity<Object> addProduct(Product product) {
        productRepository.save(product);
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Successfully saved");
        response.put("data", product);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public ResponseEntity<Object> updateProduct(Long productId, Product updatedProduct) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            product.setSku(updatedProduct.getSku());
            product.setDescription(updatedProduct.getDescription());
            product.setStatus(updatedProduct.getStatus());
            productRepository.save(product);

            HashMap<String, Object> response = new HashMap<>();
            response.put("message", "Producto actualizado exitosamente!!");
            response.put("data", product);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            HashMap<String, Object> response = new HashMap<>();
            response.put("message", "Product not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
