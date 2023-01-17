package com.postype.sns.domain.order.repository;

import com.postype.sns.domain.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
