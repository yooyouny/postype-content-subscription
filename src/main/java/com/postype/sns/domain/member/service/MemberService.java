package com.postype.sns.domain.member.service;

import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.application.exception.MemberException;
import com.postype.sns.domain.member.model.MemberRequestDto;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.repository.MemberRepository;
import com.postype.sns.utill.JwtTokenUtils;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder encoder;

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.token.expired-time-ms}")
	private Long expiredTimeMs;

	@Transactional
	public MemberRequestDto register(String memberId, String password) {

		memberRepository.findByMemberId(memberId).ifPresent(it -> {
			throw new MemberException(ErrorCode.DUPLICATED_MEMBER_NAME);
		});

		Member savedMember = memberRepository.save(Member.of(memberId, encoder.encode(password)));

		return MemberRequestDto.fromMember(savedMember);
	}

	public String login(String memberId, String password) {
		Member member = memberRepository.findByMemberId(memberId).orElseThrow(()
			-> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

		//if(!member.getPassword().equals(password)) 인코딩 전
		if(!encoder.matches(password, member.getPassword()))
			throw new MemberException(ErrorCode.INVALID_PASSWORD);

		//token
		String token = JwtTokenUtils.generateToken(memberId, secretKey, expiredTimeMs);

		return token;
	}


}
