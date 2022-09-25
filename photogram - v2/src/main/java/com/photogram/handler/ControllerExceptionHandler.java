package com.photogram.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.photogram.dto.CMRespDto;
import com.photogram.handler.exception.CustomApiException;
import com.photogram.handler.exception.CustomException;
import com.photogram.handler.exception.CustomValidationApiException;
import com.photogram.handler.exception.CustomValidationException;
import com.photogram.util.Script;

@ControllerAdvice  // 1-1. 모든 Exception을 낚아채는 어노테이션
@RestController  // 1-2. 응답 처리를 위해 데이터를 return 하기 때문에 RestController 선언
public class ControllerExceptionHandler {

	
	@ExceptionHandler(RuntimeException.class)  // 1-3. RuntimeException을 낚아채 처리하는 핸들러
	public String customRuntimeException(RuntimeException e) {
		return e.getMessage();  // 1-4. RestController이기 때문에 데이터를 return
	}
	
	// 1-5. 
//	@ExceptionHandler(CustomValidationException.class) 
	public Map<String, String> validationException(CustomValidationException e) {
		return e.getErrorMap();  
	}
	
	// 2-1. DTO를 이용해서 Map과 String 동시에 return -> 서버한테 응답할때
//	@ExceptionHandler(CustomValidationException.class) 
	public CMRespDto<?> validationDtoException(CustomValidationException e) {
		return new CMRespDto<Map<String, String>>(-1, e.getMessage(), e.getErrorMap());  
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
