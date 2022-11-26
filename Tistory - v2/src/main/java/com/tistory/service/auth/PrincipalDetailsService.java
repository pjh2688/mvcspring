package com.tistory.service.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tistory.domain.user.User;
import com.tistory.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service  // 1-1. IoC 등록 -> 기존에 등록된 UserDetailsService를 재정의하여 PrincipalDetailsService를 실행하게 한다.
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// 1-2. username 비교를 위해 DB에서 해당 username 가져오기
		Optional<User> userOp = userRepository.findByUsername(username);
				
		// 1-3. 비밀번호는 스프링 시큐리티에서 알아서 처리해준다.
		if (userOp.isPresent()) {
			return new PrincipalDetails(userOp.get());
		} 
		
		return null;

	}

}
