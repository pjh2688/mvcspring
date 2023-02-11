package com.shop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

	@GetMapping("/member/board")
	public String boardList() {
		return "board/boardList";
	}
}
