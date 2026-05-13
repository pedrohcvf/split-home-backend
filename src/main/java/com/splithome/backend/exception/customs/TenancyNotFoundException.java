package com.splithome.backend.exception.customs;

public class TenancyNotFoundException extends RuntimeException {
    public TenancyNotFoundException() {
        super("Locação não encontrada");
    }
}
