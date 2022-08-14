package com.photogram.web.controller.user;

import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${file.path}")
	private String uploadFolder;
	
	private final UserService userService;
	
	@GetMapping("/user/{pageUserId}")
	public String profile(@PathVariable Long pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		
		UserProfileDto userDto = userService.selectProfile(pageUserId, principalDetails.getUser().getId());
		model.addAttribute("userDto", userDto);
		model.addAttribute("principal", principalDetails);
		
		return "user/profile";
	}
	
	@GetMapping("/user/{id}/update")
	public String update(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		// 1. 어노테이션
//		System.out.println("어노테이션으로 찾은 세션 정보 : " + principalDetails.getUser());
		
		// 2. 직접(지연로딩 sysout 조심)
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		PrincipalDetails mPrincipalDetails = (PrincipalDetails)auth.getPrincipal();
//		System.out.println("직접 찿은 세션 정보 : " + mPrincipalDetails.getUser());
		
		model.addAttribute("principal", principalDetails);

		return "user/update";
	}
}