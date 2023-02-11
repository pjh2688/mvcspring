package com.shop.web.api.codegroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.domain.codegroup.CodeGroup;
import com.shop.handler.exception.CustomValidationApiException;
import com.shop.service.codegroup.CodeGroupService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class CodeGroupApiController {

	private final CodeGroupService service;
	
	@PostMapping("/codegroups")
	public ResponseEntity<?> register(@Validated @RequestBody CodeGroup codeGroup, BindingResult bindingResult) throws Exception {
		log.info("register");
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
				System.out.println(error.getDefaultMessage());
			}
	
			throw new CustomValidationApiException("유효성 검사 실패", errorMap);
		}
		
		int result = service.register(codeGroup);
		
		log.info("register codeGroup.getGroupCode() = " + codeGroup.getGroupCode());
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/codegroups")
	public ResponseEntity<?> list() throws Exception {
		log.info("list");
		
		List<CodeGroup> result = service.findAll();
		
		log.info("list result = " + result);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/codegroups/{groupCode}")
	public ResponseEntity<?> readByGroupCode(@PathVariable("groupCode") String groupCode) throws Exception {
		log.info("readByGroupCode");
		
		CodeGroup codeGroup = service.findByGroupCode(groupCode);
		
		log.info("codeGroup result = " + codeGroup);
		
		return new ResponseEntity<>(codeGroup, HttpStatus.OK);
	}
	
	// 2022-12-25 -> 여기까지(수정API) : 1878p
	@PutMapping("/codegroups/{groupCode}")
	public ResponseEntity<?> modifyByGroupCode(@PathVariable("groupCode") String groupCode, @Validated @RequestBody CodeGroup codeGroup, BindingResult bindingResult) throws Exception {
		log.info("modify");
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
				System.out.println(error.getDefaultMessage());
			}
	
			throw new CustomValidationApiException("유효성 검사 실패", errorMap);
		}
		
		codeGroup.setGroupCode(groupCode);
		
		int result = service.update(codeGroup);
		
		log.info("modify");
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@DeleteMapping("/codegroups/{groupCode}")
	public ResponseEntity<?> deleteByGroupCode(@PathVariable("groupCode") String groupCode) throws Exception {
		log.info("delete");
		
		int result = service.delete(groupCode);
		
		log.info("delete");
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
