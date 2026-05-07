package com.splithome.backend.exception.customs;

import java.util.UUID;

public class PropertyNotFoundException extends RuntimeException {
    public PropertyNotFoundException(UUID id) {
        super("Imóvel com ID: " + id + " não encontrado");
    }
}
