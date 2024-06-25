package com.ucc.Crudservice.repositories;

import com.ucc.Crudservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
