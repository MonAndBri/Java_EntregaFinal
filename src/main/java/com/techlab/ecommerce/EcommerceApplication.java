package com.techlab.ecommerce;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.techlab.ecommerce.model.Producto;
import com.techlab.ecommerce.service.ProductoService;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    public CommandLineRunner cargarDatos(ProductoService service) {
        return args -> {
            service.guardar(new Producto("Café molido 500g", 4500, 30, "Bebidas"));
            service.guardar(new Producto("Yerba mate 1kg", 3200, 50, "Bebidas"));
            service.guardar(new Producto("Galletitas dulces", 1850, 100, "Almacén"));
        };
    }
}