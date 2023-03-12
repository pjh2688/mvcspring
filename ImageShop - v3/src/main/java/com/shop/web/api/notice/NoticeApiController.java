package com.shop.web.api.notice;

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

import com.shop.domain.notice.Notice;
import com.shop.handler.exception.CustomValidationApiException;
import com.shop.service.notice.NoticeService;
import com.shop.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class NoticeApiController {
	
	private final NoticeService noticeService;

	@PostMapping("/notices")
	public ResponseEntity<?> register(@Validated @RequestBody Notice notice, BindingResult bindingResult) throws Exception {
		
		log.info("notice.getTitle() = " + notice.getTitle());
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
				System.out.println(error.getDefaultMessage());
			}
	
			throw new CustomValidationApiException("유효성 검사 실패", errorMap);
		}
		
		noticeService.register(notice);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "공지글 등록하기 성공", null), HttpStatus.OK);
	}
	
	@GetMapping("/notices")
	public ResponseEntity<?> list() throws Exception {
		log.info("list");
		
		List<Notice> result = noticeService.findAll();
		
		log.info("list result = " + result);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "공지글 리스트 불러오기 성공", result), HttpStatus.OK);
	}
	
	@GetMapping("/notices/{noticeNo}")
	public ResponseEntity<?> readByNoticeNo(@PathVariable("noticeNo") Long noticeNo) throws Exception{
		log.info("readByUserNo");
		
		Notice findNotice = noticeService.findByNoticeNo(noticeNo);
		
		log.info("readByNoticeNo = " + findNotice.getNoticeNo());
		
		return new ResponseEntity<>(new CMRespDto<>(1, "공지글 불러오기 성공", findNotice), HttpStatus.OK);
	}
	
	@PutMapping("/notices/{noticeNo}")
	public ResponseEntity<?> modifyByNoticeNo(@PathVariable("noticeNo") Long noticeNo, @Validated @RequestBody Notice notice, BindingResult bindingResult) throws Exception {
		log.info("modify : notice.getNoticeNo() = " + notice.getNoticeNo());
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
				System.out.println(error.getDefaultMessage());
			}
	
			throw new CustomValidationApiException("유효성 검사 실패", errorMap);
		}
		
		notice.setNoticeNo(noticeNo);
		
		int result = noticeService.update(notice);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "공지글 정보 업데이트 성공", result), HttpStatus.OK);
	}
	
	@DeleteMapping("/notices/{noticeNo}")
	public ResponseEntity<?> deleteByNoticeNo(@PathVariable("noticeNo") Long noticeNo) throws Exception {
		log.info("deleteByNoticeNo");
		
		noticeService.delete(noticeNo);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "게시글 삭제 성공", null), HttpStatus.OK);
	}
}
