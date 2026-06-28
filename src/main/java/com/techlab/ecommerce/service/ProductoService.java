package com.techlab.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.techlab.ecommerce.exception.ProductoNoEncontradoException;
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
        // save: si el id es 0 inserta; si ya existe, actualiza. Devuelve el objeto con id asignado.
        return repository.save(p);
    }

    // READ: lista completa.
    public List<Producto> listarTodos() {
        return repository.findAll();
    }

    // READ: por id. findById devuelve Optional — si está vacío, lanzamos la excepción.
    public Producto obtenerPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("No se encontró un producto con id " + id));
    }

    // UPDATE
    public Producto actualizar(Integer id, Producto datos) {
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
    public void eliminar(Integer id) {
        // Verifica existencia primero para devolver 404 coherente.
        Producto p = obtenerPorId(id);
        repository.delete(p);
    }

    // Método de consulta personalizados: busca productos por su nombre.
    public List<Producto> buscarPorNombre(String nombre) {
        return repository.findByNombreContaining(nombre);
    }
    
    // Método de consulta personalizado: busca productos por el nombre de su categoría.
    public List<Producto> buscarPorCategoria(String categoria) {
        return repository.findByCategoriaNombreContainingIgnoreCase(categoria);
    }
}
