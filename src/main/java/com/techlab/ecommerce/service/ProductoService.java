package com.techlab.ecommerce.service;

import com.techlab.ecommerce.exception.ProductoNoEncontradoException;
import com.techlab.ecommerce.exception.StockInsuficienteException;
import com.techlab.ecommerce.exception.PrecioInvalidoException;
import com.techlab.ecommerce.model.Producto;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Capa de servicio: contiene la lógica de negocio del sistema.
 *
 * Es responsable de:
 * - Mantener la colección de productos.
 * - Asignar el id al guardar un nuevo producto.
 * - Validar los datos antes de guardar o actualizar.
 * - Buscar, modificar y eliminar productos por id.
 *
 * No tiene Scanner ni System.out: no interactúa con el usuario.
 * Quien quiera mostrar mensajes o leer datos lo hace por afuera
 * (en este proyecto, lo hace la clase Main). Esa separación es
 * la que va a permitir, en clases siguientes, reemplazar el menú
 * por una API REST sin tocar este archivo.
 */
@Service
public class ProductoService {

    // Colección en memoria que guarda los productos.
    // En la pre-entrega esta lista vive solo durante la ejecución
    // del programa: al cerrar la aplicación se pierde todo. Más
    // adelante, esta lista se reemplazará por una base de datos.
    private List<Producto> productos = new ArrayList<>();

    // Contador para asignar ids únicos. Es 'static' porque pertenece
    // a la clase y no a una instancia particular: garantiza que el
    // id sea único aunque hubiera varias instancias del servicio.
    private static int contadorId = 1;

    // ----------------------------------------------------------------
    // Operaciones del CRUD
    // ----------------------------------------------------------------

    // CREATE: agrega un nuevo producto al catálogo.
    public Producto guardar(Producto p) {

        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }

        if (p.getPrecio() <= 0) {
            throw new PrecioInvalidoException("El precio debe ser mayor a cero. Se recibió: " + p.getPrecio());
        }

        if (p.getStock() < 0) {
            throw new StockInsuficienteException("El stock no puede ser negativo. Se recibió: " + p.getStock());
        }

        // El id lo asigna el servicio, no el usuario. Después de
        // asignarlo, incrementamos el contador para el próximo.
        p.setId(contadorId);
        contadorId++;

        productos.add(p);
        return p;
    }

    // READ: devuelve toda la lista de productos.
    // Declaramos el retorno como List (interfaz) y no como
    // ArrayList (clase concreta). Así el código depende del
    // "qué se puede hacer" y no del "cómo está implementado".
    // Si mañana cambiamos la implementación interna, quien use
    // este método sigue funcionando sin cambios.
    public List<Producto> listarTodos() {
        return productos;
    }

    // READ: busca un producto por id. Si no existe, lanza excepción.
    // No devolvemos null porque obligaría a quien llama a chequear
    // por null en cada uso, y olvidarse de hacerlo provoca el famoso
    // NullPointerException. Una excepción es más explícita.
    public Producto obtenerPorId(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new ProductoNoEncontradoException("No se encontró un producto con id " + id);
    }

    // UPDATE: actualiza los datos de un producto existente.
    // Recibe el id del producto a modificar y un objeto Producto
    // con los nuevos datos. Solo actualiza los campos editables;
    // el id no se modifica nunca.
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

        // Reutilizamos obtenerPorId: si no existe, lanza excepción
        // y la actualización se cancela automáticamente.
        Producto p = obtenerPorId(id);

        // Modificamos el producto encontrado. Como Java pasa los
        // objetos por referencia, los cambios se reflejan en la
        // lista sin necesidad de hacer nada más.
        p.setNombre(datos.getNombre());
        p.setPrecio(datos.getPrecio());
        p.setStock(datos.getStock());
        p.setCategoria(datos.getCategoria());

        return p;
    }

    // DELETE: elimina un producto por id.
    public void eliminar(int id) {
        // Verificamos que exista antes de eliminar. Si no existe,
        // obtenerPorId lanza la excepción y el método termina.
        Producto p = obtenerPorId(id);
        productos.remove(p);
    }
}
