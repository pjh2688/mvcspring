package com.photogram.web.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.photogram.config.auth.PrincipalDetails;
import com.photogram.dto.user.UserProfileDto;
import com.photogram.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/user/{pageUserId}") 
	public String profile(@PathVariable Long pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		UserProfileDto userProfileDto = userService.userProfile(pageUserId, principalDetails.getUser().getId());
		
		// 1-1. 해당 페이지 주인의 user 엔티티.
		model.addAttribute("dto", userProfileDto);
		
		// 1-2. 로그인한 회원의 principalDetails 객체.
		model.addAttribute("principal", principalDetails.getUser());
		
		return "user/profile";
	}
	
	@GetMapping("/user/update")
	public String update(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {  // 1-3. 어노테이션 사용해서 세션에 접근하는 방법 : SecurityContextHolder에 바로 접근할 수 있는 @AuthenticationPrincipal 어노테이션 
//		System.out.println("어노테이션 사용해서 찾은 세션 정보 : " + principalDetails.getUser());
		
		// 1-4. 어노테이션 사용 안하고 세션에 접근하는 방법.
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();	
//		PrincipalDetails mPrincipalDetails = (PrincipalDetails) auth.getPrincipal();
//		System.out.println("직접 찾은 세션 정보 : " + mPrincipalDetails.getUser());
		
		model.addAttribute("principal", principalDetails.getUser());
	
		return "user/update";
	}
	
}