package com.postype.sns.domain.order.service;

import com.postype.sns.domain.member.model.MemberDto;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.order.model.Order;
import com.postype.sns.domain.order.model.Point;
import com.postype.sns.domain.order.repository.OrderRepository;
import com.postype.sns.domain.post.model.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	public Long create(MemberDto member, PostDto post){
		return orderRepository.save(Order.of(member.getId(), post.getId(), post.getPrice())).getId();
	}

}
