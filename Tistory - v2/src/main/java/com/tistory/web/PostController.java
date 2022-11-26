package com.tistory.web;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.tistory.domain.category.Category;
import com.tistory.domain.user.User;
import com.tistory.handler.exception.CustomException;
import com.tistory.service.auth.PrincipalDetails;
import com.tistory.service.category.CategoryService;
import com.tistory.service.post.PostService;
import com.tistory.web.dto.post.PostDetailRespDto;
import com.tistory.web.dto.post.PostRespDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostController {
	
	private final CategoryService categoryService;
	private final PostService postService;
	
	@GetMapping("/user/{principalId}/post/write")
	public String writeForm(@RequestParam(required = false) Long categoryId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		List<Category> categories = categoryService.findAll();
		
		if (categories.size() == 0) {
            throw new CustomException("카테고리 등록이 필요해요");
        }
		
		model.addAttribute("principal", principalDetails.getUser());
		// 1-3. admin 여부 
		model.addAttribute("isAdmin", principalDetails.getUser().getUsername().equals("admin") ? true : false);
    	
		model.addAttribute("categories", categories);
		
		return "/post/writeForm";
	}

	@GetMapping("/user/{pageOwnerId}/post")
	public String postList(@RequestParam(required = false) Long categoryId, @PathVariable Long pageOwnerId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model, @PageableDefault(size = 2) Pageable pageable) {
		
		PostRespDto postRespDto = null;
		
		// 1-2. categoryId는 url 파라미터로 붙어오면 값이 매개변수에 전달되서 온다. -> @RequestParam은 안붙여도 되는데 이해를 위해 붙여놓았다.
		if(categoryId == null) {
			postRespDto = postService.viewPost(pageOwnerId, pageable, principalDetails.getUser());
		} else {
			postRespDto = postService.viewPostByCategoryId(pageOwnerId, categoryId, pageable, principalDetails.getUser());
					
		}
		
		// 1-1. 세션 전달
		model.addAttribute("principal", principalDetails.getUser());
		model.addAttribute("postRespDto", postRespDto);
				
		model.addAttribute("categoryId", categoryId);
		// 1-3. admin 여부 
		model.addAttribute("isAdmin", principalDetails.getUser().getUsername().equals("admin") ? true : false);
		
		
		return "/post/list";
	}
	
	@GetMapping("/user/{pageOwnerId}/post/{postId}")
	public String postDetail(@PathVariable Long pageOwnerId, @PathVariable Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		
		PostDetailRespDto postDetailRespDto = null;
		
		User loginUser = principalDetails.getUser();
		
		if(loginUser == null) {
			postDetailRespDto = postService.viewPostDetail(postId);
		} else {
			postDetailRespDto = postService.viewPostDetail(postId, loginUser);
		}
		
		model.addAttribute("principal", principalDetails.getUser());
		model.addAttribute("pageOwnerId", pageOwnerId);
		model.addAttribute("postDetailRespDto", postDetailRespDto);
		// 1-3. admin 여부 
		model.addAttribute("isAdmin", principalDetails.getUser().getUsername().equals("admin") ? true : false);
				
		
		return "/post/detail";
	}
	
	@GetMapping("/user/{pageOwnerId}/post/{postId}/update")
	public String postUpdateForm(@PathVariable Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		
		PostDetailRespDto postDetailRespDto = null;
		
		User loginUser = principalDetails.getUser();
		
		if(loginUser == null) {
			postDetailRespDto = postService.viewPostDetail(postId);
		} else {
			postDetailRespDto = postService.viewPostDetail(postId, loginUser);
		}
		
		List<Category> categories = categoryService.findAll();
		
		if (categories.size() == 0) {
            throw new CustomException("카테고리 등록이 필요해요");
        }
		
		// 1-1. 세션 전달
		model.addAttribute("principal", principalDetails.getUser());
		
		model.addAttribute("postDetailRespDto", postDetailRespDto);
		
		model.addAttribute("categories", categories);
		
		// 1-3. admin 여부 
		model.addAttribute("isAdmin", principalDetails.getUser().getUsername().equals("admin") ? true : false);
    	
		return "/post/writeForm";
	}
}
