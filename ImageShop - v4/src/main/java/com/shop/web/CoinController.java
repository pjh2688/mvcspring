package com.shop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CoinController {

	@GetMapping("/viewCoin")
	public String coinInfo() {
		return "coin/coin_info";
	}
}
