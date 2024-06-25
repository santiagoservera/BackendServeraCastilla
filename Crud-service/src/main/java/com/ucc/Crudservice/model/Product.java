package com.ucc.Crudservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "SKU es obligatorio")
    private String sku;

    @NotBlank(message = "Nombre es obligatorio")
    private String name;

    @NotBlank(message = "Descripcion es obligatoria")
    private String description;

    @NotNull(message = "Precio es obligatorio")
    @DecimalMin(value= "0.0", message = "Precio tiene que ser mayor o igual a 0")
    private Double price;

    @NotNull(message = "Estatus es requerido")
    private Boolean status;

}
