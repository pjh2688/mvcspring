package com.shop.handler.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogoutAuthenticationEntryPoint implements AuthenticationEntryPoint {

	// 1-1. JWT 필터에서 예외가 발생한 경우, 필터의 순서상 Spring의 DispatcherServlet까지 예외가 도달하지 않기 때문에 이를 처리하기위한 Handler가 필요
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		response.sendRedirect("/loginForm");
		
	}

}
