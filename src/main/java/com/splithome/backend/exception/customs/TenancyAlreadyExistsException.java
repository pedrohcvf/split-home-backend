package com.splithome.backend.exception.customs;

public class TenancyAlreadyExistsException extends RuntimeException {
    public TenancyAlreadyExistsException(String address) {
        super("Já existe uma locação ativa no endereço: " + address);
    }
}
