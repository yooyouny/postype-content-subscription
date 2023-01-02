package com.postype.sns.domain.member.dto;

public record MemberRegisterCommand(
	String memberId,
	String password
) {

}
