package com.tistory.entity.love;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoveRepository extends JpaRepository<Love, Long> {

	@Query(value = "SELECT * FROM love WHERE userId = :userId AND postId = :postId", nativeQuery = true)
    Optional<Love> mFindByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
	
	@Query(value = "SELECT * FROM love WHERE postId = :postId", nativeQuery = true)
    Optional<Love> findByPostId(@Param("postId") Long postId);
	
	@Query(value = "DELETE FROM love WHERE postId = :postId", nativeQuery = true)
	void deleteByPostId(@Param("postId") Long postId);
}
