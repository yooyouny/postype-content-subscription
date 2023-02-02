package com.postype.sns.domain.post.service;

import static java.util.stream.Collectors.toList;

import com.postype.sns.application.contoller.dto.AlarmDto;
import com.postype.sns.application.contoller.dto.CommentDto;
import com.postype.sns.application.contoller.dto.MemberDto;
import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.application.exception.ApplicationException;
import com.postype.sns.domain.member.model.Alarm;
import com.postype.sns.domain.member.model.AlarmArgs;
import com.postype.sns.domain.member.model.AlarmEvent;
import com.postype.sns.domain.member.model.AlarmType;
import com.postype.sns.domain.member.model.Follow;
import com.postype.sns.domain.member.repository.AlarmRepository;
import com.postype.sns.domain.member.repository.FollowRepository;
import com.postype.sns.domain.member.service.AlarmService;
import com.postype.sns.domain.post.model.Comment;
import com.postype.sns.domain.post.model.Like;
import com.postype.sns.domain.post.repository.CommentRepository;
import com.postype.sns.domain.post.repository.LikeRepository;
import com.postype.sns.domain.member.model.Member;
import com.postype.sns.domain.member.model.util.CursorRequest;
import com.postype.sns.domain.member.model.util.PageCursor;
import com.postype.sns.domain.member.repository.MemberRepository;
import com.postype.sns.domain.post.model.Post;
import com.postype.sns.application.contoller.dto.PostDto;
import com.postype.sns.domain.post.repository.PostRepository;
import com.postype.sns.producer.AlarmProducer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService{

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final FollowRepository followRepository;
	private final LikeRepository likeRepository;
	private final CommentRepository commentRepository;
	private final AlarmProducer alarmProducer;


	@Transactional
	public Long create(String title, String body, MemberDto memberDto, int price){
		//user find
		Member writer = Member.toDto(memberDto);
		//post save
		Long savedPostId = postRepository.save(Post.of(title, body, writer, price)).getId();
		//Member following writer
		List<Member> followingList = followRepository.findAllByToMemberId(writer.getId()).stream().map(
			Follow::getFromMember).toList();

		for(int i=0; i< followingList.size(); i++) {
			Member followingMember = followingList.get(i);
			alarmProducer.send(new AlarmEvent(followingMember.getId(), //알람을 받는 작성자를 팔로우 하고 있는 사람들
				AlarmType.NEW_POST_ON_SUBSCRIBER,
				new AlarmArgs(writer.getId(), //알람을 발생시킨 포스트의 작성자
					"Post", savedPostId)//알람을 발생시킨 포스트의 아이디
				)
			);
		}
		return savedPostId;
	}

	@Transactional
	public PostDto modify(String title, String body, MemberDto memberDto, Long postId){
		//user find
		Member foundedMember = Member.toDto(memberDto);
		//post exist
		Post post = getPostOrException(postId);

		//post permission
		if(post.getMember() != foundedMember){
			throw new ApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", foundedMember.getMemberId(), postId));
		}

		post.setTitle(title);
		post.setBody(body);
		return PostDto.fromPost(postRepository.saveAndFlush(post));
	}
	@Transactional
	public void delete(MemberDto memberDto, Long postId){
		//user find
		Member foundedMember = Member.toDto(memberDto);
		Post post = getPostOrException(postId);

		if(post.getMember() != foundedMember){
			throw new ApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", foundedMember.getMemberId(), postId));
		}
		likeRepository.deleteAllByPost(post);
		commentRepository.deleteAllByPost(post);
		postRepository.delete(post);
	}

	public Page<PostDto> getList (Pageable pageable){
		return postRepository.findAll(pageable).map(PostDto::fromPost);
	}

	//내가 쓴 글 읽기
	public Page<PostDto> getMyPostList (MemberDto memberDto, Pageable pageable){
		return postRepository.findAllByMemberId(memberDto.getId(), pageable).map(PostDto::fromPost);
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
	public void like(Long postId, MemberDto memberDto){
		Post post = getPostOrException(postId);
		Member member = Member.toDto(memberDto);

		//check like
		likeRepository.findByMemberAndPost(member, post).ifPresent(it -> {
			throw new ApplicationException(ErrorCode.ALREADY_LIKE,
				String.format("memberName %s already like post %d", member.getMemberId(), postId));
		});

		//like save
		likeRepository.save(Like.of(member, post));

		alarmProducer.send(new AlarmEvent(post.getMember().getId(), //알람을 받는 포스트 작성자에게 알람 전송
				AlarmType.NEW_LIKE_ON_POST,
				new AlarmArgs(member.getId(), // 알람을 발생시킨 좋아요를 누른 사람의 아이디
					"Like", post.getId())// 알람이 발생한 포스트의 아이디
			)
		);
	}

	public long getLikeCount(Long postId) {
		Post post = getPostOrException(postId);
		return likeRepository.countByPost(post);
	}

	public Page<PostDto> getLikeByMember(MemberDto memberDto, Pageable pageable) {
		Member member = Member.toDto(memberDto);

		List<Like> likedList = likeRepository.findAllByMember(member);
		List<Post> postList = likedList.stream().map(Like::getPost).toList();

		return postRepository.findAllByMember(postList.stream().map(Post::getId).toList(), pageable).map(PostDto::fromPost);
	}

	@Transactional
	public void comment(Long postId, MemberDto memberDto, String comment){
		Member member = Member.toDto(memberDto);
		Post post = getPostOrException(postId);

		//comment save
		Comment cmt = commentRepository.save(Comment.of(member, post, comment));

		alarmProducer.send(new AlarmEvent(post.getMember().getId(), //알람을 받는 포스트 작성자에게 알람 전송
			AlarmType.NEW_COMMENT_ON_POST,
			new AlarmArgs(member.getId(), //알람을 발생시킨 코멘트를 작성한 사람의 아이디
				"Comment", cmt.getId())//알람이 발생한 코멘트의 아이디
			)
		);

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
