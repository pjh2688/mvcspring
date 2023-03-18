package com.shop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CodeController {
	
	@GetMapping("/viewCodeGroup")
	public String viewCodegroup() {
		return "codegroup/code_group";
	}
	
	@GetMapping("/viewCodeDetail")
	public String viewCodeDetail() {
		return "codedetail/code_detail";
	}
}
