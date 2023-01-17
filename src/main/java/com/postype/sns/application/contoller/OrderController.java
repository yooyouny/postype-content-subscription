package com.postype.sns.application.contoller;

import com.postype.sns.application.usecase.CreateOrderUseCase;
import com.postype.sns.domain.order.service.OrderService;
import com.postype.sns.domain.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

	private final CreateOrderUseCase createOrderUseCase;

	@PostMapping("/{postId}")
	public void create(Authentication authentication, @PathVariable Long postId){
		createOrderUseCase.execute(authentication.getName(), postId);
	}


}
