package com.postype.sns.application.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postype.sns.application.contoller.dto.request.PostCreateRequest;
import com.postype.sns.domain.member.model.MemberDto;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.service.MemberService;
import com.postype.sns.domain.order.model.Order;
import com.postype.sns.domain.order.model.OrderDto;
import com.postype.sns.domain.order.service.OrderService;
import com.postype.sns.domain.post.model.Post;
import com.postype.sns.domain.post.model.PostDto;
import com.postype.sns.fixture.MemberFixture;
import com.postype.sns.fixture.PostFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private OrderService orderService;

	@Test
	@DisplayName("주문 성공 테스트")
	@WithMockUser
	@Transactional
	void orderCreateSuccess() throws Exception {
		Member member = MemberFixture.get("member", "password", 1L);
		Post post = PostFixture.get(member.getMemberId(), 1L, 1L);

		when(orderService.create(MemberDto.fromEntity(member), PostDto.fromPost(post)))
			.thenReturn(OrderDto.fromEntity(Order.of(member, post)));

		mockMvc.perform(post("/api/v1/order/1")
			.contentType(MediaType.APPLICATION_JSON)
		).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser //인증 되지 않은 유저
	@DisplayName("주문 요청시 로그인을 하지 않은 경우 실패 테스트")
	void orderCreateFailCausedByNotLogin() throws Exception {

		String title = "title";
		String body = "body";
		int price = 0;

		mockMvc.perform(post("/api/v1/order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body, price)))
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}
}
