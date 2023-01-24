package com.postype.sns.domain.order.service;

import com.postype.sns.application.exception.ApplicationException;
import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.domain.member.model.MemberDto;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.order.model.Order;
import com.postype.sns.domain.order.model.OrderDto;
import com.postype.sns.domain.order.model.Point;
import com.postype.sns.domain.order.repository.OrderRepository;
import com.postype.sns.domain.post.model.Post;
import com.postype.sns.domain.post.model.PostDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	public OrderDto create(MemberDto member, PostDto post){
		orderRepository.findAllByMemberIdAndPostId(member.getId(), post.getId()).ifPresent(it -> {
			throw new ApplicationException(ErrorCode.ALREADY_ORDER,
				String.format("%s already ordered this post", member.getMemberId()));
		});

		Order order = orderRepository.save(Order.of(Member.of(member), Post.of(post)));

		return OrderDto.fromEntity(order);
	}

	public OrderDto findByMemberIdAndPostId(MemberDto member, PostDto post) {
		Order order = orderRepository.findByMemberIdAndPostId(member.getId(), post.getId());
		return OrderDto.fromEntity(order);
	}
}
