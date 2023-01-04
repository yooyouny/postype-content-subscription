package com.postype.sns.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorCode {
	DUPLICATED_MEMBER_NAME(HttpStatus.CONFLICT, "Member name is duplicated"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error"),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Member not founded"),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid")
	;

	private final HttpStatus status;
	private final String message;

}
