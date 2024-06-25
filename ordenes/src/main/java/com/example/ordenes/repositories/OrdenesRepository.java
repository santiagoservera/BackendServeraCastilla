package com.example.ordenes.repositories;

import com.example.ordenes.model.Ordenes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenesRepository extends JpaRepository<Ordenes, Long> {

}
