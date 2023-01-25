package com.postype.sns.domain.member.service;

import com.postype.sns.application.exception.ApplicationException;
import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.application.contoller.dto.FollowDto;
import com.postype.sns.application.contoller.dto.MemberDto;
import com.postype.sns.domain.member.model.entity.Follow;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.repository.FollowRepository;
import com.postype.sns.domain.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

		memberRepository.findByMemberId(toMember.getMemberId()).orElseThrow(()
			-> new ApplicationException(ErrorCode.MEMBER_NOT_FOUND));

		Follow savedFollow = followRepository.save(Follow.of(fromMember.getId(), toMember.getId()));
		return FollowDto.fromEntity(savedFollow);
	}

	public Page<FollowDto> getFollowList(MemberDto fromMember, Pageable pageable) {
		Member member = getMemberOrException(fromMember.getMemberId());
		return followRepository.findAllByFromMemberId(member.getId(), pageable).map(FollowDto::fromEntity);
	}
	//해당 멤버를 팔로잉 하고 있는 멤버들의 목록을 반환
	public List<FollowDto> getFollowers(MemberDto toMember){
		Member member = getMemberOrException(toMember.getMemberId());
		return followRepository.findByToMemberId(member.getId()).stream().map(FollowDto::fromEntity).toList();
	}

	private Member getMemberOrException(String memberId){
		return memberRepository.findByMemberId(memberId).orElseThrow(() ->
			new ApplicationException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s not founded", memberId)));
	}
}
