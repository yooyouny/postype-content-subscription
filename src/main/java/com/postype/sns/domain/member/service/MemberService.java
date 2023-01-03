package com.postype.sns.domain.member.service;

import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.application.exception.MemberException;
import com.postype.sns.domain.member.model.MemberRequestDto;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	public MemberRequestDto register(String memberId, String password) {

		memberRepository.findByMemberId(memberId).ifPresent(it -> {
			throw new MemberException(ErrorCode.DUPLICATED_MEMBER_NAME, String.format("%s is duplicated", memberId));
		});

		Member savedMember = memberRepository.save(Member.of(memberId, password));
		return MemberRequestDto.fromMember(savedMember);
	}

	public String login(String memberId, String password) {
		//회원가입 여부 확인
		Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new MemberException(ErrorCode.DUPLICATED_MEMBER_NAME, ""));

		//패스워드 일치 확인
		if(!member.getPassword().equals(password))
			throw new MemberException(ErrorCode.DUPLICATED_MEMBER_NAME, "");

		//토큰 생성

		return "";
	}


}
