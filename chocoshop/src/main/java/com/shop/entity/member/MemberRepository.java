package com.shop.entity.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>{

	// 1-1. 아이디 중복검사를 위한 쿼리메소드
	Member findByEmail(String email);
}
