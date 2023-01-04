package com.postype.sns.domain.member.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.application.exception.MemberException;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.repository.MemberRepository;
import com.postype.sns.domain.post.model.Post;
import com.postype.sns.domain.post.repository.PostRepository;
import com.postype.sns.domain.post.service.PostService;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class PostServiceTest {

	@Autowired
	private PostService postService;

	@MockBean
	private PostRepository postRepository;

	@MockBean
	private MemberRepository memberRepository;

	@Test
	@DisplayName("포스트 작성 성공 테스트")
	void PostCreateSuccess(){
		String title = "title";
		String body = "body";
		String memberId = "memberId";

		//mocking
		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(mock(Member.class)));
		when(postRepository.save(any())).thenReturn(mock(Post.class));

		Assertions.assertDoesNotThrow(() -> postService.create(title, body, memberId));
	}

	@Test
	@DisplayName("포스트 작성 시 요청 멤버가 존재 하지 않는 경우 실패 테스트")
	void PostCreateFailCausedByNotFoundedMember(){
		String title = "title";
		String body = "body";
		String memberId = "memberId";

		//mocking
		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.empty());
		when(postRepository.save(any())).thenReturn(mock(Post.class));

		MemberException e = Assertions.assertThrows(MemberException.class, () -> postService.create(title, body, memberId));
		Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND, e.getErrorCode());
	}

}
