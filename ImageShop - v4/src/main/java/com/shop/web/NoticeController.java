package com.shop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticeController {

	@GetMapping("/viewNotice")
	public String viewNotice() {
		return "notice/notice_info";
	}
}
