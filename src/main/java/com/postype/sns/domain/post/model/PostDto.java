package com.postype.sns.domain.post.model;

import com.postype.sns.domain.member.model.MemberDto;
import com.postype.sns.domain.order.model.Order;
import com.postype.sns.domain.order.model.Point;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostDto {
	private Long id;
	private String title;
	private String body;
	private MemberDto member;
	private Point price;
	private Timestamp registeredAt;
	private Timestamp updatedAt;
	private Timestamp deletedAt;

	public static PostDto fromPost(Post post){
		return new PostDto(
			post.getId(),
			post.getTitle(),
			post.getBody(),
			MemberDto.fromEntity(post.getMember()),
			post.getPrice(),
			post.getRegisteredAt(),
			post.getUpdatedAt(),
			post.getDeletedAt()
		);
	}
}
