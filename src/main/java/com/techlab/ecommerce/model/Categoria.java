package com.techlab.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

// Cada Producto pertenece a una Categoría.
@Entity // Esto le dice a JPA que esta clase se mapea a una tabla de la base de datos.
@Table(name = "categorias") // Opcional: especifica el nombre de la tabla
@Data   // Lombok: Genera automáticamente Getters, Setters, ToString, Equals y HashCode
@NoArgsConstructor // Lombok: Genera un constructor vacío 
@AllArgsConstructor // Lombok: Genera un constructor con todos los campos
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

    // CONSTRUCTOR PERSONALIZADO (Mantenelo para crear categorías sin ID)
    // El ID lo asigna el CategoriaService al momento de guardar la categoría. 
    // El usuario nunca elige el ID, se genera automáticamente en la base de datos.
    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

}