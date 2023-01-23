package com.postype.sns.application.exception;

import com.postype.sns.application.contoller.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<?> applicationHandler(ApplicationException e){
		log.error("Error occurs {}", e.toString());
		return ResponseEntity.status(e.getErrorCode().getStatus())
			.body(Response.error(e.getErrorCode().name()));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity applicationHandler(DataIntegrityViolationException e){
		log.error("Error occurs {}", e.toString());
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(Response.error(ErrorCode.DUPLICATED_KEY.name()));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> applicationHandler(RuntimeException e){
		log.error("Error occurs {}", e.toString());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name()));
	}

}
