package com.postype.sns.domain.order.model;

import com.postype.sns.domain.member.model.MemberDto;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.post.model.Post;
import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ORDERS")
@NoArgsConstructor
@Getter
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long id;
	@Column(name="member_id")
	private Long memberId;
	@Column(name = "post_id")
	private Long postId;
	@Embedded
	private Point price;
	@Column(name="order_at")
	private Timestamp orderAt;

	public static Order of(Long memberId, Long postId, Point price){
		Order order = new Order();
		order.memberId = memberId;
		order.postId = postId;
		order.price = price;
		return order;
	}

	@PrePersist
	void orderAt() {
		this.orderAt = Timestamp.from(Instant.now());
	}

}
