package com.postype.sns.domain.member.service;

import com.postype.sns.application.exception.MemberException;
import com.postype.sns.domain.member.model.dto.MemberDto;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

//TODO :: implement
	public Member register(String memberId, String password) {
		//	회원가입하려는 memberId로 회원가입 된 user가 있는지

		Optional<Member> member= memberRepository.findByMemberId(memberId);

		//	회원가입 진행 -> User 등록 진행
		memberRepository.save(new Member());
		return new Member();
	}

	public String login(String memberId, String password) {
		//회원가입 여부 확인
		Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new MemberException());

		//패스워드 일치 확인
		if(!member.getPassword().equals(password))
			throw new MemberException();

		//토큰 생성

		return "";
	}
}
