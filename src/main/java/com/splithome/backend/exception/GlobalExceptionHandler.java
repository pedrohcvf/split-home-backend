package com.splithome.backend.exception;

import com.splithome.backend.exception.customs.*;
import com.splithome.backend.exception.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // VALIDAÇÃO DOS CAMPOS REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handlerValidationException(MethodArgumentNotValidException exception){
        String message = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(400, message, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    // VALIDAÇÃO DO EMAIL
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailAlreadyExists(EmailAlreadyExistsException exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(409, exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    // VALIDAÇÃO DO USUÁRIO
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(404, exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    // VALIDAÇÃO DAS CREDENCIAIS
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCredentialsException(InvalidCredentialsException exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(401, exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
    }

    @ExceptionHandler(PropertyAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handlePropertyAlreadyExistsException(PropertyAlreadyExistsException exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(409, exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    // VALIDAÇÃO DO IMÓVEL
    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePropertyNotFoundException(PropertyNotFoundException exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(404, exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    // VALIDAÇÃO DO PROPRIETARIO PARA DELETAR IMÓVEL
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied(AccessDeniedException exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(403, exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponseDto);
    }

    // VALIDAÇÃO GENÉRICA
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(500, "Erro interno do servidor",LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }

}
