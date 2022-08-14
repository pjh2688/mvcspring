package com.photogram.web.api.subscribe;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photogram.config.auth.PrincipalDetails;
import com.photogram.dto.CMRespDto;
import com.photogram.service.subscribe.SubscribeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subscribe/")
@RequiredArgsConstructor
public class SubscribeApiController {
	
	private final SubscribeService subScribeService;

	@PostMapping("{toUserId}")
	public ResponseEntity<?> subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("toUserId") Long toUserId) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			subScribeService.subscribe(principalDetails.getUser().getId(), toUserId);
			
			result.put("toUserId", toUserId);
			result.put("message", "success");
			result.put("code", HttpStatus.OK);
			
		} catch (Exception e) {
			result.put("message", "fail");
			result.put("code", HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(new CMRespDto<>(1, "구독 성공", result), HttpStatus.OK);
	}
	
	@DeleteMapping("{toUserId}")
	public ResponseEntity<?> unSubscribe(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("toUserId") Long toUserId) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			subScribeService.unSubscribe(principalDetails.getUser().getId(), toUserId);
			
			result.put("code", HttpStatus.OK);
			result.put("toUserId", toUserId);
			result.put("message", "success");
			
		} catch (Exception e) {
			result.put("code", HttpStatus.BAD_REQUEST);
			result.put("message", "fail");
		}
		
		return new ResponseEntity<>(new CMRespDto<>(1, "구독 취소 성공", result), HttpStatus.OK);
	}
}
