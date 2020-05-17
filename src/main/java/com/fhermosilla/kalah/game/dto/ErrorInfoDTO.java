package com.fhermosilla.kalah.game.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author felipehermosilla
 * Generic ErrorDTO
 */
@Data
@AllArgsConstructor
public class ErrorInfoDTO implements Serializable {

	private HttpStatus httpStatus;
	private LocalDateTime errorTime;
	private String errorMessage;
	private String detail;

	public ErrorInfoDTO(HttpStatus httpStatus, LocalDateTime errorTime, String errorMessage, String detail) {
		super();
		this.httpStatus = httpStatus;
		this.errorTime = errorTime;
		this.errorMessage = errorMessage;
		this.detail = detail;
	}

	
}
