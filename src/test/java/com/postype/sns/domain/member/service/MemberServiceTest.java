package com.postype.sns.domain.member.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.postype.sns.application.exception.MemberException;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.repository.MemberRepository;
import com.postype.sns.util.MemberFixture;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class MemberServiceTest {

	@Autowired
	private MemberService memberService;

	@MockBean
	private MemberRepository memberRepository;

	@Test
	@DisplayName("회원가입 성공 테스트")
	void registerOk(){
		String memberId = "memberId";
		String password = "password";

		//mocking
		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.empty());
		when(memberRepository.save(any())).thenReturn(Optional.of(mock(Member.class)));

		Assertions.assertDoesNotThrow(() -> memberService.register(memberId, password));
	}

	@Test
	@DisplayName("회원가입 실패 테스트 - memberId가 이미 있는 경우")
	void registerFailCausedByDuplicatedId(){
		String memberId = "memberId";
		String password = "password";

		Member fixture = MemberFixture.get(memberId, password);
		//mocking
		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(fixture));
		when(memberRepository.save(any())).thenReturn(Optional.of(fixture));
		Assertions.assertThrows(MemberException.class, () -> memberService.register(memberId, password));
	}

	@Test
	@DisplayName("로그인 성공 테스트")
	void loginOk(){
		String memberId = "user";
		String password = "password";

		//mocking member 객체로 하기
		//when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(mock(Member.classs)));

		//mocking fixture 사용
		Member fixture = MemberFixture.get(memberId, password);
		Assertions.assertDoesNotThrow(() -> memberService.login(memberId, password));
	}

	@Test
	@DisplayName("로그인 실패 테스트 - memberId가 없는 경우")
	void loginFailCausedByDuplicatedId(){
		String memberId = "memberId";
		String password = "password";

		//mocking
		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.empty());

		Assertions.assertThrows(MemberException.class, () -> memberService.login(memberId, password));
	}

	@Test
	@DisplayName("로그인 실패 테스트 - password가 틀린 경우")
	void loginFailCausedByWrongPassword(){
		String memberId = "memberId";
		String password = "password";
		String wrongPassword = "wrongPassword";

		Member fixture = MemberFixture.get(memberId, password);
		when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(fixture));


		Assertions.assertThrows(MemberException.class, () -> memberService.login(memberId, wrongPassword));
	}
}
