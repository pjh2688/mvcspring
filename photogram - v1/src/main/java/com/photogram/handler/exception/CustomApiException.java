package com.photogram.handler.exception;

import java.util.Map;

public class CustomApiException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CustomApiException(String message) {
		super(message);
	}
	
	public CustomApiException(String message, Map<String, String> errorMap) {
		super(message);
	}

}
