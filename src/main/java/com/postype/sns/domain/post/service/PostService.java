package com.postype.sns.domain.post.service;

import com.postype.sns.application.exception.ErrorCode;
import com.postype.sns.application.exception.MemberException;
import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.member.repository.MemberRepository;
import com.postype.sns.domain.post.model.Post;
import com.postype.sns.domain.post.repository.PostRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService{

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	@Transactional
	public void create(String title, String body, String memberId){
		//user find
		Member foundedMember = memberRepository.findByMemberId(memberId).orElseThrow(() ->
			new MemberException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s not founded", memberId)));
		//post save
		postRepository.save(Post.of(title, body, foundedMember));
	}

}
