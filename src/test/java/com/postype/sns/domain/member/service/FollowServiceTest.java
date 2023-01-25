package com.postype.sns.domain.member.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.postype.sns.application.exception.ApplicationException;
import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.application.contoller.dto.MemberDto;
import com.postype.sns.domain.member.model.entity.Follow;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.repository.FollowRepository;
import com.postype.sns.domain.member.repository.MemberRepository;
import com.postype.sns.fixture.MemberFixture;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
public class FollowServiceTest {

	@Autowired
	FollowService followService;
	@MockBean
	FollowRepository followrepository;
	@MockBean
	MemberRepository memberRepository;

	@Test
	@WithMockUser
	@DisplayName("팔로우 성공 테스트")
	//왜 null pointer 에러 나는지 모르겠음 ㅜㅜ
	void createFollowSuccess(){
		String toMemberId = "toMember";
		String fromMemberId = "fromMember";

		Member toMember = MemberFixture.get(toMemberId, "000", 1L);
		Member fromMember = MemberFixture.get(fromMemberId, "000", 2L);

		when(memberRepository.findByMemberId(fromMemberId)).thenReturn(Optional.of(fromMember));
		when(memberRepository.findByMemberId(toMemberId)).thenReturn(Optional.of(toMember));
		when(followrepository.save(Follow.of(fromMember.getId(), toMember.getId()))).thenReturn(mock(Follow.class));

		Assertions.assertDoesNotThrow(() -> followService.create(MemberDto.fromEntity(fromMember), MemberDto.fromEntity(toMember)));
	}

	@Test
	@WithMockUser
	@DisplayName("팔로우 할 멤버가 존재하지 않는 경우 실패 테스트")
	void createFollowFailCausedByNotfoundedMember(){
		String toMemberId = "toMember";
		String fromMemberId = "fromMember";

		Member toMember = MemberFixture.get(toMemberId, "000", 1L);
		Member fromMember = MemberFixture.get(fromMemberId, "000", 2L);

		when(memberRepository.findByMemberId(fromMemberId)).thenReturn(Optional.of(fromMember));
		when(memberRepository.findByMemberId(toMemberId)).thenReturn(Optional.empty());

		ApplicationException e = Assertions.assertThrows(
			ApplicationException.class, () -> followService.create(MemberDto.fromEntity(toMember), MemberDto.fromEntity(fromMember)));
		Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND, e.getErrorCode());
	}

	@Test
	@WithMockUser
	@DisplayName("팔로우 요청 멤버와 팔로우 할 멤버가 동일할 경우 실패 테스트")
	void createFollowFailCausedByDuplicatedMemberId(){
		String toMemberId = "toMember";
		String fromMemberId = "toMember";

		Member toMember = MemberFixture.get(toMemberId, "000", 1L);
		Member fromMember = MemberFixture.get(fromMemberId, "000", 1L);

		when(memberRepository.findByMemberId(fromMemberId)).thenReturn(Optional.of(fromMember));
		when(memberRepository.findByMemberId(toMemberId)).thenReturn(Optional.of(toMember));

		ApplicationException e = Assertions.assertThrows(
			ApplicationException.class, () -> followService.create(MemberDto.fromEntity(toMember), MemberDto.fromEntity(fromMember)));
		Assertions.assertEquals(ErrorCode.MEMBER_IS_SAME, e.getErrorCode());
	}





}
