package com.postype.sns.application.contoller;

import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberContoller {

	private final MemberService memberService;

	//TODO :: implement
	@PostMapping
	public void register(){
		Member member = memberService.register("", "");
	}
}
