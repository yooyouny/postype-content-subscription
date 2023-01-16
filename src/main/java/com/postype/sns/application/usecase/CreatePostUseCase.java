package com.postype.sns.application.usecase;

import com.postype.sns.domain.member.model.FollowDto;
import com.postype.sns.domain.member.model.entity.Follow;
import com.postype.sns.domain.member.model.util.CursorRequest;
import com.postype.sns.domain.member.model.util.PageCursor;
import com.postype.sns.domain.member.service.FollowService;
import com.postype.sns.domain.member.service.MemberService;
import com.postype.sns.domain.post.model.Post;
import com.postype.sns.domain.post.model.PostDto;
import com.postype.sns.domain.post.model.TimeLine;
import com.postype.sns.domain.post.service.PostService;
import com.postype.sns.domain.post.service.TimeLineService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePostUseCase {

	private final PostService postService;
	private final FollowService followService;
	private final MemberService memberService;
	private final TimeLineService timeLineService;

	public Long execute(String title, String body, String memberId){
		Long postId = postService.create(title, body, memberId);

		List<Long> followMemberIds = followService
			.getFollowers(memberService.getMember(memberId))
			.stream()
			.map(FollowDto::getFromMemberId)
			.toList();

		timeLineService.deliveryToTimeLine(postId, followMemberIds);

		return postId;
	}




}
