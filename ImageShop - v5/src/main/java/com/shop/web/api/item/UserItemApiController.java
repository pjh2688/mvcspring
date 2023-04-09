package com.shop.web.api.item;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.config.auth.PrincipalDetails;
import com.shop.domain.item.UserItem;
import com.shop.domain.member.Member;
import com.shop.service.item.UserItemService;
import com.shop.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserItemApiController {

	private final UserItemService userItemService;
	
	@GetMapping("/useritems")
	public ResponseEntity<?> userItemList(@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
		log.info("useritems list");
		
		Member loginMember = principalDetails.getMember();
		
		List<UserItem> result = userItemService.findAllByUserNo(loginMember.getUserNo());
		
		log.info("list result = " + result);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "구매 내역  리스트 불러오기 성공", result), HttpStatus.OK);
	}
	
	@GetMapping("/useritems/{userItemNo}")
	public ResponseEntity<?> userItemByUserItemNo(@PathVariable("userItemNo") Long userItemNo, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
		log.info("useritems list");
		
		UserItem findUserItem = userItemService.findByUserItemNo(userItemNo);
		
		return new ResponseEntity<>(new CMRespDto<>(1, userItemNo + "번 구매 내역 불러오기 성공", findUserItem), HttpStatus.OK);
	}
}
