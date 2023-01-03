package com.postype.sns.application.contoller.dto.response;

import com.postype.sns.domain.member.model.MemberRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberRegisterResponse {
	private Long id;
	private String memberId;

	public static MemberRegisterResponse fromMember(MemberRequestDto member){
		return new MemberRegisterResponse(
			member.getId(),
			member.getMemberId()
		);
	}
}
