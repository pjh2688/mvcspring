package com.tistory.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.tistory.entity.user.User;
import com.tistory.service.auth.PrincipalDetails;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		// 1-1. 로그인을에 성공한 유저 정보(PrincipalDetails) -> UserDetials를 재정의한 PrincipalDetails를 가져온다.(UserDetails의 몇가지 기능을 끄고 생성자로 가져오기 위해)
		PrincipalDetails loginUser = (PrincipalDetails) authentication.getPrincipal();
	
		// 1-2. PrincipalDetails에서 유저정보를 가져와 User 엔티티에 저장.
        User principal = loginUser.getUser();
 
        // 1-3. 세션 생성
        HttpSession session = request.getSession();
        
        // 1-4. 세션에 1-2번 User 객체를 principal이란 이름으로 싣는다.
        session.setAttribute("principal", principal);
        
        // 1-5. 해당 페이지로 redirect
        response.sendRedirect("/user/" + principal.getId() + "/post");
	}

	
}