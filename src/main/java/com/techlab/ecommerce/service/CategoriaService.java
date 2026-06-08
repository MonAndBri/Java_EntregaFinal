package com.techlab.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import com.techlab.ecommerce.exception.CategoriaNoEncontradaException;
import com.techlab.ecommerce.exception.CategoriaNombreInvalidoException;
import com.techlab.ecommerce.model.Categoria;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private List<Categoria> categorias = new ArrayList<>();
    private static int contadorId = 1;

    public Categoria guardar(Categoria c) {
        if (c.getNombre() == null || c.getNombre().isBlank()) {
            throw new CategoriaNombreInvalidoException("El nombre de la categoría no puede estar vacío.");
        }
        c.setId(contadorId);
        contadorId++;
        categorias.add(c);
        return c;
    }

    public List<Categoria> listarTodas() {
        return categorias;
    }

    public Categoria obtenerPorId(int id) {
        for (Categoria c : categorias) {
            if (c.getId() == id) {
                return c;
            }
        }
        throw new CategoriaNoEncontradaException("No se encontró una categoría con id " + id);
    }

    public Categoria actualizar(int id, Categoria datos) {
        if (datos.getNombre() == null || datos.getNombre().isBlank()) {
            throw new CategoriaNombreInvalidoException("El nombre de la categoría no puede estar vacío.");
        }
        Categoria c = obtenerPorId(id);
        c.setNombre(datos.getNombre());
        c.setDescripcion(datos.getDescripcion());
        return c;
    }

    public void eliminar(int id) {
        Categoria c = obtenerPorId(id);
        categorias.remove(c);
    }
}