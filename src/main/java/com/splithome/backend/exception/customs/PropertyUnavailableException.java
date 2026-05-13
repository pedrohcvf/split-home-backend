package com.splithome.backend.exception.customs;

public class PropertyUnavailableException extends RuntimeException {
    public PropertyUnavailableException(String property) {
        super("O imóvel " + property + " não está disponível para locação, por favor alterar disponibilidade.");
    }
}
