package com.shop.web.api.codedetail;

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

import com.shop.domain.codedetail.CodeDetail;
import com.shop.domain.codelabel.CodeLabelValue;
import com.shop.handler.exception.CustomValidationApiException;
import com.shop.service.codedetail.CodeDetailService;
import com.shop.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class CodeDetailApiControlller {

	private final CodeDetailService codeDetailService;
	
	@GetMapping("/codes/codegroup")
	public ResponseEntity<?> selectCodeGroup() throws Exception {
		log.info("selectCodeGroup");
		
		List<CodeLabelValue> result = codeDetailService.getCodeGroupList();
		
		log.info("selectCodeGroup");
		
		return new ResponseEntity<>(new CMRespDto<>(1, "코드그룹리스트 불러오기 성공", result), HttpStatus.OK);
	}
	

	@GetMapping("/codedetails")
	public ResponseEntity<?> list() throws Exception {
		log.info("list");
		
		List<CodeDetail> result = codeDetailService.findAll();
		
		log.info("list result = " + result);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "코드디테일 리스트 불러오기 성공", result), HttpStatus.OK);
	}
	
	@PostMapping("/codedetails")
	public ResponseEntity<?> register(@Validated @RequestBody CodeDetail codeDetail, BindingResult bindingResult) throws Exception {
		log.info("register");
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
				System.out.println(error.getDefaultMessage());
			}
	
			throw new CustomValidationApiException("유효성 검사 실패", errorMap);
		}
		
		int result = codeDetailService.register(codeDetail);
		
		log.info("register codeDetail.getGroupCode() = " + codeDetail.getGroupCode() + ", codeDetail.getCodeValue() = " + codeDetail.getCodeValue() + ", codeDetail.getCodeName() = " + codeDetail.getCodeName());
		
		return new ResponseEntity<>(new CMRespDto<>(result, "코드디테일 등록 성공", null), HttpStatus.OK);
	}
	
	@GetMapping("/codedetails/{groupCode}/{codeValue}")
	public ResponseEntity<?> readByCodeValue(@PathVariable("groupCode") String groupCode, @PathVariable("codeValue") String codeValue) throws Exception {
		log.info("readByCodeValue");
		
		CodeDetail codeDetail = new CodeDetail();
		codeDetail.setGroupCode(groupCode);
		codeDetail.setCodeValue(codeValue);
				
		CodeDetail result = codeDetailService.findByGroypCodeAndCodeValue(codeDetail);
		
		log.info("codeName result = " + codeDetail.getCodeName());
		
		return new ResponseEntity<>(new CMRespDto<>(1, "코드디테일 불러오기 성공", result), HttpStatus.OK);
	}
	
	@DeleteMapping("/codedetails/{groupCode}/{codeValue}")
	public ResponseEntity<?> deleteByCodeValue(@PathVariable("groupCode") String groupCode, @PathVariable("codeValue") String codeValue) throws Exception {
		log.info("codedetail delete start");
		
		CodeDetail codeDetail = new CodeDetail();
		codeDetail.setGroupCode(groupCode);
		codeDetail.setCodeValue(codeValue);
		
		int result = codeDetailService.delete(codeDetail);
		
		log.info("codedetail delete end");
		
		return new ResponseEntity<>(new CMRespDto<>(1, "코드디테일 정보 삭제 성공", result), HttpStatus.OK);
	}
	
	@PutMapping("/codedetails/{groupCode}/{codeValue}")
	public ResponseEntity<?> modifyByCodeValue(@PathVariable("groupCode") String groupCode, @PathVariable("codeValue") String codeValue, @Validated @RequestBody CodeDetail codeDetail, BindingResult bindingResult) throws Exception {
		log.info("codedetail modify start");
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
				System.out.println(error.getDefaultMessage());
			}
	
			throw new CustomValidationApiException("유효성 검사 실패", errorMap);
		}
		
		codeDetail.setGroupCode(groupCode);
		codeDetail.setCodeValue(codeValue);
		
		int result = codeDetailService.update(codeDetail);
		
		log.info("codedetail modify end");
		
		return new ResponseEntity<>(new CMRespDto<>(1, "코드디테일 정보 수정 성공", result), HttpStatus.OK);
	}
	
	// 2022-12-28 -> 코드 디테일 CRUD까지 했음
}
