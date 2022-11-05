package com.tistory.domain.post;

import java.util.List;

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

	@Query(value = "SELECT * FROM post p ORDER BY (SELECT COUNT(*) FROM love l WHERE p.id = l.postId) desc", nativeQuery = true)
	List<Post> findByPopular(Pageable pageable);
	
	@Query(value = "SELECT * FROM post WHERE userId = :userId ORDER BY (SELECT COUNT(*) FROM love WHERE post.id = love.postId) desc", nativeQuery = true)
	Page<Post> mfindByPopular(@Param("userId") Long userId, Pageable pageable);
	
}
