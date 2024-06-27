package com.example.ordenes.service;

import com.example.ordenes.model.Ordenes;
import com.example.ordenes.repositories.OrdenesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrdenesServiceTest {

    @Mock
    private OrdenesRepository ordenesRepository;

    @InjectMocks
    private OrdenesService ordenesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrdenes() {
        Ordenes orden = new Ordenes(1L, "ORD001", "Orden1", 1L, BigDecimal.valueOf(100.0), 1L, 100.0, "Notas");
        List<Ordenes> ordenes = Collections.singletonList(orden);

        when(ordenesRepository.findAll()).thenReturn(ordenes);

        List<Ordenes> result = ordenesService.getOrders();
        assertEquals(1, result.size());
        assertEquals("ORD001", result.get(0).getCodigo());
    }

    @Test
    public void testAddOrden() {
        Ordenes orden = new Ordenes(1L, "ORD001", "Orden1", 1L, BigDecimal.valueOf(100.0), 1L, 100.0, "Notas");
        when(ordenesRepository.save(orden)).thenReturn(orden);

        ResponseEntity<Object> response = ordenesService.newOrder(orden);

        // Verificar el comportamiento
        verify(ordenesRepository, times(1)).save(orden);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Verificar el contenido de la respuesta
        Object responseBody = response.getBody();
        assertNotNull(responseBody);
        if (responseBody instanceof String) {
            assertEquals("Order created successfully", responseBody); // Assuming response is a simple String
        } else if (responseBody instanceof HashMap) {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> responseEntity = (HashMap<String, Object>) responseBody;
            assertEquals("Order created successfully", responseEntity.get("message"));
            assertEquals(orden, responseEntity.get("data"));
        } else {
            fail("Unexpected response body type: " + responseBody.getClass().getName());
        }
    }

    @Test
    public void testDeleteOrden() {
        Long ordenId = 1L;

        // Simular la existencia del orden con el ID 1L
        Ordenes orden = new Ordenes(ordenId, "ORD001", "Orden1", 1L, BigDecimal.valueOf(100.0), 1L, 100.0, "Notas");
        when(ordenesRepository.findById(ordenId)).thenReturn(Optional.of(orden));

        // Ejecutar el método que queremos probar
        ordenesService.deleteOrder(ordenId);

        // Verificar que se haya llamado a deleteById con el ordenId
        verify(ordenesRepository, times(1)).deleteById(ordenId);
    }

    @Test
    public void testUpdateOrden() {
        Long ordenId = 1L;
        Ordenes orden = new Ordenes(ordenId, "ORD001", "Orden1", 1L, BigDecimal.valueOf(100.0), 1L, 100.0, "Notas");
        Ordenes updatedOrden = new Ordenes(ordenId, "ORD002", "Orden2", 2L, BigDecimal.valueOf(200.0), 2L, 200.0, "Updated notas");

        when(ordenesRepository.findById(ordenId)).thenReturn(Optional.of(orden));
        when(ordenesRepository.save(any(Ordenes.class))).thenReturn(orden);

        ordenesService.updateOrder(ordenId, updatedOrden);

        verify(ordenesRepository, times(1)).save(orden);
        assertEquals("ORD001", orden.getCodigo());
        assertEquals("Orden1", orden.getName());
        assertEquals(2L, orden.getProductId());
        assertEquals(BigDecimal.valueOf(200.0), orden.getUnitPrice());
        assertEquals(2L, orden.getQuantity());
        assertEquals(200.0, orden.getTotal());
        assertEquals("Updated notas", orden.getNotes());
    }

    @Test
    public void testUpdateOrdenNotFound() {
        Long ordenId = 1L;
        Ordenes updatedOrden = new Ordenes(ordenId, "ORD002", "Orden2", 2L, BigDecimal.valueOf(200.0), 2L, 200.0, "Updated notas");

        when(ordenesRepository.findById(ordenId)).thenReturn(Optional.empty());

        ordenesService.updateOrder(ordenId, updatedOrden);

        verify(ordenesRepository, never()).save(any(Ordenes.class));
    }
}
