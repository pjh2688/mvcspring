package com.photogram.web.controller.auth;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.photogram.domain.user.User;
import com.photogram.dto.auth.SignupDto;
import com.photogram.service.auth.AuthService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@GetMapping("/auth/signin")
	public String signinForm() {
		return "auth/signin";
	}
	
	@GetMapping("/auth/signup")
	public String signupForm() {
		return "auth/signup";
	}
	
	// 1. 스프링 시큐리티 의존성을 넣어주면 CSRF 토큰이 감싸져있어서 403에러페이지가 뜬다. -> 응답해줄때 CSRF 토큰을 심어서 응답해준다.
	@PostMapping("/auth/signup")
	public String signup(@Valid SignupDto params, BindingResult bindingResult) {
		User user = params.toEntity();
		authService.join(user);
		
		return "auth/signin";
	}
}
