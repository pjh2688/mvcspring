package com.photogram.domain.image;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {

	@Query(value = "SELECT * FROM image WHERE userId IN (SELECT toUserId FROM subscribe WHERE fromUserId = :principalId)", nativeQuery = true)
	List<Image> mStory(@Param(value = "principalId") Long principalId);
	
	@Query(value = "SELECT * FROM image WHERE userId IN (SELECT toUserId FROM subscribe WHERE fromUserId = :principalId)", nativeQuery = true)
	Page<Image> mStory(@Param(value = "principalId") Long principalId, Pageable pageable);
	
	@Query(value = "SELECT i.* FROM image i INNER JOIN (SELECT imageId, COUNT(imageId) AS likeCount FROM likes GROUP BY imageId) c ON i.id = c.imageId ORDER BY likeCount DESC", nativeQuery = true)
	List<Image> mPopular();
}

// SELECT i.* FROM image i INNER JOIN (SELECT imageId, COUNT(imageId) AS likeCount FROM likes GROUP BY imageId) c ON i.id = c.imageId ORDER BY likeCount DESC;


/*
 	SELECT * FROM image;

	SELECT * FROM likes;

	SELECT * FROM image WHERE id IN (2,3);

	SELECT distinct imageId FROM likes;

	SELECT * FROM image WHERE id IN (SELECT distinct imageId FROM likes);

	SELECT imageId, COUNT(imageId) AS likeCount FROM likes GROUP BY imageId ORDER BY likeCount DESC;

	SELECT * FROM image WHERE id IN ();

	SELECT imageId
	FROM (
	SELECT imageId, COUNT(imageId) AS likeCount 
	FROM likes GROUP BY imageId 
	ORDER BY likeCount DESC) c;

	SELECT * FROM image WHERE id IN (SELECT imageId
	FROM (
	SELECT imageId, COUNT(imageId) AS likeCount 
	FROM likes GROUP BY imageId 
	ORDER BY likeCount DESC) c);

	SELECT * FROM image;

	SELECT imageId, COUNT(imageId) AS likeCount 
	FROM likes GROUP BY imageId;

	SELECT i.* FROM image i INNER JOIN (SELECT imageId, COUNT(imageId) AS likeCount FROM likes GROUP BY imageId) c ON i.id = c.imageId ORDER BY likeCount DESC;
 
 */
 