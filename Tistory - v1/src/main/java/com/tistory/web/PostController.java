package com.tistory.web;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tistory.domain.user.User;
import com.tistory.service.auth.PrincipalDetails;
import com.tistory.service.post.PostService;
import com.tistory.web.dto.post.PostDetailDto;
import com.tistory.web.dto.post.PostDto;
import com.tistory.web.dto.post.PostWriteDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class PostController {
	
	private final PostService postService;

	@GetMapping("/{pageOwnerId}/post")
	public String postList(@RequestParam(required = false) Long categoryId, @PathVariable Long pageOwnerId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model, @PageableDefault(size = 2) Pageable pageable) {
		
		PostDto postDto = null;
		
		// 1-2. categoryId는 url 파라미터로 붙어오면 값이 매개변수에 전달되서 온다. -> @RequestParam은 안붙여도 되는데 이해를 위해 붙여놓았다.
		if(categoryId == null) {
			postDto = postService.viewPost(pageOwnerId, pageable, principalDetails.getUser());
		} else {
			postDto = postService.viewPostByCategoryId(pageOwnerId, categoryId, pageable, principalDetails.getUser());
			
		}
		
		// 1-1. 세션 전달
		model.addAttribute("principal", principalDetails.getUser());
		model.addAttribute("postDto", postDto);
		
		model.addAttribute("categoryId", categoryId);
		// 1-3. admin 여부 
		model.addAttribute("isAdmin", principalDetails.getUser().getUsername().equals("admin") ? true : false);
    	
		return "/post/list";
	}
	
	@GetMapping("/{pageOwnerId}/post/write")
	public String postWriteForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		// 1-1. 세션 전달
		model.addAttribute("principal", principalDetails.getUser());
		// 1-3. admin 여부 
		model.addAttribute("isAdmin", principalDetails.getUser().getUsername().equals("admin") ? true : false);
    	
		return "/post/writeForm";
	}
	
	@PostMapping("/{pageOwnerId}/post/write")
	public String postWrite(@Valid PostWriteDto postWriteDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		
		postService.writePost(postWriteDto, principalDetails.getUser());

		// 1-1. 세션 전달
		model.addAttribute("principal", principalDetails.getUser());
		// 1-3. admin 여부 
		model.addAttribute("isAdmin", principalDetails.getUser().getUsername().equals("admin") ? true : false);
    		
		return "redirect:/user/"+ principalDetails.getUser().getId() + "/post";
	}
	
	@GetMapping("/{pageOwnerId}/post/{postId}")
	public String postDetail(@PathVariable Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		
		PostDetailDto postDetailRespDto = null;
		
		User loginUser = principalDetails.getUser();
		
		if(loginUser == null) {
			postDetailRespDto = postService.viewPostDetail(postId);
		} else {
			postDetailRespDto = postService.viewPostDetail(postId, loginUser);
		}
		
		// 1-1. 세션 전달
		model.addAttribute("principal", principalDetails.getUser());
		
		model.addAttribute("postDetailRespDto", postDetailRespDto);
		// 1-3. admin 여부 
		model.addAttribute("isAdmin", principalDetails.getUser().getUsername().equals("admin") ? true : false);
    			
		return "/post/detail";
	}
	
	@GetMapping("/{pageOwnerId}/post/{postId}/update")
	public String postUpdateForm(@PathVariable Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		
		PostDetailDto postDetailRespDto = null;
		
		User loginUser = principalDetails.getUser();
		
		if(loginUser == null) {
			postDetailRespDto = postService.viewPostDetail(postId);
		} else {
			postDetailRespDto = postService.viewPostDetail(postId, loginUser);
		}
		
		// 1-1. 세션 전달
		model.addAttribute("principal", principalDetails.getUser());
		
		model.addAttribute("postDetailRespDto", postDetailRespDto);
		// 1-3. admin 여부 
		model.addAttribute("isAdmin", principalDetails.getUser().getUsername().equals("admin") ? true : false);
    	
		return "/post/writeForm";
	}

}
