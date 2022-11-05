package com.tistory.service.auth;

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
		User userEntity = userRepository.findByUsername(username);
				
		// 1-3. 비밀번호는 스프링 시큐리티에서 알아서 처리해준다.
		if(userEntity == null) {
			return null;
		} else {
			// 1-4. 해당 userEntity가 존재하면 PrincipalDetails를 Authentication 객체안에 넣어서 반환하면서 HttpSession 안에 있는 SecurityContextHolder 안에 Authentication 객체를 넣는다. (방법1)  
			return new PrincipalDetails(userEntity);
		}

	}

}
