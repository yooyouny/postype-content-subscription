package com.postype.sns.application.contoller;

import com.postype.sns.application.contoller.dto.request.PostCommentRequest;
import com.postype.sns.application.contoller.dto.request.PostCreateRequest;
import com.postype.sns.application.contoller.dto.request.PostModifyRequest;
import com.postype.sns.application.contoller.dto.response.CommentResponse;
import com.postype.sns.application.contoller.dto.response.PostResponse;
import com.postype.sns.application.contoller.dto.response.Response;
import com.postype.sns.application.usecase.CreatePostUseCase;
import com.postype.sns.application.usecase.TimeLinePostsUseCase;
import com.postype.sns.domain.member.model.util.CursorRequest;
import com.postype.sns.domain.member.model.util.PageCursor;
import com.postype.sns.domain.post.model.Post;
import com.postype.sns.application.contoller.dto.PostDto;
import com.postype.sns.domain.post.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;
	private final TimeLinePostsUseCase timeLinePostsUseCase;
	private final CreatePostUseCase createPostUseCase;


	@PostMapping
	public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication){
		createPostUseCase.execute(request.getTitle(), request.getBody(), authentication.getName(), request.getPrice());
		return Response.success();
	}

	@PutMapping("/{postId}")
	public Response<PostResponse> modify(@PathVariable Long postId, @RequestBody PostModifyRequest request, Authentication authentication){
		PostDto post = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);
		return Response.success(PostResponse.fromPostDto(post));
	}

	@DeleteMapping("/{postId}")
	public Response<Void> delete(@PathVariable Long postId, Authentication authentication){
		postService.delete(authentication.getName(), postId);
		return Response.success();
	}

	@GetMapping //TODO :: 오프셋 기반 좋아요 순 으로 정렬 limit 30 size 10
	public Response<Page<PostResponse>> getPostList(Pageable pageable, Authentication authentication){
		return Response.success(postService.getList(pageable).map(PostResponse::fromPostDto));
	}

	@GetMapping("/my") //TODO :: 오프셋 기반 timestamp 내림차순으로 정렬
	public Response<Page<PostResponse>> getMyPostList(Pageable pageable, Authentication authentication){
		return Response.success(postService.getMyPostList(authentication.getName(), pageable).map(PostResponse::fromPostDto));
	}

	@GetMapping("/member/timeline")
	public PageCursor<Post> getTimeLine(Authentication authentication, CursorRequest request){
		return timeLinePostsUseCase.executeTimeLine(authentication.getName(), request);
	}

	@PostMapping("{postId}/likes")
	public Response<Void> like(@PathVariable Long postId, Authentication authentication){
		postService.like(postId, authentication.getName());
		return Response.success();
	}

	@GetMapping("{postId}/likes")
	public Response<Integer> getLike(@PathVariable Long postId){
		return Response.success(postService.getLikeCount(postId));
	}

	@GetMapping("/likes")
	public Response<List<PostDto>> getLikePosts(Authentication authentication){
		return Response.success(postService.getLikePosts(authentication.getName()));
	}

	@PostMapping("{postId}/comments")
	public Response<Void> comment(@PathVariable Long postId, @RequestBody PostCommentRequest request, Authentication authentication){
		postService.comment(postId, authentication.getName(), request.getComment());
		return Response.success();
	}

	@GetMapping("{postId}/comments")
	public Response<Page<CommentResponse>> getComment(@PathVariable Long postId, Pageable pageable){
		return Response.success(postService.getComment(postId, pageable).map(CommentResponse::fromDto));
	}




}
