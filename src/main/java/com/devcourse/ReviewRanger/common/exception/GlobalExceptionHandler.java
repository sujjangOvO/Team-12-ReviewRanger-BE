package com.devcourse.ReviewRanger.common.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final String VALID_LOG_FORMAT = "Class : {}, Error : {}, ErrorMessage : {}";
	private static final String LOG_FORMAT = "Class : {}, ErrorMessage : {}";

	public record ErrorResponse(
		String errorCode,
		String message
	) {
	}

	@ExceptionHandler(RangerException.class)
	public ResponseEntity<ErrorResponse> handleRangerException(RangerException e) {
		ErrorCode errorCode = e.getErrorCode();
		log.warn(LOG_FORMAT, e.getClass().getSimpleName(), errorCode);

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(new ErrorResponse(errorCode.name(), errorCode.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleRangerException(MethodArgumentNotValidException e) {
		String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		log.warn(VALID_LOG_FORMAT, e.getClass().getSimpleName(), "@Valid", errorMessage);

		return ResponseEntity.status(BAD_REQUEST)
			.body(new ErrorResponse(BAD_REQUEST.name(), errorMessage));
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ErrorResponse> handleRangerException(DataAccessException e) {
		String errorMessage = e.getMessage();
		log.warn(LOG_FORMAT, e.getClass().getSimpleName(), errorMessage);

		return ResponseEntity.status(BAD_REQUEST)
			.body(new ErrorResponse(BAD_REQUEST.name(), errorMessage));
	}

	@ExceptionHandler(JsonProcessingException.class)
	public ResponseEntity<ErrorResponse> handleRangerException(JsonProcessingException e) {
		String errorMessage = e.getMessage();
		log.warn(LOG_FORMAT, e.getClass().getSimpleName(), errorMessage);

		return ResponseEntity.status(BAD_REQUEST)
			.body(new ErrorResponse(BAD_REQUEST.name(), errorMessage));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
		log.warn(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage());

		return ResponseEntity.status(INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(INTERNAL_SERVER_ERROR.name(), e.getMessage()));
	}
}
