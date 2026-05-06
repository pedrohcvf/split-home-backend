package com.splithome.backend.exception.customs;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String email){
        super("Email ja cadastrado: " + email);
    }
}
