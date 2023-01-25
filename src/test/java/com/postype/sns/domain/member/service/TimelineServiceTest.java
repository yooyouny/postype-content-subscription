package com.postype.sns.domain.member.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.postype.sns.application.usecase.TimeLinePostsUseCase;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.model.util.CursorRequest;
import com.postype.sns.domain.member.repository.MemberRepository;
import com.postype.sns.domain.post.model.Post;
import com.postype.sns.domain.post.model.TimeLine;
import com.postype.sns.domain.post.repository.PostRepository;
import com.postype.sns.domain.post.repository.TimeLineRepository;
import com.postype.sns.fixture.PostFixture;
import com.postype.sns.fixture.TimeLineFixture;
import java.util.ArrayList;
import java.util.List;
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
public class TimelineServiceTest {

	@Autowired
	private TimeLinePostsUseCase timeLinePostsUseCase;

	@MockBean
	private TimeLineRepository timeLineRepository;

	@MockBean
	private MemberRepository memberRepository;

	@MockBean
	private PostRepository postRepository;

	@Test
	@DisplayName("포스트 저장 시 타임라인 테이블 저장 성공 테스트")
	void saveTimeLineSuccess(){

		Long postId = 1L;
		String memberId = "member";
		long id = 1L;

		Member member = mock(Member.class);
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		TimeLine timeLine = TimeLineFixture.get(id, postId);
		Post post = PostFixture.get(memberId, postId, id);
		Pageable pageable = mock(Pageable.class);

		//mocking
		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member));
		List<TimeLine> timelines = new ArrayList<>();
		when(timeLineRepository.findAllByMemberIdAndOrderByIdDesc(1L, 10)).thenReturn(
			(List.of(timeLine)));
		when(postRepository.findAllByInId(ids)).thenReturn((List<Post>) mock(Post.class));

		Assertions.assertDoesNotThrow(() -> timeLinePostsUseCase.executeTimeLine(memberId,
			mock(CursorRequest.class)));
	}




}
