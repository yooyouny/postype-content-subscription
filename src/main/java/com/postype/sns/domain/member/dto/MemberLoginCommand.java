package com.postype.sns.domain.member.dto;

public record MemberLoginCommand(
	String memberId,
	String password
) {

}
