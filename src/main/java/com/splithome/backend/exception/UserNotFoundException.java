package com.splithome.backend.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("Usuário não encontrado: " + email);
    }
}
