package com.techlab.ecommerce.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.techlab.ecommerce.model.Producto;
import com.techlab.ecommerce.service.ProductoService;

import jakarta.validation.Valid;

@RestController // le dice a Spring que esta clase va a manejar requests HTTP y que las
                // respuestas se van a serializar automáticamente a JSON. Sin esta anotación,
                // Spring no sabe que esta clase es un controlador
@RequestMapping("/productos") // define la URL base. Todo endpoint de esta clase va a empezar con /productos
@CrossOrigin(origins = "http://localhost:5500") // (ajustar el puerto si es necesario)
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
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos()); // devuelve la lista de productos con un status HTTP 200 OK. Spring convierte la lista de objetos Producto a JSON automáticamente
    }

    // Endpoint para obtener un producto por su ID
    // @GetMapping("/{id}") mapea a GET /productos/{id}. El {id} es un placeholder 
    // que se reemplaza por el valor real en la URL. Por ejemplo, GET /productos/1 
    // ejecuta este método con id=1 y devuelve el producto con ID 1 en formato JSON.
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable int id) {
        return ResponseEntity.ok(service.obtenerPorId(id)); // devuelve el producto con el ID especificado con un status HTTP 200 OK. Si no existe, el servicio lanza una excepción que se maneja en GlobalExceptionHandler para devolver un 404 Not Found.
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
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(producto));
    }
    // @RequestBody funcionando – Spring tomó el JSON del cuerpo de la request y lo convirtió
    // automáticamente a un objeto Producto de Java. 
    // Enviamos un JSON y el servicio recibió un objeto Java.

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable int id, @Valid @RequestBody Producto producto) {
        return ResponseEntity.ok(service.actualizar(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // GET /productos/nombre/{nombre}
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Producto>> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(service.buscarPorNombre(nombre));
    }

    // GET /productos/categoria/{categoria}
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(service.buscarPorCategoria(categoria));
    }

}