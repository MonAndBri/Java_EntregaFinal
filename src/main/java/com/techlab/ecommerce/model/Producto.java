package com.techlab.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

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
@Getter                  // Lombok genera todos los Getters automáticamente
@Setter                  // Lombok genera todos los Setters automáticamente
@NoArgsConstructor       // Lombok genera el constructor vacío invisible
@AllArgsConstructor      // Lombok genera el constructor con todos los campos
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

    // Dejo la sobreescritura de toString para evitar el error de StackOverflow al imprimir un producto, 
    // ya que la categoría también tiene un toString que a su vez imprime sus productos, 
    // generando un ciclo infinito.
    @Override
    public String toString() {
        return "ID: " + id +
                " | " + nombre +
                " | $" + precio +
                " | Stock: " + stock +
                " | Categoría: " + categoria.getNombre();
    }
}
