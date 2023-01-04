package com.postype.sns.application.exception;

import com.postype.sns.domain.member.model.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberException extends RuntimeException{

	private ErrorCode errorCode;
	private String message;

	public MemberException(ErrorCode errorCode){
		this.errorCode = errorCode;
		this.message = null;
	}

	@Override
	public String getMessage() {
		if(message == null){
			return errorCode.getMessage();
		}
		return String.format("%s. %s", errorCode.getMessage(), message);
	}
}
