package com.postype.sns.application.usecase;

import com.postype.sns.domain.member.model.MemberDto;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.service.MemberService;
import com.postype.sns.domain.order.model.Order;
import com.postype.sns.domain.order.model.OrderDto;
import com.postype.sns.domain.order.service.OrderService;
import com.postype.sns.domain.post.model.PostDto;
import com.postype.sns.domain.post.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderUseCase {

	private final MemberService memberService;
	private final PostService postService;
	private final OrderService orderService;


	//TODO :: 이미 구입한 포스트면 예외 발생
	@Transactional
	public OrderDto create(String memberId, Long postId){
		MemberDto member = memberService.getMember(memberId);
		PostDto post = postService.getPostById(postId);
		return orderService.create(member, post);
	}

	public OrderDto getOrder(String memberId, Long postId){
		MemberDto member = memberService.getMember(memberId);
		PostDto post = postService.getPostById(postId);
		return orderService.findByMemberIdAndPostId(member, post);
	}

}
