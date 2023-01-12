package com.postype.sns.domain.post.repository;

import com.postype.sns.domain.member.model.entity.Member;
import com.postype.sns.domain.post.model.Post;
import com.postype.sns.domain.post.model.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	Page<Post> findAllByMemberId(Long memberId, Pageable pageable);
}
