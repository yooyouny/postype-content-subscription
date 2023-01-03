package com.postype.sns.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberException extends RuntimeException{

	private ErrorCode errorCode;
	private String message;

	@Override
	public String getMessage() {
		if(message == null){
			return errorCode.getMessage();
		}
		return String.format("%s. %s", errorCode.getMessage(), message);
	}
}
