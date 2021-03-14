package br.com.compasso.uol.exception;

public class ProductNotFoundException extends  RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}
