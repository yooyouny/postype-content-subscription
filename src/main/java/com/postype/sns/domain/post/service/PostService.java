package com.postype.sns.domain.post.service;

import com.postype.sns.application.contoller.dto.CommentDto;
import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.application.exception.ApplicationException;
import com.postype.sns.domain.post.model.Comment;
import com.postype.sns.domain.post.model.Like;
import com.postype.sns.domain.post.repository.CommentRepository;
import com.postype.sns.domain.post.repository.LikeRepository;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.model.util.CursorRequest;
import com.postype.sns.domain.member.model.util.PageCursor;
import com.postype.sns.domain.member.repository.MemberRepository;
import com.postype.sns.domain.post.model.Post;
import com.postype.sns.application.contoller.dto.PostDto;
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
	private final LikeRepository likeRepository;
	private final CommentRepository commentRepository;

	@Transactional
	public Long create(String title, String body, String memberId, int price){
		//user find
		Member foundedMember = getMemberOrException(memberId);
		//post save
		return postRepository.save(Post.of(title, body, foundedMember, price)).getId();
	}

	@Transactional
	public PostDto modify(String title, String body, String memberId, Long postId){
		//user find
		Member foundedMember = getMemberOrException(memberId);
		//post exist
		Post post = getPostOrException(postId);

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
		Member foundedMember = getMemberOrException(memberId);
		Post post = getPostOrException(postId);

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

		Member member = getMemberOrException(memberId);
		return postRepository.findAllByMemberId(member.getId(), pageable).map(PostDto::fromPost);
	}

	public List<Post> getPostsByIds(List<Long> ids){
		return postRepository.findAllByInId(ids);
	}
	public PostDto getPost(Long id) {
		return postRepository.findById(id).map(PostDto::fromPost).orElseThrow(() ->
		new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", id)));
	}

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

	@Transactional
	public void like(Long postId, String memberId){
		Member member = getMemberOrException(memberId);
		Post post = getPostOrException(postId);

		//check like
		likeRepository.findByMemberAndPost(member, post).ifPresent(it -> {
			throw new ApplicationException(ErrorCode.ALREADY_LIKE,
				String.format("memberName %s already like post %d", memberId, postId));
		});
		likeRepository.save(Like.of(member, post));
	}

	public int getLikeCount(Long postId) {
		Post post = getPostOrException(postId);

		return likeRepository.countByPost(post.getId());
	}

	public Page<PostDto> getLikeByMember(String memberId, Pageable pageable) {
		Member member = getMemberOrException(memberId);

		List<Like> likedList = likeRepository.findAllByMember(member);
		List<Post> postList = likedList.stream().map(Like::getPost).toList();

		return postRepository.findAllByMember(postList.stream().map(Post::getId).toList(), pageable).map(PostDto::fromPost);
	}

	@Transactional
	public void comment(Long postId, String memberId, String comment){
		Member member = getMemberOrException(memberId);
		Post post = getPostOrException(postId);

		//comment save
		commentRepository.save(Comment.of(member, post, comment));
	}

	public Page<CommentDto> getComment(Long postId, Pageable pageable){
		Post post = getPostOrException(postId);
		return commentRepository.findAllByPost(post, pageable).map(CommentDto::fromEntity);
	}
	private Post getPostOrException(Long postId){
		return postRepository.findById(postId).orElseThrow(() ->
			new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
	}
	private Member getMemberOrException(String memberId){
		return memberRepository.findByMemberId(memberId).orElseThrow(() ->
			new ApplicationException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s not founded", memberId)));
	}
}
