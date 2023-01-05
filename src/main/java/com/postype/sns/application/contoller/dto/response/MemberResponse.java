package com.postype.sns.application.contoller.dto.response;

import com.postype.sns.domain.member.model.MemberRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {

	private Long id;
	private String userName;

	public static MemberResponse fromMember(MemberRequestDto member) {
		return new MemberResponse(
				member.getId(),
				member.getMemberId()
		);
	}

}

