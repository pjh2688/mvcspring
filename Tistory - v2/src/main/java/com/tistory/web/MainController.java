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
import com.tistory.web.dto.post.PostMainRespDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	private final PostService postService;

	@GetMapping({"/", "/main"})
	public String home(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model, @PageableDefault(size = 3) Pageable pageable) {
		
		User loginUser = null;
		
		if(principalDetails == null) {  	// 1-1. 등록된 회원이 아닌 anonymous
			
			// 1-5. 전체 회원들의 포스팅 게시글들을 인기순으로
			PostMainRespDto postMainRespDto = postService.viewPostByPopular(pageable);
			
			model.addAttribute("principal", null);
			model.addAttribute("postMainRespDto", postMainRespDto);
			
		} else {  // 1-2. 회원인 경우
			loginUser = principalDetails.getUser();
			
			// 1-4. 로그인한 회원의 포스팅 게시글들을 인기순으로
			PostMainRespDto postMainRespDto = postService.viewPostByPopular(loginUser, pageable);
			
			model.addAttribute("principal", loginUser);
			// 1-3. admin 여부 
			model.addAttribute("isAdmin", principalDetails.getUser().getUsername().equals("admin") ? true : false);
			model.addAttribute("postMainRespDto", postMainRespDto);
		}
				
		return "main";
	}
}	
