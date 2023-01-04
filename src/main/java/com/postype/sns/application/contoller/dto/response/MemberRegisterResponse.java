package com.postype.sns.application.contoller.dto.response;

import com.postype.sns.domain.member.model.MemberRequestDto;
import com.postype.sns.domain.member.model.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberRegisterResponse {
	private Long id;
	private String memberId;
	private MemberRole role;

	public static MemberRegisterResponse fromMember(MemberRequestDto member){
		return new MemberRegisterResponse(
			member.getId(),
			member.getMemberId(),
			member.getRole()
		);
	}
}
