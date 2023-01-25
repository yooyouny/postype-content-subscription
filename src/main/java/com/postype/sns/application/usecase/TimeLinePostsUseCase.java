package com.postype.sns.application.usecase;

import com.postype.sns.application.contoller.dto.FollowDto;
import com.postype.sns.application.contoller.dto.MemberDto;
import com.postype.sns.domain.member.model.util.CursorRequest;
import com.postype.sns.domain.member.model.util.PageCursor;
import com.postype.sns.domain.member.service.FollowService;
import com.postype.sns.domain.member.service.MemberService;
import com.postype.sns.domain.post.model.Post;
import com.postype.sns.domain.post.model.TimeLine;
import com.postype.sns.domain.post.service.PostService;
import com.postype.sns.domain.post.service.TimeLineService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimeLinePostsUseCase {
	final private FollowService followService;
	final private PostService postService;
	final private MemberService memberService;
	final private TimeLineService timeLineService;

	public PageCursor<Post> execute(String memberId, CursorRequest request){ //pull model
		MemberDto member = memberService.getMember(memberId);
		List<FollowDto> followings = followService.getFollowList(member);
		List<Long> followingMemberIds = followings.stream().map(FollowDto::getToMemberId).toList();
		return postService.getTimeLinePosts(followingMemberIds, request);
	}

	public PageCursor<Post> executeTimeLine(String memberId, CursorRequest request){//push
		MemberDto member = memberService.getMember(memberId);
		PageCursor<TimeLine> pagedTimeLines = timeLineService.getTimeLine(member.getId(), request); //왜 페이지 커서에 담아야하지?
		List<Long> postIds = pagedTimeLines.contents().stream().map(TimeLine::getPostId).toList();
		List<Post> posts = postService.getPostsByIds(postIds);
		return new PageCursor(pagedTimeLines.nextCursorRequest(), posts);
	}

}
