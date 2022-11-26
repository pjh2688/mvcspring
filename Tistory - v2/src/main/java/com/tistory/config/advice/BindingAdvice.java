package com.tistory.config.advice;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.tistory.handler.exception.CustomValidationApiException;
import com.tistory.handler.exception.CustomValidationException;

@Aspect
@Component
public class BindingAdvice {

//	@Before("execution(* com.tistory.web.*Controller.*(..))")
	public void before() {
		System.out.println("AOP 실험 시작");
	}
	
//	@After("execution(* com.tistory.web.*Controller.*(..))")
	public void after() {
		System.out.println("AOP 실험 종료");
	}
	
	@Around("execution(* com.tistory.web.*Controller.*(..))")
	public Object commonAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		Object[] args = proceedingJoinPoint.getArgs();
		
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						System.out.println(error.getDefaultMessage());
						
						// 2022-11-24 -> throw하는 부분을 for문 안쪽으로 옮김
						throw new CustomValidationException(error.getDefaultMessage(), errorMap);
					}	
				}
			}
		}
		
		return proceedingJoinPoint.proceed();
	}
	
	@Around("execution(* com.tistory.web.api.*.*ApiController.*(..))")
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		Object[] args = proceedingJoinPoint.getArgs();
		
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						
						// 1-1. 파일은 json형태에서 string으로 변환이 안되기 때문에 따로 메시지를 만들어서 넣어준다.
						if(error.getField().matches("thumnailFile")) {
							errorMap.put(error.getField(), "썸네일 이미지를 첨부해주세요.");
						} else {

							errorMap.put(error.getField(), error.getDefaultMessage());
							System.out.println(error.getDefaultMessage());
						}
						
					}
			
					throw new CustomValidationApiException("유효성 검사 실패", errorMap);
				}
			}
		}
		
		return proceedingJoinPoint.proceed();
	}
}
