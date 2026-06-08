package com.techlab.ecommerce.model;

// Cada Producto pertenece a una Categoría.
public class Categoria {

    private int id;
    private String nombre;
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