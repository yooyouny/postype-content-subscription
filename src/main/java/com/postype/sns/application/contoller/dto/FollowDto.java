package com.postype.sns.application.contoller.dto;

import com.postype.sns.domain.member.model.entity.Follow;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowDto {
	private Long id;
	private Long fromMemberId;
	private Long toMemberId;
	private Timestamp registeredAt;
	private Timestamp updatedAt;
	private Timestamp deletedAt;

	public static FollowDto fromEntity(Follow follow){
		return new FollowDto(
			follow.getId(),
			follow.getFromMemberId(),
			follow.getToMemberId(),
			follow.getRegisteredAt(),
			follow.getUpdatedAt(),
			follow.getDeletedAt()
		);
	}
}
