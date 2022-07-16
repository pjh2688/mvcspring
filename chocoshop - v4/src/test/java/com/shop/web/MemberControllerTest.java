package com.shop.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.shop.dto.member.MemberFormDto;
import com.shop.entity.member.Member;
import com.shop.service.member.MemberService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc  // 1-1. MockMvc 테스트를 위해 @AutoConfigureMockMvc 어노테이션 추가
@Transactional
class MemberControllerTest {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private MockMvc mockMvc;  // 1-2. MockMvc 클래스를 이용하면 실제 객체와 비슷하지만 테스트에 필요한 기능만 가지는 가짜 객체를 만들 수 있습니다. 또 MockMvc 객체를 이용하면 웹 브라우저에서 요청하는 것처럼 테스트도 가능합니다.
	
	public Member createMember(String email, String password) {
		MemberFormDto memberFormDto = new MemberFormDto();
		memberFormDto.setEmail(email);
		memberFormDto.setName("홍길동");
		memberFormDto.setAddress("서울시 마포구 합정동");
		memberFormDto.setPassword(password);
		
		Member member = Member.createMember(memberFormDto, passwordEncoder);
		
		return memberService.saveMember(member);
	}
	
	@Test
	@DisplayName("로그인 성공 테스트")
	public void loginSuccessTest() throws Exception {
		String email = "test@email.com";
		String password = "1234";
		
		this.createMember(email, password);
		
		mockMvc.perform(formLogin().userParameter("email")
								   .loginProcessingUrl("/members/login")
								   .user(email).password(password)).andExpect(SecurityMockMvcResultMatchers.authenticated());
	}
	
	@Test
	@DisplayName("로그인 실패 테스트")
	public void loginFailTest() throws Exception {
		String email = "test@email.com";
		String password = "1234";
		
		this.createMember(email, password);
		
		mockMvc.perform(formLogin().userParameter("email")
								   .loginProcessingUrl("/members/login")
								   .user(email).password("12345")).andExpect(SecurityMockMvcResultMatchers.unauthenticated());
	}
	

}
