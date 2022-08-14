package com.chocogram.web.board;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

	/**
	 * 1. 게시글 리스트 페이지 
	 */
	@GetMapping("/list")
	public String openBoardList() {
		return "board/list";
	}
	
	/**
	 * 2. 게시글 등록 페이지 
	 */
	@GetMapping("/write")
	public String openBoardWriteForm() {
		return "board/write";
	}
	
	/**
	 * 3. 게시글 상세 페이지 
	 */
	@GetMapping("/view/{id}")
	public String openBoardView(@PathVariable final Long id, Model model) {
		model.addAttribute("id", id);
		return "board/view";
	}
}
