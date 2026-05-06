package com.splithome.backend.exception.customs;


public class PropertyAlreadyExistsException extends RuntimeException {
    public PropertyAlreadyExistsException(String address) {
        super("Endereço " + address + " já cadastrado");
    }
}
