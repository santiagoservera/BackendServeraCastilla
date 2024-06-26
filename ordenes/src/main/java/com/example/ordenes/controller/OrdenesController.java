package com.example.ordenes.controller;

import com.example.ordenes.model.Ordenes;
import com.example.ordenes.service.OrdenesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenesController {

    private final OrdenesService ordenesService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Ordenes> getOrders() {
        System.out.println("Probando obtener orden");
        return ordenesService.getOrders();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> newOrder(@Valid @RequestBody Ordenes orden, BindingResult bindingResult) {
        System.out.println("Probando crear orden");
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        return ordenesService.newOrder(orden);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateOrder(@PathVariable("id") Long id, @Valid @RequestBody Ordenes updatedOrder, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return ordenesService.updateOrder(id, updatedOrder);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteOrder(@PathVariable("id") Long id) {
        return ordenesService.deleteOrder(id);
    }
}
