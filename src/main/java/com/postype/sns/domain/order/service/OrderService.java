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
		orderRepository.findAllByMemberIdAndPostId(member.getId(), post.getId()).ifPresent(it ->
			new ApplicationException(ErrorCode.DUPLICATED_KEY)); //왜 못잡지???

		Order order = orderRepository.save(Order.of(Member.of(member), post.getId(), post.getPrice()));

		return OrderDto.fromEntity(order);
	}

	public OrderDto findByMemberIdAndPostId(MemberDto member, PostDto post) {
		Order order = orderRepository.findByMemberIdAndPostId(member.getId(), post.getId());
		return OrderDto.fromEntity(order);
	}
}
