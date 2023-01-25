package com.postype.sns.application.usecase;

import com.postype.sns.application.contoller.dto.MemberDto;
import com.postype.sns.domain.member.service.MemberService;
import com.postype.sns.application.contoller.dto.OrderDto;
import com.postype.sns.domain.order.service.OrderService;
import com.postype.sns.application.contoller.dto.PostDto;
import com.postype.sns.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderUseCase {

	private final MemberService memberService;
	private final PostService postService;
	private final OrderService orderService;

	@Transactional
	public OrderDto create(String memberId, Long postId){
		MemberDto member = memberService.getMember(memberId);
		PostDto post = postService.getPost(postId);
		return orderService.create(member, post);
	}

	public OrderDto getOrder(String memberId, Long postId){
		MemberDto member = memberService.getMember(memberId);
		PostDto post = postService.getPost(postId);
		return orderService.findByMemberIdAndPostId(member, post);
	}

}
