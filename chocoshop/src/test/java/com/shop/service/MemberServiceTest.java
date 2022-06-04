package com.shop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.shop.dto.member.MemberFormDto;
import com.shop.entity.member.Member;
import com.shop.service.member.MemberService;

@SpringBootTest
@Transactional  // 1-1. 테스트 클래스에 트랜잭션 어노테이션이 붙으면 테스트 실행 후 롤백된다.
class MemberServiceTest {

	@Autowired
	MemberService memberService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public Member createMember() {
		MemberFormDto memberFormDto = new MemberFormDto();
		memberFormDto.setEmail("admin@jadecross.com");
		memberFormDto.setName("관리자");
		memberFormDto.setPassword("1234");
		memberFormDto.setAddress("우리집");
		
		return Member.createMember(memberFormDto, passwordEncoder);
	}
	
	@Test
	@DisplayName("회원가입 테스트")
	public void saveMemberTest() {
		Member member = createMember();
		Member savedMember = memberService.saveMember(member);
		
		assertEquals(member.getEmail(), savedMember.getEmail());
		assertEquals(member.getName(), savedMember.getName());
		assertEquals(member.getAddress(), savedMember.getAddress());
		assertEquals(member.getPassword(), savedMember.getPassword());
		assertEquals(member.getRole(), savedMember.getRole());
	}
}
