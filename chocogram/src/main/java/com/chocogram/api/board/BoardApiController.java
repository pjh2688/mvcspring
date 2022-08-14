package com.chocogram.api.board;

// import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chocogram.entity.board.dto.BoardRequestDto;
import com.chocogram.entity.board.dto.BoardResponseDto;
import com.chocogram.paging.CommonParams;
import com.chocogram.service.board.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardApiController {

	private final BoardService boardService;
	
	/**
	 * 1. 게시글 생성 
	 */
	
	@PostMapping("/boards")
	public Long save(@RequestBody final BoardRequestDto params) {
		return boardService.save(params);
	}
	
	/**
	 * 2-1. 게시글 리스트 조회 
	 */
//	@GetMapping("/boards")
//	public List<BoardResponseDto> findAll(@RequestParam final char deleteYn) {
//		return boardService.findAllByDeleteYn(deleteYn);
//	}
	
	/**
	 * 2-2. 게시글 리스트 조회(페이징) 
	 */
	@GetMapping("/boards")
	public Map<String, Object> findAllByPaging(final CommonParams params) {
		return boardService.findAll(params);
	}
	
	/**
	 * 3. 게시글 상세정보 조회 
	 */
	@GetMapping("/boards/{id}")
	public BoardResponseDto findById(@PathVariable final Long id) {
		return boardService.findById(id);
	}
	
	
	/**
	 * 4. 게시글 수정 
	 */
	@PatchMapping("/boards/{id}")
	public Long save(@PathVariable final Long id, @RequestBody final BoardRequestDto params) {
		return boardService.update(id, params);
	}
	
	/**
	 * 5. 게시글 삭제 
	 **/
	@DeleteMapping("/boards/{id}")
	public Long delete(@PathVariable final Long id) {
		return boardService.delete(id);
	}
}
