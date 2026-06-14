package com.techlab.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techlab.ecommerce.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}