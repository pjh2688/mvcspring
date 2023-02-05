package com.shop.config.jwt;

public interface JwtProperties {

	String SECRET_KEY = "ca4f77187e7e4f23a7fa2c67c020e4d7";
	Long EXPIRATION_TIME = 1000 * 60L * 60L * 1L;
	String HEADER_STRING = "Authorization";
	String HEADER_REFRESH_STRING = "Authorization-refresh";
	String TOKEN_PREFIX = "Bearer ";
}
