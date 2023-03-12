package com.shop.web.api.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.domain.board.Board;
import com.shop.handler.exception.CustomValidationApiException;
import com.shop.service.board.BoardService;
import com.shop.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class BoardApiController {
	
	private final BoardService boardService;

	@PostMapping("/boards")
	public ResponseEntity<?> register(@Validated @RequestBody Board board, BindingResult bindingResult) throws Exception {
		
		log.info("board.getWriter() = " + board.getWriter());
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
				System.out.println(error.getDefaultMessage());
			}
	
			throw new CustomValidationApiException("유효성 검사 실패", errorMap);
		}
		
		boardService.register(board);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "게시글 등록하기 성공", null), HttpStatus.OK);
	}
	
	@GetMapping("/boards")
	public ResponseEntity<?> list(Authentication authentication) throws Exception {
		log.info("list");
		
		List<Board> result = boardService.findAll();
		
		log.info("list result = " + result);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "게시글 리스트 불러오기 성공", result), HttpStatus.OK);
	}
	
	@GetMapping("/boards/{boardNo}")
	public ResponseEntity<?> readByBoardNo(@PathVariable("boardNo") Long boardNo) throws Exception {
		log.info("readByUserNo");
		
		Board findBoard = boardService.findByBoardNo(boardNo);
		
		log.info("readByBoardNo us = " + findBoard);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "게시글 불러오기 성공 성공", findBoard), HttpStatus.OK);
	}
	
	@PutMapping("/boards/{boardNo}")
	public ResponseEntity<?> modifyByBoardNo(@PathVariable("boardNo") Long boardNo, @Validated @RequestBody Board board) throws Exception {
		log.info("modify : board.getWriter() = " + board.getWriter());
		log.info("modify : board.getBoardNo() = " + board.getBoardNo());
		
		board.setBoardNo(boardNo);
		
		int result = boardService.update(board);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "게시글 정보 업데이트 성공", result), HttpStatus.OK);
	}
	
	@DeleteMapping("/boards/{boardNo}")
	public ResponseEntity<?> deleteByBoardNo(@PathVariable("boardNo") Long boardNo) throws Exception {
		log.info("deleteByBoardNo");
		
		int result = boardService.delete(boardNo);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "게시글 삭제 성공", result), HttpStatus.OK);
	}
}
