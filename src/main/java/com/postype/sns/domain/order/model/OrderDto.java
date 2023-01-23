package com.postype.sns.domain.order.model;


import com.postype.sns.domain.member.model.MemberDto;
import com.postype.sns.domain.member.model.entity.Member;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderDto {
	private Long id;
	private String memberId;
	private Long postId;
	private int value;
	private Timestamp orderAt;
	public static OrderDto fromEntity(Order order){
		return new OrderDto(
			order.getId(),
			order.getMember().getMemberId(),
			order.getPostId(),
			order.getPrice().getValue(),
			order.getOrderAt()
		);
	}
}
