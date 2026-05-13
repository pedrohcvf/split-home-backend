package com.splithome.backend.exception.customs;

public class UserNotMemberException extends RuntimeException {
    public UserNotMemberException(String email) {
        super("O usuário " + email + " não pertence a esta locação");
    }
}
