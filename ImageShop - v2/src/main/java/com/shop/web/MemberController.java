package com.shop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	
	@GetMapping("/viewMember")
	public String viewMemberInfo() {
		return "member/member_info";
	}

}
