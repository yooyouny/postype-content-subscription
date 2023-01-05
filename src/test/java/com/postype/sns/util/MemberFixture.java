package com.postype.sns.util;

import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.model.MemberRole;
import java.sql.Timestamp;
import java.time.Instant;

public class MemberFixture {
//테스트용으로 만든 Member Entity
	public static Member get(String memberId, String password){
		Member member = new Member();
			member.setId(2L);
			member.setMemberId(memberId);
			member.setPassword(password);
			member.setRole(MemberRole.USER);
			member.setRegisteredAt(Timestamp.from(Instant.now()));
		return member;
	}
}
