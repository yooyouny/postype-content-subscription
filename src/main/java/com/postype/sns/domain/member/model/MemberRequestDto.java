package com.postype.sns.domain.member.model;

import com.postype.sns.domain.member.model.entity.Member;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto{
	private Long id;
	private String memberId;
	private String password;
	private String email;
	private MemberRole role;
	private Timestamp registeredAt;
	private Timestamp updatedAt;
	private Timestamp deletedAt;

	public static MemberRequestDto fromMember(Member member){
		return new MemberRequestDto(
			member.getId(),
			member.getMemberId(),
			member.getPassword(),
			member.getEmail(),
			member.getRole(),
			member.getRegisteredAt(),
			member.getUpdatedAt(),
			member.getDeletedAt()
		);
	}

}
