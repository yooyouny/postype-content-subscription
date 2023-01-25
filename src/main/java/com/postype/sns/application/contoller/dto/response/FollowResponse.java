package com.postype.sns.application.contoller.dto.response;

import com.postype.sns.application.contoller.dto.FollowDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowResponse {
	private Long id;
	private Long fromMemberId;
	private Long toMemberId;

	public static FollowResponse fromFollowDto(FollowDto follow){
		return new FollowResponse(
			follow.getId(),
			follow.getFromMemberId(),
			follow.getToMemberId()
		);
	}

}
