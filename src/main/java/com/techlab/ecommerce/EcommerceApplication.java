package com.techlab.ecommerce;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.techlab.ecommerce.model.Categoria;
import com.techlab.ecommerce.model.Producto;
import com.techlab.ecommerce.service.CategoriaService;
import com.techlab.ecommerce.service.ProductoService;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    public CommandLineRunner cargarDatos(ProductoService productoService, CategoriaService categoriaService) {
        return args -> {
            if (categoriaService.listarTodas().isEmpty()) {

                // 1. Primero creamos los moldes de las categorías que necesitamos
                Categoria almacen = categoriaService.guardar(new Categoria("Almacén", "Productos de almacén"));
				Categoria bebidas = categoriaService.guardar(new Categoria("Bebidas", "Bebidas y líquidos"));

                // 2. Ahora sí creamos los productos pasándole el OBJETO categoría completo al final
                productoService.guardar(new Producto("Café molido 500g", 4500.0, 30, bebidas));
                productoService.guardar(new Producto("Yerba mate 1kg", 3200.0, 50, bebidas));
                productoService.guardar(new Producto("Galletitas dulces", 1850.0, 100, almacen));
            }
        };
    }
}