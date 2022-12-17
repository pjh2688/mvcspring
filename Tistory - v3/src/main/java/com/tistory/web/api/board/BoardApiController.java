package com.tistory.web.api.board;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tistory.entity.user.User;
import com.tistory.service.auth.PrincipalDetails;
import com.tistory.service.board.BoardService;
import com.tistory.web.dto.CMRespDto;
import com.tistory.web.dto.board.BoardRequestDto;
import com.tistory.web.dto.board.BoardResponseDto;
import com.tistory.web.dto.board.paging.CommonParams;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardApiController {
	
	private final BoardService boardService;
	
	@PostMapping("/boards")
	public ResponseEntity<?> writeBoard(@RequestBody @Valid final BoardRequestDto boardRequestDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) { 
		
		User principal = principalDetails.getUser();
		
		boardService.writeBoard(boardRequestDto, principal);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "포스팅 글쓰기 성공", null), HttpStatus.OK);
	}
	
//	@GetMapping("/boards")
	public ResponseEntity<?> selectBoardByAll(@AuthenticationPrincipal PrincipalDetails principalDetails) { 
		
		List<BoardResponseDto> result = boardService.findAll();
		
		return new ResponseEntity<>(new CMRespDto<>(1, "포스팅 글 리스트 불러오기 성공", result), HttpStatus.OK);
	}
	
	@GetMapping("/boards")
	public ResponseEntity<?> selectBoardByPaging(final CommonParams params, @AuthenticationPrincipal PrincipalDetails principalDetails) { 
		
		Map<String, Object> result = boardService.findAll(params);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "포스팅 글 리스트 불러오기 성공", result), HttpStatus.OK);
	}
	
	/**
	 *  4. 게시글 수정
	 */
	@PatchMapping("/boards/{boardId}")
	public ResponseEntity<?> updateBoard(@PathVariable Long boardId, @RequestBody @Valid final BoardRequestDto boardRequestDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) { 
		
		boardService.update(boardId, boardRequestDto);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "포스팅 글쓰기 수정 성공", null), HttpStatus.OK);
	}
}
