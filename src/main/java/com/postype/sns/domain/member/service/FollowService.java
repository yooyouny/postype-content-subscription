package com.postype.sns.domain.member.service;

import com.postype.sns.application.exception.ApplicationException;
import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.domain.member.model.FollowDto;
import com.postype.sns.domain.member.model.MemberDto;
import com.postype.sns.domain.member.model.entity.Follow;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.repository.FollowRepository;
import com.postype.sns.domain.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

	private final FollowRepository followRepository;
	private final MemberRepository memberRepository;

	public FollowDto create(MemberDto fromMember, MemberDto toMember) {
		if(fromMember.getId() == toMember.getId())
			throw new ApplicationException(ErrorCode.MEMBER_IS_SAME, String.format(" %s is same", fromMember.getMemberId()));

		log.error(fromMember.getId() + " " + toMember.getId());

		Follow savedFollow = followRepository.save(Follow.of(fromMember.getId(), toMember.getId()));
		return FollowDto.fromEntity(savedFollow);
	}

	public List<FollowDto> getFollowList(MemberDto fromMember) {
		Member member = memberRepository.findByMemberId(fromMember.getMemberId()).orElseThrow(()
			-> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));

		return followRepository.findByFromMemberId(member.getId()).stream().map(
			FollowDto::fromEntity).toList();
	}
}
