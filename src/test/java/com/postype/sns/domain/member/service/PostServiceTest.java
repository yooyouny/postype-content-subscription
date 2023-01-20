package com.postype.sns.domain.member.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.application.exception.ApplicationException;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.repository.MemberRepository;
import com.postype.sns.domain.post.model.Post;
import com.postype.sns.domain.post.repository.PostRepository;
import com.postype.sns.domain.post.service.PostService;
import com.postype.sns.fixture.MemberFixture;
import com.postype.sns.fixture.PostFixture;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
		int price = 1000;

		//mocking
		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(mock(Member.class)));
		when(postRepository.save(any())).thenReturn(mock(Post.class));

		Assertions.assertDoesNotThrow(() -> postService.create(title, body, memberId, price));
	}

	@Test
	@DisplayName("포스트 작성 시 요청 멤버가 존재 하지 않는 경우 실패 테스트")
	void PostCreateFailCausedByNotFoundedMember(){
		String title = "title";
		String body = "body";
		String memberId = "memberId";
		int price = 1000;

		//mocking
		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.empty());
		when(postRepository.save(any())).thenReturn(mock(Post.class));

		ApplicationException e = Assertions.assertThrows(
			ApplicationException.class, () -> postService.create(title, body, memberId, price));
		Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND, e.getErrorCode());
	}

	@Test
	@DisplayName("포스트 수정 성공 테스트")
	void PostModifySuccess(){
		String title = "title";
		String body = "body";
		String memberId = "memberId";
		Long postId = 1L;

		//mocking
		Post post = PostFixture.get(memberId, postId, 1L);
		Member member = post.getMember();

		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member));
		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(postRepository.saveAndFlush(any())).thenReturn(post);

		Assertions.assertDoesNotThrow(() -> postService.modify(title, body, memberId, postId));
	}
	@Test
	@DisplayName("포스트 수정 시 작성자와 수정자가 다를 경우 실패 테스트")
	void postModifyFailCausedByNotLoginMember(){
		String title = "title";
		String body = "body";
		String memberId = "memberId";
		Long postId = 1L;

		//mocking
		Post post = PostFixture.get(memberId, postId, 1L); //memberId, postId, member sequence id
		Member writer = MemberFixture.get("memberId", "password", 2L);

		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(writer));
		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		ApplicationException e = Assertions.assertThrows(
			ApplicationException.class, () -> postService.modify(title, body, memberId, postId));
		Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
	}
	@Test
	@DisplayName("포스트 수정 시 포스트가 존재 하지 않는 경우 실패 테스트")
	void postModifyFailCausedByNotFoundedPost(){
		String title = "title";
		String body = "body";
		String memberId = "memberId";
		Long postId = 1L;

		//mocking
		Post post = PostFixture.get(memberId, postId, 1L);
		Member member = post.getMember();

		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member)); //포스트에 등록된 작성자와 멤버가 동일함을 명시
		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		ApplicationException e = Assertions.assertThrows(
			ApplicationException.class, () -> postService.modify(title, body, memberId, postId));
		Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
	}

	@Test
	@DisplayName("포스트 삭제 성공 테스트")
	void PostDeleteSuccess(){
		String memberId = "memberId";
		Long postId = 1L;

		//mocking
		Post post = PostFixture.get(memberId, postId, 1L);
		Member member = post.getMember();

		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member));
		when(postRepository.findById(postId)).thenReturn(Optional.of(post));


		Assertions.assertDoesNotThrow(() -> postService.delete(memberId, 1L));
	}
	@Test
	@DisplayName("포스트 삭제 시 권한이 없는 경우 실패 테스트")
	void postDeleteFailCausedByNotLoginMember(){
		String title = "title";
		String body = "body";
		String memberId = "memberId";
		Long postId = 1L;

		//mocking
		Post post = PostFixture.get(memberId, postId, 1L);
		Member writer = MemberFixture.get("memberId", "password", 2L);

		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(writer));
		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		ApplicationException e = Assertions.assertThrows(
			ApplicationException.class, () -> postService.delete(memberId, 1L));
		Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
	}
	@Test
	@DisplayName("포스트 삭제 시 포스트가 존재 하지 않는 경우 실패 테스트")
	void postDeleteFailCausedByNotFoundedPost(){
		String memberId = "memberId";
		Long postId = 1L;

		//mocking
		Post post = PostFixture.get(memberId, postId, 1L);
		Member member = post.getMember();

		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member));
		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		ApplicationException e = Assertions.assertThrows(
			ApplicationException.class, () -> postService.delete(memberId, 1L));
		Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
	}

	@Test
	@DisplayName("피드 목록 조회 성공 테스트")
	void FeedListSuccess(){

		Pageable pageable = mock(Pageable.class);

		when(postRepository.findAll(pageable)).thenReturn(Page.empty());

		Assertions.assertDoesNotThrow(() -> postService.getList(pageable));
	}

	@Test
	@DisplayName("내 피드 목록 조회 성공 테스트")
	void MyFeedListSuccess(){

		Pageable pageable = mock(Pageable.class);
		Member member = mock(Member.class);

		when(memberRepository.findByMemberId((any()))).thenReturn(Optional.of(member));
		when(postRepository.findAllByMemberId(member.getId(), pageable)).thenReturn(Page.empty());

		Assertions.assertDoesNotThrow(() -> postService.getMyPostList("", pageable));
	}

}
