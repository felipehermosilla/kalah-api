package com.fhermosilla.kalah.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author felipehermosilla
 * Class for handling http 403 Forbidden exceptions
 */
@ResponseStatus (value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends Exception {
    private static final long serialVersionUID = 1L;

    public ForbiddenException(String message){
        super(message);
    }
}
