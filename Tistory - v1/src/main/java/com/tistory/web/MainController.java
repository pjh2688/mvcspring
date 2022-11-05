package com.tistory.web;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tistory.domain.user.User;
import com.tistory.service.auth.PrincipalDetails;
import com.tistory.service.post.PostService;
import com.tistory.web.dto.post.MainPostDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	private final PostService postService;

	@GetMapping({"/", "/main"})
	public String home(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model, @PageableDefault(size = 3) Pageable pageable) {
		
		User loginUser = null;
		
		if(principalDetails == null) {
			model.addAttribute("principal", null);
			
			// 1-1. 로그인 유저가 아닐때 main 페이지 꾸밀요소 실어서 보내야한다.
			
		} else {
			loginUser = principalDetails.getUser();
			MainPostDto mainPostDto = postService.viewPostByPopular(loginUser, pageable);
			
			model.addAttribute("principal", loginUser);
			// 1-3. admin 여부 
			model.addAttribute("isAdmin", principalDetails.getUser().getUsername().equals("admin") ? true : false);
			model.addAttribute("mainPostDto", mainPostDto);
		}
				
		return "main";
	}
	
	// 2022-11-04 -> 여기까지
}
