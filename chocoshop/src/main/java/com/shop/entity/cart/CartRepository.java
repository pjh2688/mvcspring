package com.shop.entity.cart;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

	// 1-1. 현재 로그인한 회원의 Cart 엔티티를 찾기 위해 추가
	Cart findByMemberId(Long memberId);
}
