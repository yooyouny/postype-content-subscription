package com.postype.sns.application.contoller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostModifyRequest {
	private String title;
	private String body;
	private int price;
}
