package com.postype.sns.domain.member.model.entity;

import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "follow")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE follow SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
//TODO :: table에 인덱스 추가
public class Follow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "from_member_id")
	private Long fromMemberId;
	@Column(name = "to_member_id")
	private Long toMemberId;
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
	public static Follow of(Long fromMemberId, Long toMemberId){
		Follow follow = new Follow();
		follow.setFromMemberId(fromMemberId);
		follow.setToMemberId(toMemberId);
		return follow;
	}

}
