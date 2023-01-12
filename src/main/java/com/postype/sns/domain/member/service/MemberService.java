package com.postype.sns.domain.member.service;

import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.application.exception.ApplicationException;
import com.postype.sns.domain.member.model.MemberDto;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.repository.MemberRepository;
import com.postype.sns.utill.JwtTokenUtils;
import java.util.List;
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

	public MemberDto loadMemberByMemberId(String memberId){
		return memberRepository.findByMemberId(memberId).map(MemberDto::fromEntity).orElseThrow(() ->
			new ApplicationException(ErrorCode.MEMBER_NOT_FOUND, String.format(" %s is not founded", memberId)));
	}

	public MemberDto getMember(String fromMemberId){
		Member member = memberRepository.findByMemberId(fromMemberId).orElseThrow(() ->
			new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));
		return MemberDto.fromEntity(member);
	}

	public List<MemberDto> getMembers(List<Long> ids){
		return memberRepository.findAllByIn(ids).stream().map(MemberDto::fromEntity).toList();
	}

	@Transactional
	public MemberDto register(String memberId, String password) {

		memberRepository.findByMemberId(memberId).ifPresent(it -> {
			throw new ApplicationException(ErrorCode.DUPLICATED_MEMBER_ID);
		});

		Member savedMember = memberRepository.save(Member.of(memberId, encoder.encode(password)));

		return MemberDto.fromEntity(savedMember);
	}

	public String login(String memberId, String password) {
		Member member = memberRepository.findByMemberId(memberId).orElseThrow(()
			-> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));

		//if(!member.getPassword().equals(password)) 인코딩 전
		if(!encoder.matches(password, member.getPassword()))
			throw new ApplicationException(ErrorCode.INVALID_PASSWORD);

		//token
		String token = JwtTokenUtils.generateToken(memberId, secretKey, expiredTimeMs);

		return token;
	}


}
