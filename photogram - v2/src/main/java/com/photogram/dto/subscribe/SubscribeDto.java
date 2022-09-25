package com.photogram.dto.subscribe;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeDto {
	
	// 주의 : JpaResultMapper 사용시 DTO에
	//   - mariadb는 Long이나 Integer 타입을  BigInteger로 바꿔줘야한다.  

	private BigInteger id;
	private String username;
	private String profileImageUrl;
	
	// 집에선 BigInteger, 질병청에선 Integer로
	private BigInteger subscribeState;  
	private BigInteger equalUserState;
	
}

/*
 *  - 스칼라 서브 쿼리 만드는 과정
 *  로그인(1), 화면(1,3)
	1-1. SELECT * FROM subscribe;
 
	1-2. SELECT * FROM user;

	1-3. SELECT * FROM subscribe WHERE fromUserId = 2;

	-- 무식한 방법
	2-1. SELECT * FROM user WHERE id = 1 OR id = 3;

	-- 조인(user.id = subscribe.toUserId)
	2-2. SELECT 
			u.id, u.username, u.profileImageUrl 
		 FROM 
		 	user u INNER JOIN subscribe s ON u.id = s.toUserId
		 WHERE 
		 	s.fromUserId = 2;
	
	-- 로그인(1), 화면(1,3) -> 구독 여부 확인
	3-1. SELECT 
			true 
		 FROM 
		 	subscribe 
		 WHERE 
		 	fromUserId = 1 AND toUserId = 3;

	-- 가상 컬럼 추가
	4-1. SELECT 
			u.id, u.username, u.profileImageUrl, 1 AS subscribeState
		 FROM 
		 	user u INNER JOIN subscribe s ON u.id = s.toUserId
		 WHERE 
		 	s.fromUserId = 2;
	
	-- 스칼라 서브 쿼리 -> select 절에 select가 한 번 더 나오는 쿼리(단일행을 리턴해야 함.)
	5-1. SELECT u.id, u.username, u.profileImageUrl, 
			(SELECT count(*) FROM user) AS subscribeState
		 FROM
			 user u INNER JOIN subscribe s ON u.id = s.toUserId
		 WHERE 
			 s.fromUserId = 2;
	
	-- 구독 여부 완성 쿼리 -> 참고 :  SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = 3;
	6. SELECT 
			u.id, u.username, u.profileImageUrl, 
		    (SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = u.id) AS subscribeState
	   FROM 
			user u INNER JOIN subscribe s ON u.id = s.toUserId
	   WHERE 
			s.fromUserId = 2;
	
	-- 동일 유저인지 판단 쿼리 1
	7. SELECT 
			u.id, u.username, u.profileImageUrl, 
			(SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = u.id) AS subscribeState,
			1 equlaUserState
	   FROM 
			user u INNER JOIN subscribe s ON u.id = s.toUserId
	   WHERE 
			s.fromUserId = 2;
	
	-- 동일 유저인지 판단 쿼리 2 - 최종 -> 여기서 1은 login한 유저의 id
	8. SELECT
	 		u.id, u.username, u.profileImageUrl, 
			(SELECT TRUE FROM subscribe WHERE fromUserId = 1 AND toUserId = u.id) AS subscribeState,
			(1=u.id) as equalUserState
	   FROM 
	   		user u 
	   INNER JOIN 
			subscribe s ON u.id = s.toUserId
	   WHERE 
			s.fromUserId = 2;
			
	-- 동일 유저인지 판단 쿼리3 - if로 마무리(0과 1만 출력되게)
	9. SELECT u.id, u.username, u.profileImageUrl, 
			if((SELECT 1 FROM subscribe WHERE fromUserId = 1 AND toUserId = u.id), 1, 0) AS subscribeState,
			if((1=u.id), 1, 0) as equalUserState
	   FROM 
	   		user u INNER JOIN subscribe s ON u.id = s.toUserId
	   WHERE
	    	s.fromUserId = 2;


 */