package com.splithome.backend.exception.customs;

public class TenancyNotFoundException extends RuntimeException {
    public TenancyNotFoundException(String inviteCode) {
        super("Locação na propriedade " + inviteCode + " não encontrada.");
    }
}
