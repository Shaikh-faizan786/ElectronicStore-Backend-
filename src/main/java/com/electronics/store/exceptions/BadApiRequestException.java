package com.electronics.store.exceptions;

public class BadApiRequestException extends  RuntimeException{

    public BadApiRequestException(String message) {
        super("Bad Request !!");
    }

    public BadApiRequestException() {

    }
}
