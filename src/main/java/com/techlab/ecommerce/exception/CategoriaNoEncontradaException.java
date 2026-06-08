package com.techlab.ecommerce.exception;

public class CategoriaNoEncontradaException extends RuntimeException {
    public CategoriaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
