package com.postype.sns.application.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postype.sns.application.contoller.dto.request.MemberLoginRequest;
import com.postype.sns.application.contoller.dto.request.MemberRegisterRequest;
import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.application.exception.MemberException;
import com.postype.sns.domain.member.model.MemberRequestDto;
import com.postype.sns.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc //컨트롤러 api 테스트
public class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private MemberService memberService;
	@Test
	@DisplayName("회원가입 성공 테스트")
	@WithAnonymousUser
	public void registerOk() throws Exception {
		String memberId = "memberId";
		String password = "password";

		when(memberService.register(memberId, password)).thenReturn(mock(MemberRequestDto.class));

		mockMvc.perform(post("/api/v1/members/register")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsBytes(new MemberRegisterRequest(memberId, password)))
		).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("회원가입 실패 테스트 - 이미 가입된 memberId으로 시도하는 경우 에러반환")
	public void registerFailCausedByDuplicated() throws Exception {
		String memberId = "memberId";
		String password = "password";

		when(memberService.register(memberId, password)).thenThrow(new MemberException(ErrorCode.DUPLICATED_MEMBER_NAME, ""));

		mockMvc.perform(post("/api/v1/members/register")
				.contentType(MediaType.APPLICATION_JSON)
				// TODO : add request body
			.content(objectMapper.writeValueAsBytes(new MemberRegisterRequest(memberId, password)))
		).andDo(print())
			.andExpect(status().isConflict());

	}

	@Test
	@DisplayName("로그인 성공 테스트")
	public void loginOk() throws Exception {
		String memberId = "memberId";
		String password = "password";

		when(memberService.login(memberId, password)).thenReturn("test_token");

		mockMvc.perform(post("/api/v1/members/login")
				.contentType(MediaType.APPLICATION_JSON)
				// TODO : add request body
				.content(objectMapper.writeValueAsBytes(new MemberLoginRequest(memberId, password)))
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("로그인 실패 테스트 - 아이디를 찾지 못함")
	public void loginFailCausedById() throws Exception {
		String memberId = "memberId";
		String password = "password";

		when(memberService.login(memberId, password)).thenThrow(new MemberException(ErrorCode.DUPLICATED_MEMBER_NAME, ""));

		mockMvc.perform(post("/api/v1/members/login")
				.contentType(MediaType.APPLICATION_JSON)
				// TODO : add request body
				.content(objectMapper.writeValueAsBytes(new MemberLoginRequest(memberId, password)))
			).andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("로그인 실패 테스트 - 잘못된 패스워드 입력")
	public void loginFailCausedByPassword() throws Exception {
		String memberId = "memberId";
		String password = "password";

		when(memberService.login(memberId, password)).thenThrow(new MemberException(ErrorCode.DUPLICATED_MEMBER_NAME, ""));

		mockMvc.perform(post("/api/v1/members/login")
				.contentType(MediaType.APPLICATION_JSON)
				// TODO : add request body
				.content(objectMapper.writeValueAsBytes(new MemberLoginRequest(memberId, password)))
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}
}
