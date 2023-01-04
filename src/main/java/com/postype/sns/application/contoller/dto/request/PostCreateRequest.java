package com.postype.sns.application.contoller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateRequest {
	private String title;
	private String body;
}
