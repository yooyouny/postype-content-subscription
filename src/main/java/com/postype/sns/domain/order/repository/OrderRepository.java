package com.postype.sns.domain.order.repository;

import com.postype.sns.domain.order.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	Order findByMemberIdAndPostId(Long memberId, Long postId);
	Optional<Order> findAllByMemberIdAndPostId(Long memberId, Long postId);
}
