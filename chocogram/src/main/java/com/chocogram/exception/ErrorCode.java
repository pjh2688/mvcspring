package com.chocogram.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorCode {

	/**
	 * 1. 400 BAD_REQUEST : 잘못된 요청 
	 **/
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	
	/**
	 * 2. 404 BAD_REQUEST : 리소스를 찾을 수 없음 
	 **/
	POSTS_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글 정보를 찾을 수 없습니다."),
	
	/**
	 * 3. 405 METHOD_NOT_ALLOWED : 허용되지 않은 Request Method 허용 
	 **/
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않는 메소드입니다."),
	
	/**
	 * 4. 500 INTERNAL_SERVER_ERROR : 내부 서버 오류
	 **/
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
	
	;
	
	private final HttpStatus status;
	
	private final String message;
	
}
