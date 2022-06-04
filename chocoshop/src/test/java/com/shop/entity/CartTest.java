package com.shop.entity;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.shop.dto.member.MemberFormDto;
import com.shop.entity.cart.Cart;
import com.shop.entity.cart.CartRepository;
import com.shop.entity.member.Member;
import com.shop.entity.member.MemberRepository;

@SpringBootTest
@Transactional
class CartTest {

	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PersistenceContext
	EntityManager em;
	
	public Member createMember() {
		MemberFormDto memberFormDto = new MemberFormDto();
		memberFormDto.setEmail("admin@jadecross.com");
		memberFormDto.setName("관리자");
		memberFormDto.setPassword("1234");
		memberFormDto.setAddress("우리집");
		
		return Member.createMember(memberFormDto, passwordEncoder);
	}
	
	
	@Test
	@DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
	public void findCartAndMemberTest() {
		Member member = createMember();
		memberRepository.save(member);
		
		Cart cart = new Cart();
		cart.setMember(member);
		cartRepository.save(cart);
		
		em.flush();
		em.clear();
		
		Cart savedCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new);
		
		assertEquals(savedCart.getMember().getId(), member.getId());
		
	}

}
