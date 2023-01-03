package com.postype.sns.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorCode {
	DUPLICATED_MEMBER_NAME(HttpStatus.CONFLICT, "Member name is duplicated"),
	;

	private HttpStatus status;
	private String message;

}
