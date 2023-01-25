package com.postype.sns.application.contoller.dto.response;

import com.postype.sns.application.contoller.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {

	private Long id;
	private String userName;

	public static MemberResponse fromMemberDto(MemberDto member) {
		return new MemberResponse(
				member.getId(),
				member.getMemberId()
		);
	}

}

