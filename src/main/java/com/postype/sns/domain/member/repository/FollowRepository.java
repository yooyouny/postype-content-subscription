package com.postype.sns.domain.member.repository;

import com.postype.sns.domain.member.model.entity.Follow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
	List<Follow> findByFromMemberId(Long id);
	List<Follow> findByToMemberId(Long id);
}
