package com.postype.sns.application.usecase;

import com.postype.sns.domain.member.model.MemberDto;
import com.postype.sns.domain.member.service.MemberService;
import com.postype.sns.domain.order.service.OrderService;
import com.postype.sns.domain.post.model.PostDto;
import com.postype.sns.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

	private final MemberService memberService;

	private final PostService postService;
	private final OrderService orderService;

	public Long execute(String memberId, Long postId){
		MemberDto member = memberService.getMember(memberId);
		PostDto post = postService.getPostById(postId);
		return orderService.create(member, post);
	}

}
