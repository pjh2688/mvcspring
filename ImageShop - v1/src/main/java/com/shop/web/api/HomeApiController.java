package com.shop.web.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeApiController {

	@GetMapping("/api/admin/home")
	public String adminHome() {
		return "home";
	}
	
	@GetMapping("/api/manage/home")
	public String manageHome() {
		return "home";
	}
}
