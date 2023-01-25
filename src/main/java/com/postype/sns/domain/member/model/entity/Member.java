package com.postype.sns.domain.member.model.entity;

import com.postype.sns.application.contoller.dto.MemberDto;
import com.postype.sns.domain.order.model.Order;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class Member {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "member_id")
	private String memberId;
	private String password;
	@Column(name = "member_name")
	private String memberName;
	private String email;
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private MemberRole role = MemberRole.USER;
	@OneToMany(mappedBy = "member")
	private List<Order> orders = new ArrayList<Order>();
	@Column(name = "register_at")
	private Timestamp registeredAt;
	@Column(name = "updated_at")
	private Timestamp updatedAt;
	@Column(name = "deleted_at")
	private Timestamp deletedAt;

	@PrePersist
	void registeredAt() {
		this.registeredAt = Timestamp.from(Instant.now());
	}

	@PreUpdate
	void updatedAt() {
		this.updatedAt = Timestamp.from(Instant.now());
	}
	//새 멤버 엔티티를 만들어 주는 메소드
	public static Member of(String memberId, String password, String memberName, String email){
		Member member = new Member();
		member.setMemberId(memberId);
		member.setPassword(password);
		member.setMemberName(memberName);
		member.setEmail(email);
		return member;
	}

	public static Member of(MemberDto dto){
		Member member = new Member();
		member.setId(dto.getId());
		member.setMemberId(dto.getMemberId());
		member.setPassword(dto.getPassword());
		member.setMemberName(dto.getMemberName());
		member.setEmail(dto.getEmail());
		member.setRole(dto.getRole());
		return member;
	}

}
