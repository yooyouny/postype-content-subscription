package com.postype.sns.application.contoller;

import com.postype.sns.application.contoller.dto.response.FollowResponse;
import com.postype.sns.application.contoller.dto.response.Response;
import com.postype.sns.domain.member.model.FollowDto;
import com.postype.sns.domain.member.model.MemberDto;
import com.postype.sns.domain.member.model.entity.Follow;
import com.postype.sns.domain.member.service.FollowService;
import com.postype.sns.domain.member.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/follow")
@RequiredArgsConstructor
@Slf4j
public class FollowController {

	private final FollowService followService;
	private final MemberService memberService;

	@PostMapping("/{fromMemberName}/{toMemberName}")
	public Response<FollowResponse> create(@PathVariable String fromMemberName, @PathVariable String toMemberName){
		var fromMember = memberService.getMember(fromMemberName);
		var toMember = memberService.getMember(toMemberName);
		FollowDto dto = followService.create(fromMember, toMember);

		return Response.success(FollowResponse.fromFollowDto(dto));
	}

	@GetMapping("/{fromMemberName}") //dto를 controller client, contorller service사이에 쓸 수 있나? response로 쓰고 싶은데 못하겠음 ㅜ
	public Response<List<FollowDto>> getFollowList(@PathVariable String fromMemberName){
		MemberDto fromMember = memberService.getMember(fromMemberName);
		return Response.success(followService.getFollowList(fromMember));
	}


}
