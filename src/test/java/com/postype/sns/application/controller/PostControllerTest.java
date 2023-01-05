package com.postype.sns.application.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postype.sns.application.contoller.dto.request.PostCreateRequest;
import com.postype.sns.application.contoller.dto.request.PostModifyRequest;
import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.application.exception.ApplicationException;
import com.postype.sns.domain.post.model.PostRequestDto;
import com.postype.sns.domain.post.service.PostService;
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


@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PostService postService;

	@Test
	@WithMockUser //인증 된 유저
	@DisplayName("포스트 작성 성공 테스트")
	void postCreateSuccess() throws java.lang.Exception {

		String title = "title";
		String body = "body";

		mockMvc.perform(post("/api/v1/posts")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser //인증 되지 않은 유저
	@DisplayName("포스트 작성 시 로그인을 하지 않은 경우 실패 테스트")
	void postCreateFailCausedByNotLogin() throws java.lang.Exception {

		String title = "title";
		String body = "body";

		mockMvc.perform(post("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser //인증 된 유저
	@DisplayName("포스트 수정 성공 테스트")
	void postModifySuccess() throws java.lang.Exception {

		String title = "title";
		String body = "body";

		when(postService.modify(eq(title), eq(body), any(), any()))
			.thenReturn(PostRequestDto.fromPost(PostFixture.get("memberId", 1L, 1L)));

		mockMvc.perform(put("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("포스트 수정 시 로그인 하지 않은 경우 실패 테스트")
	@WithAnonymousUser
	void postModifyFailCausedByNotLoginMember() throws java.lang.Exception {

		String title = "title";
		String body = "body";

		mockMvc.perform(put("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
			).andDo(print())
			.andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
	}

	@Test
	@WithMockUser //인증 된 유저
	@DisplayName("포스트 수정 멤버가 작성 멤버가 아닐 경우 실패 테스트")
	void postModifyFailCausedByNotCreatedMember() throws java.lang.Exception {

		String title = "title";
		String body = "body";
		//반환 값이 없는 경우 when이 아닌 doThrow
		doThrow(new ApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(body), any(), eq(1L));

		mockMvc.perform(put("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser //인증 된 유저
	@DisplayName("포스트 수정 시 포스트가 없는 경우 실패 테스트")
	void postModifyFailCausedByNotFoundedPost() throws java.lang.Exception {

		String title = "title";
		String body = "body";

		//mocking
		doThrow(new ApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(title), eq(body), any(), eq(1L));


		mockMvc.perform(put("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
			).andDo(print())
			.andExpect(status().isNotFound());
	}
	@Test
	@WithMockUser //인증 된 유저
	@DisplayName("포스트 삭제 성공 테스트")
	void postDeleteSuccess() throws java.lang.Exception {

		mockMvc.perform(delete("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser //인증 되지 않은 유저
	@DisplayName("포스트 삭제 시 로그인 하지 않은 경우 실패 테스트")
	void postDeleteFailNotLoginMember() throws java.lang.Exception {

		mockMvc.perform(delete("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser //인증 된 유저
	@DisplayName("포스트 삭제 성공 테스트")
	void postDeleteSuccess() throws java.lang.Exception {

		mockMvc.perform(delete("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
	}






}
