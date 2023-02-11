package com.shop.web.api.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

import com.shop.domain.codelabel.CodeLabelValue;
import com.shop.domain.member.Member;
import com.shop.handler.exception.CustomValidationApiException;
import com.shop.service.member.MemberService;
import com.shop.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class MemberApiController {
	
	private final MemberService memberService;
	
	@PostMapping("/users/setup")
	public ResponseEntity<?> setupAdmin(@Validated @RequestBody Member member, BindingResult bindingResult) throws Exception {
		log.info("setupAdmin : member.getUserName() = " + member.getUserName());
		
		int result = memberService.setupAdmin(member);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "관리자 등록하기 성공", result), HttpStatus.OK);
	}

	@GetMapping("/codes/job")
	public ResponseEntity<?> selectJobList() throws Exception {
		log.info("selectCodeGroup");
	
		String groupCode = "A01";  // A01 그롭코드가 직업 데이터를 저장하는 코드
		List<CodeLabelValue> result = memberService.getCodeGroupList(groupCode);
		
		log.info("selectCodeGroup");
		
		return new ResponseEntity<>(new CMRespDto<>(1, "코드그룹리스트 불러오기 성공", result), HttpStatus.OK);
	}
	
	@PostMapping("/users")
	public ResponseEntity<?> register(@Validated @RequestBody Member member, BindingResult bindingResult) throws Exception {
		
		log.info("member.getUsername() = " + member.getUserName());
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
				System.out.println(error.getDefaultMessage());
			}
	
			throw new CustomValidationApiException("유효성 검사 실패", errorMap);
		}
		
		int result = memberService.register(member);
	
		return new ResponseEntity<>(new CMRespDto<>(1, "멤버 등록하기 성공", result), HttpStatus.OK);
	}
	
	@GetMapping("/users")
	public ResponseEntity<?> list(Authentication authentication) throws Exception {
		log.info("list");
		
		List<Member> result = memberService.findAll();
		
		log.info("list result = " + result);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "멤버 리스트 불러오기 성공", result), HttpStatus.OK);
	}
	
	@GetMapping("/users/{userNo}")
	public ResponseEntity<?> readByUserNo(@PathVariable("userNo") Long userNo) throws Exception {
		log.info("readByUserNo");
		
		Member findMember = memberService.findByUserNo(userNo);
		
		log.info("readByUserNo user = " + findMember);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "유저 불러오기 성공 성공", findMember), HttpStatus.OK);
	}
	
	@PutMapping("/users/{userNo}")
	public ResponseEntity<?> modifyByUserNo(@PathVariable("userNo") Long userNo, @Validated @RequestBody Member member) throws Exception {
		log.info("modify : member.getUserName() = " + member.getUserName());
		log.info("modify : userNo = " + member.getUserNo());
		
		member.setUserNo(userNo);
		
		int result = memberService.update(member);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "유저 정보 업데이트 성공", result), HttpStatus.OK);
	}
	
	@DeleteMapping("/users/{userNo}")
	public ResponseEntity<?> deleteByUserNo(@PathVariable("userNo") Long userNo) throws Exception {
		log.info("Delete");
		
		int result = memberService.delete(userNo);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "유저 삭제 성공", result), HttpStatus.OK);
	}
}
