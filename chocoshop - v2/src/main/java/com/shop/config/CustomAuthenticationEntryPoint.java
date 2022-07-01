package com.shop.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	/*
	 * - CustomAuthenticationEntryPoint.class 설명 : ajax 통신시에
 *   -> ajax의 경우 http request header에 XMLHttpRequest라는 값이 세팅되어 요청이 오는데
 *      인증되지 않은 사용자가 ajax로 리소스를 요청할 경우 "Unauthorized" 에러를 발생시키고 나머지 경우는 로그인 페이지로 리다이렉트 시켜준다.  
	 */
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		if("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		} else {
			response.sendRedirect("/members/login");
		}
		
	}

}

/*
 *  - 포인트 컷(pointcut)
 *   -> 특정 조건에서 어드바이스(공통 기능의 코드)를 실행하기 위한 알고리즘입니다.
 *   -> 공통 기능을 수행하기 위한 일종의 필터라고 생각하면 됩니다.  
 *   
 *  
 */
