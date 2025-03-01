package com.ecoapi.goodshopping.exceptions;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String s) {
        super(s); // Ensure the message is passed to the parent class, if this is not called, then the getMessage() method will return null
    }
}
