package com.tistory.web;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.tistory.service.auth.PrincipalDetails;
import com.tistory.service.user.UserService;
import com.tistory.web.dto.user.JoinDto;
import com.tistory.web.dto.user.PasswordResetDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/login-form")
    public String loginForm() {
        return "/user/loginForm";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "/user/joinForm";
    }
    
    @PostMapping("/join")
    public String join(@Valid JoinDto joinDto, BindingResult bindingResult) {
    	
        userService.join(joinDto.toEntity());

        return "redirect:/login-form";
    }
    
    @GetMapping("/password-reset-form")
    public String passwordResetForm() {
    	
    	return "/user/passwordResetForm";
    }
    
    @PostMapping("/password-reset")
    public String passwordReset(@Valid PasswordResetDto passwordResetDto, BindingResult bindingResult) {
    	
    	userService.resetPassword(passwordResetDto);
  
        return "redirect:/login-form";
    }
    
    @GetMapping("/user/{pageOwnerId}")
    public String updateForm(@PathVariable Long pageOwnerId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
    	
    	// 1-1. 세션 전달(로그인한 회원 아이디)
    	model.addAttribute("principal", principalDetails.getUser());
    	
    	// 1-2. 페이지의 주인 아이디
    	model.addAttribute("pageOwnerId", pageOwnerId);
    
    	// 1-3. admin 여부 
    	model.addAttribute("isAdmin", principalDetails.getUser().getUsername().equals("admin") ? true : false);
    	
        return "/user/updateForm";
    }
    
}
