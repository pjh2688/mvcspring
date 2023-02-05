package com.shop.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

	@GetMapping("/registerAdmin")
	public String registerAdmin() {
		return "admin/setup_admin";
	}
}
