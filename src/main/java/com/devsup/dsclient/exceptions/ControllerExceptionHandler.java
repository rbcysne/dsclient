package com.devsup.dsclient.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(RegisterNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(RegisterNotFoundException e, HttpServletRequest request) {
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		StandardError stdErr = new StandardError();
		
		stdErr.setTimestamp(Instant.now());
		stdErr.setStatus(status.value());
		stdErr.setError("Register not found");
		stdErr.setMessage(e.getMessage());
		
		return ResponseEntity.status(status).body(stdErr);
	}
}
