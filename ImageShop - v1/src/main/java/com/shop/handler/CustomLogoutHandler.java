package com.shop.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.shop.config.jwt.service.JwtService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class CustomLogoutHandler implements LogoutHandler {

	private final JwtService jwtService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		jwtService.logout(request, response);
		
	}

}
