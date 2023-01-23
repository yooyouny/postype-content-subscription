package com.postype.sns.domain.post.service;

import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.application.exception.ApplicationException;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.model.util.CursorRequest;
import com.postype.sns.domain.member.model.util.PageCursor;
import com.postype.sns.domain.member.repository.MemberRepository;
import com.postype.sns.domain.post.model.Post;
import com.postype.sns.domain.post.model.PostDto;
import com.postype.sns.domain.post.repository.PostRepository;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService{

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	@Transactional
	public Long create(String title, String body, String memberId, int price){
		//user find
		Member foundedMember = memberRepository.findByMemberId(memberId).orElseThrow(() ->
			new ApplicationException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s not founded", memberId)));
		//post save
		return postRepository.save(Post.of(title, body, foundedMember, price)).getId();
	}

	@Transactional
	public PostDto modify(String title, String body, String memberId, Long postId){
		//user find
		Member foundedMember = memberRepository.findByMemberId(memberId).orElseThrow(() ->
			new ApplicationException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s not founded", memberId)));

		//post exist
		Post post = postRepository.findById(postId).orElseThrow(() ->
			 new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

		//post permission
		if(post.getMember() != foundedMember){
			throw new ApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", memberId, postId));
		}

		post.setTitle(title);
		post.setBody(body);
		return PostDto.fromPost(postRepository.saveAndFlush(post));
	}
	@Transactional
	public void delete(String memberId, Long postId){
		//user find
		Member foundedMember = memberRepository.findByMemberId(memberId).orElseThrow(() ->
			new ApplicationException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s not founded", memberId)));

		Post post = postRepository.findById(postId).orElseThrow(() ->
			new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

		if(post.getMember() != foundedMember){
			throw new ApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", memberId, postId));
		}

		postRepository.delete(post);
	}

	public Page<PostDto> getList (Pageable pageable){
		return postRepository.findAll(pageable).map(PostDto::fromPost);
	}

	//내가 쓴 글 읽기
	public Page<PostDto> getMyPostList (String memberId, Pageable pageable){

		Member member = memberRepository.findByMemberId(memberId).orElseThrow(() ->
			new ApplicationException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s not founded", memberId)));

		return postRepository.findAllByMemberId(member.getId(), pageable).map(PostDto::fromPost);
	}

	public List<Post> getPostsByIds(List<Long> ids){
		return postRepository.findAllByInId(ids);
	}
	public PostDto getPostById(Long id) {return postRepository.findById(id).map(PostDto::fromPost).orElseThrow(() ->
		new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", id)));}

	public PageCursor<Post> getTimeLinePosts(List<Long> memberIds, CursorRequest request){
		List<Post> posts = findAllByMemberId(memberIds, request);
		Long nextKey = posts.stream()
			.mapToLong(Post::getId)
			.min()
			.orElse(CursorRequest.DEFAULT_KEY);
		return new PageCursor<>(request.next(nextKey), posts);
	}

	private List<Post> findAllByMemberId(List<Long> memberIds, CursorRequest request){
		if(request.hasKey())
			return postRepository.findAllByLessThanIdAndInMemberIdsAndOrderByIdDesc(request.key(), memberIds,
				request.size());
		return postRepository.findAllByINMemberIdsAndOrderByIdDesc(memberIds, request.size());

	}

}
