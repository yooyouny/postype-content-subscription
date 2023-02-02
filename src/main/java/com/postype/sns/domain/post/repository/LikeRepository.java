package com.postype.sns.domain.post.repository;

import com.postype.sns.domain.post.model.Like;
import com.postype.sns.domain.member.model.Member;
import com.postype.sns.domain.post.model.Post;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

	Optional<Like> findByMemberAndPost(Member member, Post post);

	//@Query(nativeQuery = true, value="SELECT COUNT(*) from likes as l where l.post_id = :postId ")
	//Integer countByPostLike(@Param("postId") Long postId);

	long countByPost(Post post);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE likes entity SET deleted_at = NOW() WHERE entity.post_id = :post")
	void deleteAllByPost(@Param("post") Post post);
	List<Like> findAllByMember(Member member);

}
