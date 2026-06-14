package com.techlab.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techlab.ecommerce.model.Producto;

// Interfaz vacía: hereda save, findById, findAll, deleteById y más de JpaRepository.
// Spring genera la implementación en tiempo de ejecución. No escribimos SQL.
// <Producto, Integer>: entidad que maneja y tipo de su clave primaria.
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Método de consulta personalizado: busca productos cuyo nombre contenga el texto dado.
    List<Producto> findByNombreContaining(String nombre);

    // Método de consulta personalizado: busca productos por el nombre de su categoría.
    @Query("SELECT p FROM Producto p WHERE p.categoria.nombre = :nombreCategoria")
    List<Producto> buscarPorCategoria(@Param("nombreCategoria") String nombreCategoria);
}
