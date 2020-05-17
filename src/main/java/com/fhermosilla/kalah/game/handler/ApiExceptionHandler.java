package com.fhermosilla.kalah.game.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.fhermosilla.kalah.game.dto.ErrorInfoDTO;
import com.fhermosilla.kalah.game.exceptions.ForbiddenException;
import com.fhermosilla.kalah.game.exceptions.InvalidParameterException;
import com.fhermosilla.kalah.game.exceptions.ResourceNotFoundException;

/**
 *
 * @author felipehermosilla
 * Generic Error Handler for the API
 */

@RestControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorInfoDTO error = new ErrorInfoDTO(HttpStatus.BAD_REQUEST, LocalDateTime.now(),request.getDescription(false), ex.getBindingResult().toString());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

	@ExceptionHandler(InvalidParameterException.class)
    protected ResponseEntity<?> invalidParameterException(InvalidParameterException ex, WebRequest request) {
        ErrorInfoDTO error = new ErrorInfoDTO(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

	@ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<?> forbiddenException(ForbiddenException ex, WebRequest request) {
        ErrorInfoDTO error = new ErrorInfoDTO(HttpStatus.FORBIDDEN, LocalDateTime.now(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorInfoDTO ErrorInfoDTO = new ErrorInfoDTO(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(ErrorInfoDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExcpetionHandler(Exception ex, WebRequest request) {
        ErrorInfoDTO ErrorInfoDTO = new ErrorInfoDTO(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(ErrorInfoDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
