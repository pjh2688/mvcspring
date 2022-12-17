package com.tistory.web;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tistory.entity.category.Category;
import com.tistory.handler.exception.CustomException;
import com.tistory.service.auth.PrincipalDetails;
import com.tistory.service.board.BoardService;
import com.tistory.service.category.CategoryService;
import com.tistory.web.dto.board.BoardResponseDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	
	private final CategoryService categoryService;
	private final BoardService boardService;
	
	@GetMapping("/user/boards")
	public String boardList(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		
		// 2022-12-07 -> Tistory에 mybatis 게시판 추가하기 세팅중
		
		List<Category> categories = categoryService.findAll();

		if (categories.size() == 0) {
			throw new CustomException("카테고리 등록이 필요해요");
		}

		// 1-1. 세션 전달(로그인한 회원 아이디)
		model.addAttribute("principal", principalDetails.getUser());

		model.addAttribute("categories", categories);

		return "/board/list";
	}
	
	@GetMapping("/user/boards/write")
	public String boardWriteForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		
		List<Category> categories = categoryService.findAll();

		if (categories.size() == 0) {
			throw new CustomException("카테고리 등록이 필요해요");
		}

		// 1-1. 세션 전달(로그인한 회원 아이디)
		model.addAttribute("principal", principalDetails.getUser());

		model.addAttribute("categories", categories);
		
		return "/board/writeForm";
	}
	
	@GetMapping("/user/boards/{boardId}/detail")
	public String boardDetail(@PathVariable Long boardId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		
		List<Category> categories = categoryService.findAll();
		
		if (categories.size() == 0) {
			throw new CustomException("카테고리 등록이 필요해요");
		}
		
		BoardResponseDto boardResponseDto = boardService.findById(boardId);
		
		// 1-1. 세션 전달(로그인한 회원 아이디)
		model.addAttribute("principal", principalDetails.getUser());
		model.addAttribute("boardResponseDto", boardResponseDto);
		model.addAttribute("categories", categories);
		
		return "/board/detail";
	}
	
	@GetMapping("/user/boards/{boardId}/update")
	public String boardUpdate(@PathVariable Long boardId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		
		List<Category> categories = categoryService.findAll();
		
		if (categories.size() == 0) {
			throw new CustomException("카테고리 등록이 필요해요");
		}
		
		BoardResponseDto boardResponseDto = boardService.findById(boardId, principalDetails.getUser());
		
		// 1-1. 세션 전달(로그인한 회원 아이디)
		model.addAttribute("principal", principalDetails.getUser());
		model.addAttribute("boardResponseDto", boardResponseDto);
		model.addAttribute("categories", categories);
		
		return "/board/writeForm";
	}
	

}
