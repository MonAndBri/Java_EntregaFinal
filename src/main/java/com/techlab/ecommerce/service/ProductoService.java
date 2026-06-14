package com.techlab.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.techlab.ecommerce.exception.PrecioInvalidoException;
import com.techlab.ecommerce.exception.ProductoNoEncontradoException;
import com.techlab.ecommerce.exception.StockInsuficienteException;
import com.techlab.ecommerce.model.Producto;
import com.techlab.ecommerce.repository.ProductoRepository;

// Capa de servicio: lógica de negocio.
// Sin Scanner ni System.out — no interactúa con el usuario directamente.
@Service
public class ProductoService {

    // Inyección por constructor: Spring pasa el repositorio.
    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public Producto guardar(Producto p) {
        // Validaciones con if — se reemplazan por anotaciones en el Bloque D.
        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (p.getPrecio() <= 0) {
            throw new PrecioInvalidoException("El precio debe ser mayor a cero. Se recibió: " + p.getPrecio());
        }
        if (p.getStock() < 0) {
            throw new StockInsuficienteException("El stock no puede ser negativo. Se recibió: " + p.getStock());
        }
        // save: si el id es 0 inserta; si ya existe, actualiza. Devuelve el objeto con id asignado.
        return repository.save(p);
    }

    // READ: lista completa.
    public List<Producto> listarTodos() {
        return repository.findAll();
    }

    // READ: por id. findById devuelve Optional — si está vacío, lanzamos la excepción.
    public Producto obtenerPorId(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("No se encontró un producto con id " + id));
    }

    // UPDATE
    public Producto actualizar(int id, Producto datos) {
        if (datos.getNombre() == null || datos.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (datos.getPrecio() <= 0) {
            throw new PrecioInvalidoException("El precio debe ser mayor a cero. Se recibió: " + datos.getPrecio());
        }
        if (datos.getStock() < 0) {
            throw new StockInsuficienteException("El stock no puede ser negativo. Se recibió: " + datos.getStock());
        }
        // Reutiliza obtenerPorId: si no existe, corta acá con la excepción.
        Producto p = obtenerPorId(id);
        p.setNombre(datos.getNombre());
        p.setPrecio(datos.getPrecio());
        p.setStock(datos.getStock());
        p.setCategoria(datos.getCategoria());
        // save sobre un objeto con id existente: actualiza la fila.
        return repository.save(p);
    }

    // DELETE
    public void eliminar(int id) {
        // Verifica existencia primero para devolver 404 coherente.
        Producto p = obtenerPorId(id);
        repository.delete(p);
    }
}
