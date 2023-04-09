package com.shop.config.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shop.domain.member.Member;
import com.shop.mapper.member.IMemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {  // 1-1. 스프링 시큐리티에서 PrincipalDetailsService는 기본적으로 http://localhost:8080/login 요청시 실행된다.
	
	private final IMemberMapper mapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("PrincipalDetailsService.loadUserByUsername 실행됨!!!!");
		
		Optional<Member> memberOp = mapper.findByUserId(username);
		
		if(memberOp.isPresent()) {
			
			Member loginMember = memberOp.get();
			
			return new PrincipalDetails(loginMember);
		} 
		
		return null;
		
	}

}
