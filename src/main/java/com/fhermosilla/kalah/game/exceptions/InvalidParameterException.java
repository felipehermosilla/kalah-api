package com.fhermosilla.kalah.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
*
* @author felipehermosilla
* Class for handling http 400 Bad Request exceptions
*/
@ResponseStatus (value = HttpStatus.BAD_REQUEST)
public class InvalidParameterException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidParameterException(String message){
        super(message);
    }
}