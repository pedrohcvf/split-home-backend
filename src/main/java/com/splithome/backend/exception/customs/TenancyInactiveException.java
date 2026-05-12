package com.splithome.backend.exception.customs;

public class TenancyInactiveException extends RuntimeException {
    public TenancyInactiveException(String tenancy) {
        super("A locação " + tenancy + " não está mais ativa.");
    }
}
