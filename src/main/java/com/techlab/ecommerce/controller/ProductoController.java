package com.techlab.ecommerce.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlab.ecommerce.model.Producto;
import com.techlab.ecommerce.service.ProductoService;

@RestController // le dice a Spring que esta clase va a manejar requests HTTP y que las respuestas se van a serializar automáticamente a JSON. Sin esta anotación, Spring no sabe que esta clase es un controlador
@RequestMapping("/productos") // define la URL base. Todo endpoint de esta clase va a empezar con /productos
public class ProductoController {

    private final ProductoService service; // Inyección por constructor - ProductoService no se crea con new. Spring lo crea automáticamente y se lo pasa al controlador

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping // mapea el método listarTodos() a una request HTTP GET. Cuando alguien haga GET /productos, Spring ejecuta ese método y devuelve la lista de productos en formato JSON. Sin esta anotación, Spring no sabe qué método ejecutar para cada request
    public List<Producto> listarTodos() {
        return service.listarTodos();
    }
}
