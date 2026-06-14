package com.techlab.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

// Cada Producto pertenece a una Categoría.
@Entity // Esto le dice a JPA que esta clase se mapea a una tabla de la base de datos.
@Table(name = "categorias") // Opcional: especifica el nombre de la tabla
public class Categoria {

    @Id // Esto le dice a JPA que este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática de IDs
    private int id;

    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    @Column(name = "nombre", nullable = false, length = 50, unique = true)
    private String nombre;
    
    @NotBlank(message = "La descripción de la categoría no puede estar vacía")
    @Column(name = "descripcion", length = 200)
    private String descripcion;

    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Constructor vacío: lo necesitan Spring y JPA.
    public Categoria() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return "ID: " + id + " | " + nombre + " | " + descripcion;
    }
}