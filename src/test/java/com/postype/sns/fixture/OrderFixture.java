package com.postype.sns.fixture;

import com.postype.sns.domain.member.model.MemberRole;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.order.model.Order;
import com.postype.sns.domain.order.model.Point;
import java.sql.Timestamp;
import java.time.Instant;

public class OrderFixture {
	public static Order get(Member member, Long postId, Point value) {
		Order order = Order.of(member, postId, value);
		return order;
	}
}
