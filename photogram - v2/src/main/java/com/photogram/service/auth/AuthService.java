package com.photogram.service.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.photogram.domain.user.User;
import com.photogram.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service  // 1-1. Ioc 등록 및 트랜잭션 관리
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User signup(User user) {
	
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		
		user.setPassword(encPassword);
		user.setRole("ROLE_USER");
		
		User userEntity = userRepository.save(user);
		
		return userEntity;
	}
}
