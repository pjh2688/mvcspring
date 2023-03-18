package com.shop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PdsController {

	@GetMapping("/viewPds")
	public String viewPds() {
		return "pds/pds_info";
	}
}
