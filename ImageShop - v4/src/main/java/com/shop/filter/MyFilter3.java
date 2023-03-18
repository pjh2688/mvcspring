package com.shop.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter3 implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
//		chain.doFilter(request, response);  // 1-1. 계속해서 프로세스를 실행하라.
		
		// 1-2. HttpServletRequest, HttpServletResponse 형태로 request를 변환해 저장.
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		// 1-5. id, pw가 정상적으로 들어와서 로그인이 정상적으로 완료가 되면 토큰을 만들어주고 그걸 응답해 준다.
		//  ex) Request가 올때마다 header에 Authorization에 value 값으로 토큰이 들어온다.
		//      그때 넘어온 토큰이 내가 만든 토큰이 맞는지만 검증하면 된다.(RSA, HS256)
		if(req.getMethod().equals("POST")) {
			System.out.println("POST 요청됨");
			String headerAuth = req.getHeader("Authorization");
			System.out.println(headerAuth);
			System.out.println("필터3");
			
			if(headerAuth.equals("cos")) {
				// 1-3. HttpServletRequest, HttpServletResponse로 변형된 데이터로 진행.
				chain.doFilter(req, res);
			
			} else {
				// 1-4. HttpServletResponse 객체의 UTF-8 인코딩 설정(안해주면 한글은 ???으로 뜸)
				res.setCharacterEncoding("UTF-8");
				res.setContentType("text/html; charset=UTF-8");
				
				PrintWriter out = res.getWriter();
				
				out.println("인증되지 않음.");
			}
		}
	}
}
