package com.techlab.ecommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlab.ecommerce.exception.CategoriaNoEncontradaException;
import com.techlab.ecommerce.model.Categoria;
import com.techlab.ecommerce.service.CategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoria(@PathVariable int id) {
        try {
            return ResponseEntity.ok(service.obtenerPorId(id));
        } catch (CategoriaNoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@Valid @RequestBody Categoria nuevaCategoria) {
        try {
            Categoria creada = service.guardar(nuevaCategoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable int id, @Valid @RequestBody Categoria datos) {
        try {
            return ResponseEntity.ok(service.actualizar(id, datos));
        } catch (CategoriaNoEncontradaException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        try {
            service.eliminar(id);
            return ResponseEntity.ok().build();
        } catch (CategoriaNoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
