package com.photogram.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

	@Modifying  // 1. INSERT, DELETE, UPDATE를 네이트브 쿼리로 작성하려면 해당 어노테이션을 넣어줘야 한다.
	@Query(value = "INSERT INTO subscribe(fromUserId, toUserId, createDate) VALUES(:fromUserId, :toUserId, now())", nativeQuery = true)
	void mSubscribe(@Param(value = "fromUserId") Long fromUserId, @Param(value = "toUserId") Long toUserId); 

	@Modifying
	@Query(value = "DELETE FROM subscribe WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
	void mUnSubscribe(@Param(value = "fromUserId") Long fromUserId, @Param(value = "toUserId") Long toUserId);
	
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE fromUserId = :principalId AND toUserId = :pageUserId", nativeQuery = true)
	int mSubscribeState(@Param(value = "principalId") Long principalId, @Param(value = "pageUserId") Long pageUserId);
	
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE fromuserId = :pageUserId", nativeQuery = true)
	int mSubscribeCount(@Param(value = "pageUserId") Long pageUserId);
}

