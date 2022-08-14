package com.photogram.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.photogram.dto.CMRespDto;
import com.photogram.handler.exception.CustomApiException;
import com.photogram.handler.exception.CustomException;
import com.photogram.handler.exception.CustomValidationApiException;
//import com.photogram.dto.CMRespDto;
import com.photogram.handler.exception.CustomValidationException;
import com.photogram.util.Script;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {
	
	// CMRespDto, Script 비교
	// 1. 클라이언트에게 응답할 때는 Script가 좋음.
	// 2. Ajax 통신할때는 CMRespDto가 좋음.
	// 3. Android 통신할때도 CMRespDto 가 좋음.
		
	// 1. 스크립트 리턴
	@ExceptionHandler(CustomValidationException.class)
	public String validationException(CustomValidationException e) {		
		// 2022-05-05 여기까지 함
		if(e.getErrorMap() == null) {
			return Script.back(e.getMessage());
		} else {
			return Script.back(e.getErrorMap().toString());
		}
		
	}
	
	// 2,3. 데이터 리턴
	@ExceptionHandler(CustomValidationApiException.class)
	public ResponseEntity<?> validationApiException(CustomValidationApiException e) {		
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
	}
	
	// 4. customApiExceptionHandler
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> customApiException(CustomApiException e) {		
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	// 5. customException
	@ExceptionHandler(CustomException.class)
	public String customException(CustomException e) {		
		return Script.back(e.getMessage());	
	}
}
