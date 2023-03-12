package com.shop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {
	
	@GetMapping("/viewItem")
	public String viewItemInfo() {
		return "item/item_info";
	}
}
