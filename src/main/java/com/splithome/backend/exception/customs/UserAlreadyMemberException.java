package com.splithome.backend.exception.customs;

public class UserAlreadyMemberException extends RuntimeException {
    public UserAlreadyMemberException(String user) {
        super("O usuário " + user + " já é membro dessa locação.");
    }
}
