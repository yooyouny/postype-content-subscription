package com.postype.sns.application.usecase;

import com.postype.sns.domain.member.model.FollowDto;
import com.postype.sns.domain.member.model.MemberDto;
import com.postype.sns.domain.member.model.entity.Follow;
import com.postype.sns.domain.member.model.util.CursorRequest;
import com.postype.sns.domain.member.model.util.PageCursor;
import com.postype.sns.domain.member.service.FollowService;
import com.postype.sns.domain.member.service.MemberService;
import com.postype.sns.domain.post.model.Post;
import com.postype.sns.domain.post.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class TimeLinePostsUseCase {
	final private FollowService followService;
	final private PostService postService;
	final private MemberService memberService;
	public PageCursor<Post> execute(String memberId, CursorRequest request){
		MemberDto member = memberService.getMember(memberId);
		List<FollowDto> followings = followService.getFollowList(member);
		List<Long> followingMemberIds = followings.stream().map(FollowDto::getToMemberId).toList();
		return postService.getTimeLinePosts(followingMemberIds, request);
	}

}
