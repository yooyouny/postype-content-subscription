package com.postype.sns.domain.post.model;

import com.postype.sns.domain.member.model.MemberRequestDto;
import com.postype.sns.domain.member.model.entity.Member;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostRequestDto {
	private Long id;
	private String title;
	private String body;
	private MemberRequestDto member;
	private Timestamp registeredAt;
	private Timestamp updatedAt;
	private Timestamp deletedAt;

	public static PostRequestDto fromPost(Post post){
		return new PostRequestDto(
			post.getId(),
			post.getTitle(),
			post.getBody(),
			MemberRequestDto.fromMember(post.getMember()),
			post.getRegisteredAt(),
			post.getUpdatedAt(),
			post.getDeletedAt()
		);
	}
}
