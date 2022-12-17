package com.tistory.entity.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

	@Query(value = "SELECT * FROM post WHERE userId = :userId ORDER BY id DESC", nativeQuery = true)
    Page<Post> findByUserId(@Param("userId") Long userId, Pageable pageable);
	
	@Query(value = "SELECT * FROM post WHERE userId = :userId AND categoryId = :categoryId ORDER BY id DESC", nativeQuery = true)
    Page<Post> findByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId, Pageable pageable);

	@Query(value = "SELECT * FROM post WHERE userId = :userId ORDER BY (SELECT COUNT(*) FROM love WHERE post.id = love.postId) desc", nativeQuery = true)
	Page<Post> findByPopular(@Param("userId") Long userId, Pageable pageable);
	
	@Query(value = "SELECT * FROM post ORDER BY (SELECT COUNT(*) FROM love WHERE love.postId = post.id) desc", nativeQuery = true)
	Page<Post> findByPopularAll(Pageable pageable);
}
