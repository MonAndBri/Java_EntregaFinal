package com.techlab.ecommerce.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.techlab.ecommerce.model.Producto;
import com.techlab.ecommerce.service.ProductoService;



@RestController // le dice a Spring que esta clase va a manejar requests HTTP y que las
                // respuestas se van a serializar automáticamente a JSON. Sin esta anotación,
                // Spring no sabe que esta clase es un controlador
@RequestMapping("/productos") // define la URL base. Todo endpoint de esta clase va a empezar con /productos
public class ProductoController {

    private final ProductoService service; // Inyección por constructor - ProductoService no se crea con new. Spring lo
                                           // crea automáticamente y se lo pasa al controlador

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping // mapea el método listarTodos() a una request HTTP GET. Cuando alguien haga GET
                // /productos, Spring ejecuta ese método y devuelve la lista de productos en
                // formato JSON. Sin esta anotación, Spring no sabe qué método ejecutar para
                // cada request
    public List<Producto> listarTodos() {
        return service.listarTodos();
    }

    // Endpoint para obtener un producto por su ID
    // @GetMapping("/{id}") mapea a GET /productos/{id}. El {id} es un placeholder 
    // que se reemplaza por el valor real en la URL. Por ejemplo, GET /productos/1 
    // ejecuta este método con id=1 y devuelve el producto con ID 1 en formato JSON.
    @GetMapping("/{id}")
    public Producto obtenerPorId(@PathVariable int id) {
        return service.obtenerPorId(id);
    }
    // @PathVariable funcionando – Spring tomó el 1 de la URL y lo pasó directamente
    // al método como parámetro id.
    // JSON funcionando – el objeto Producto de Java se convirtió automáticamente a
    // JSON. Cada atributo privado de la clase se mapeó a una clave del JSON gracias
    // a los getters.

    @PostMapping // mapea a POST /productos. Este endpoint se usa para crear un nuevo producto. El
                 // cliente envía un JSON con los datos del producto en el cuerpo de la
                 // request, y Spring lo convierte automáticamente a un objeto Producto de
                 // Java gracias a la anotación @RequestBody.
    public Producto crearProducto(@RequestBody Producto producto) {
        return service.guardar(producto);
    }
    // @RequestBody funcionando – Spring tomó el JSON del cuerpo de la request y lo convirtió
    // automáticamente a un objeto Producto de Java. 
    // Enviamos un JSON y el servicio recibió un objeto Java.

    @PutMapping("/{id}")
    public Producto actualizarProducto(@PathVariable int id, @RequestBody Producto producto) {
        return service.actualizar(id, producto);
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable int id) {
        service.eliminar(id);
    }

}