package com.postype.sns.application.contoller;

import com.postype.sns.application.contoller.dto.request.OrderCreateRequest;
import com.postype.sns.application.contoller.dto.response.OrderResponse;
import com.postype.sns.application.contoller.dto.response.Response;
import com.postype.sns.application.usecase.OrderUseCase;
import com.postype.sns.domain.order.model.Order;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

	private final OrderUseCase OrderUseCase;

	@PostMapping("/{postId}")
	public Response<OrderResponse> create(Authentication authentication, @PathVariable Long postId){
		return Response.success(
			OrderResponse.fromDto(OrderUseCase.create(authentication.getName(), postId)));
	}

	@GetMapping("/{postId}")
	public Response<OrderResponse> getOrderByMemberAndPost(Authentication authentication, @PathVariable Long postId){
		return Response.success(
			OrderResponse.fromDto(OrderUseCase.getOrder(authentication.getName(), postId)));
	}

}
