package com.example.ordenes.service;

import com.example.ordenes.model.Ordenes;
import com.example.ordenes.repositories.OrdenesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdenesService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final OrdenesRepository ordenesRepository;

    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

    /*****************METODO PARA OBTENER ORDENES*****************/
    public List<Ordenes> getOrders() {
        return ordenesRepository.findAll();
    }

    /*****************METODO PARA CREAR ORDEN*****************/
    public ResponseEntity<Object> newOrder(Ordenes orden) {
        Long productId = orden.getProductId();
        String url = "http://localhost:8083/api/products/find/" + productId;

        HttpHeaders headers = createHeaders("santi", "santi"); // Cambia esto según tus credenciales
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

            // Verificar si el producto existe
            if (response.getStatusCode() == HttpStatus.OK) {
                // Producto encontrado, guardar la orden
                ordenesRepository.save(orden);
                return new ResponseEntity<>("Order created successfully", HttpStatus.CREATED);
            } else {
                // Producto no encontrado, no guardar la orden
                return new ResponseEntity<>("Product not found, order not created", HttpStatus.NOT_FOUND);
            }
        } catch (HttpClientErrorException.NotFound ex) {
            return new ResponseEntity<>("Product not found, order not created", HttpStatus.NOT_FOUND);
        }
    }

    /*****************METODO PARA ELIMINAR ORDEN*****************/
    public ResponseEntity<Object> deleteOrder(Long id) {
        Optional<Ordenes> existingOrderOptional = ordenesRepository.findById(id);
        if (existingOrderOptional.isPresent()) {
            ordenesRepository.deleteById(id);
            return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }

    /*****************METODO PARA ACTUALIZAR ORDEN*****************/
    public ResponseEntity<Object> updateOrder(Long id, Ordenes updatedOrder) {
        Optional<Ordenes> existingOrderOptional = ordenesRepository.findById(id);
        if (existingOrderOptional.isPresent()) {
            Ordenes existingOrder = existingOrderOptional.get();
            existingOrder.setProductId(updatedOrder.getProductId());
            existingOrder.setUnitPrice(updatedOrder.getUnitPrice());
            existingOrder.setQuantity(updatedOrder.getQuantity());
            existingOrder.setTotal(updatedOrder.getTotal());
            existingOrder.setNotes(updatedOrder.getNotes());

            ordenesRepository.save(existingOrder);

            return new ResponseEntity<>("Order updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }
}
