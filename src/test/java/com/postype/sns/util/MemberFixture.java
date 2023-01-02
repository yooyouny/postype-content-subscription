package com.postype.sns.util;

import com.postype.sns.domain.member.model.entity.Member;

public class MemberFixture {
//테스트용으로 만든 Member Entity
	public static Member get(String memberId, String password){
		Member member = Member.builder()
			.id(1L)
			.memberId(memberId)
			.password(password)
			.build();

		return member;
	}
}
