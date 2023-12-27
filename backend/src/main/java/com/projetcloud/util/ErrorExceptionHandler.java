package com.projetcloud.util;

import com.projetcloud.dto.response.ErrorDTO;
import com.projetcloud.exceptions.BadLoginException;
import com.projetcloud.exceptions.LoginAlreadyUsedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice(basePackages = "com.projetcloud.controller")
public class ErrorExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(LoginAlreadyUsedException.class)
    public ResponseEntity<Object> handleLoginAlreadyUsedException(Exception ex, WebRequest request) {
        ErrorDTO errorDTO = createDTO("Login is already used", HttpStatus.CONFLICT, ex, request);
        return handleExceptionInternal(ex, errorDTO, new HttpHeaders(), HttpStatus.valueOf(errorDTO.getStatus()), request);
    }

    @ExceptionHandler(BadLoginException.class)
    public ResponseEntity<Object> handleBadLoginException(Exception ex, WebRequest request) {
        ErrorDTO errorDTO = createDTO("Incorrect login", HttpStatus.UNAUTHORIZED, ex, request);
        return handleExceptionInternal(ex, errorDTO, new HttpHeaders(), HttpStatus.valueOf(errorDTO.getStatus()), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        System.out.println(ex.getStackTrace()[0].getFileName());
        System.out.println(ex.getStackTrace()[0].getLineNumber());
        System.out.println(ex.getClass());
        ErrorDTO errorDTO = createDTO("Une erreur est survenue.", HttpStatus.BAD_REQUEST, ex, request);
        return handleExceptionInternal(ex, errorDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    public ErrorDTO createDTO(String customMessage, HttpStatus status, Exception ex, WebRequest request) {
        ErrorDTO errorDTO;
        if (Objects.isNull(ex.getCause()) || Objects.isNull(ex.getCause().getMessage())) {
            errorDTO = new ErrorDTO(customMessage, status, ((ServletWebRequest) request).getRequest().getRequestURI());
        } else {
            errorDTO = new ErrorDTO(ex.getCause().getMessage(), status, ((ServletWebRequest) request).getRequest().getRequestURI());
        }
        return errorDTO;
    }
}
