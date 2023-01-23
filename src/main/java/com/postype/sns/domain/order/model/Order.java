package com.postype.sns.domain.order.model;

import com.postype.sns.domain.member.model.MemberDto;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.post.model.Post;
import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "orders")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
	@Column(name = "post_id")
	private Long postId;
	@Embedded
	private Point price;
	@Column(name="order_at")
	private Timestamp orderAt;

	public static Order of(Member member, Long postId, Point price){
		Order order = new Order();
		order.setMember(member);
		order.postId = postId;
		order.price = price;
		return order;
	}
	public void setMember(Member member){
		if(this.member != null){
			this.member.getOrders().remove(this);
		}
		this.member = member;
		member.getOrders().add(this);
	}

	@PrePersist
	void orderAt() {
		this.orderAt = Timestamp.from(Instant.now());
	}




}
