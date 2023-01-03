package com.postype.sns.application.contoller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberRegisterRequest{

	private String memberId;
	private String password;
}
