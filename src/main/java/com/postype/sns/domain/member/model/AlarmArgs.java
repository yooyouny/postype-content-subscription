package com.postype.sns.domain.member.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmArgs {
	private Long fromMemberId; //알람을 발생 시킨 멤버의 아이디
	private String causedBy; // 알람을 발생시킨 대상 (코멘트, 포스트, 라이크, 팔로우)
	private Long targetId; // 알람을 발생시킨 대상의 아이디
}
