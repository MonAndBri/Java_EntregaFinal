package com.techlab.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Modelo de dominio: representa un producto del catálogo.
 *
 * Aplica encapsulamiento: los atributos son privados y se accede
 * a ellos a través de getters y setters. Esta clase no sabe nada
 * sobre cómo se almacenan los productos ni cómo se muestran al
 * usuario; su única responsabilidad es representar un producto.
 */
@Entity
@Table(name = "productos")
public class Producto {

    // Atributos privados: nadie de afuera puede modificarlos
    // directamente. Para acceder o modificarlos se usan los métodos
    // getters y setters definidos más abajo.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Positive(message = "El precio debe ser un número positivo")
    @Column(name = "precio", nullable = false)
    private double precio;
    
    @PositiveOrZero(message = "El stock debe ser un número no negativo")
    @Column(name = "stock", nullable = false)
    private int stock;
    
    @ManyToOne // Muchos productos pertenecen a una sola categoría
    @JoinColumn(name = "categoria_id", nullable = false) // Nombra la columna FK en MySQL
    private Categoria categoria; // <-- Cambió de "String" a "Categoria"

    // Constructor sin id: el id lo asigna el ProductoService al
    // momento de guardar el producto. El usuario nunca elige el id.
    public Producto(String nombre, double precio, int stock, Categoria categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    // Constructor vacío: útil para crear un Producto y completarlo
    // con setters después. También lo necesitará Spring/JPA más adelante en el
    // curso.
    public Producto() {
    }

    // Getters y setters: la única forma de acceder o modificar
    // los atributos privados desde afuera de la clase.
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | " + nombre +
                " | $" + precio +
                " | Stock: " + stock +
                " | Categoría: " + categoria.getNombre();
    }
}
