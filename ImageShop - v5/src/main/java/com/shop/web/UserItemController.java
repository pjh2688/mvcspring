package com.shop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserItemController {

	@GetMapping("/viewUserItem")
	public String userItemInfo() {
		return "item/useritem_info";
	}
}
