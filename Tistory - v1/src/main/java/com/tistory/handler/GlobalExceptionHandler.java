package com.tistory.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.tistory.handler.exception.CustomApiException;
import com.tistory.handler.exception.CustomException;
import com.tistory.handler.exception.CustomValidationApiException;
import com.tistory.handler.exception.CustomValidationException;
import com.tistory.util.Script;
import com.tistory.web.dto.CMRespDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice  // 1-1. 모든 Exception을 낚아채는 어노테이션
@RestController
public class GlobalExceptionHandler {

//	@ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(Exception e) { // fetch 요청시 발동
        log.error("에러 발생 : " + e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

//  @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> htmlException(Exception e) { // 일반적인 요청 Get(a태그), Post(form태그) 요청
        log.error("에러 발생 : " + e.getMessage());

        return new ResponseEntity<>(Script.back(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    
 // 3-1. html을 클라이언트에게 return 할때
 	@ExceptionHandler(CustomValidationException.class) 
 	public String validationScriptException(CustomValidationException e) {
 		
 		if(e.getErrorMap() == null) {
 			return Script.back(e.getMessage());
 		} else {
 			return Script.back(e.getErrorMap().toString());
 		}
 	}
    
 // 4-1. api 1 - validation
 	@ExceptionHandler(CustomValidationApiException.class) 
 	public ResponseEntity<?> validationApiException(CustomValidationApiException e) {
 		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);

 	}
 	
 	// 5-1. api 2
 	@ExceptionHandler(CustomApiException.class) 
 	public ResponseEntity<?> apiException(CustomApiException e) {
 		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
 	}
 	
 // 6. CustomException
 	@ExceptionHandler(CustomException.class)
 	public String exception(CustomException e) {
 		return Script.back(e.getMessage());
 	}
 	
 	
}
