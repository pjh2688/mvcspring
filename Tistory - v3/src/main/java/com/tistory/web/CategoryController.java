package com.tistory.web;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.tistory.entity.category.Category;
import com.tistory.service.auth.PrincipalDetails;
import com.tistory.service.category.CategoryService;
import com.tistory.web.dto.category.CategoryWriteReqDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CategoryController {
	
	private final CategoryService categoryService;
	
	@GetMapping("/category/write")
    public String catgegoryWriteForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        
		model.addAttribute("principal", principalDetails.getUser());
		// 1-3. admin 여부 
		model.addAttribute("isAdmin", principalDetails.getUser().getUsername().equals("admin") ? true : false);
    	
		return "/category/writeForm";
    }
	
	@PostMapping("/category/write") 
	public String categoryWrite(@Valid CategoryWriteReqDto categoryWriteReqDto,
            BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		Category category = categoryWriteReqDto.toEntity(principalDetails.getUser());

		categoryService.categoryRegister(category);
				
		return "redirect:/category/write";
	}
}
