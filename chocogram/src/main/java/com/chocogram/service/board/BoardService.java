package com.chocogram.service.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chocogram.entity.board.Board;
import com.chocogram.entity.board.BoardRepository;
import com.chocogram.entity.board.dto.BoardRequestDto;
import com.chocogram.entity.board.dto.BoardResponseDto;
import com.chocogram.exception.CustomException;
import com.chocogram.exception.ErrorCode;
import com.chocogram.mapper.board.IBoardMapper;
import com.chocogram.paging.CommonParams;
import com.chocogram.paging.Pagination;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	
	private final IBoardMapper boardMapper;
	
	/**
	 * 1. 게시글 생성 
	 */
	@Transactional
	public Long save(final BoardRequestDto params) {
		Board entity = boardRepository.save(params.toEntity());
		return entity.getId();
	}
	
	/**
	 * 2-1. 게시글 조회 
	 */
	public List<BoardResponseDto> findAll() {
		Sort sort = Sort.by(Direction.DESC, "id", "createdDate");
		List<Board> list = boardRepository.findAll(sort);
		
		// 2-2. Java 8 Stream API 사용 O
	//	return list.stream().map(BoardResponseDto::new).collect(Collectors.toList());
		
		// 2-3. Java 8 Stream API 사용 X
		List<BoardResponseDto> boardList = new ArrayList<>();
		
		for(Board entity : list) {
			boardList.add(new BoardResponseDto(entity));
		}
		
		return boardList;
	}
	
	/**
	 * 3-1. 게시글 수정 
	 */
	@Transactional
	public Long update(final Long id, final BoardRequestDto params) {
		
		// 3-2. Java 8 Stream API 사용 O
//		Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
//		entity.update(params.getTitle(), params.getContent(), params.getWriter());
		
		// 3-3 Java 8 Stream API
		Board entity = boardRepository.findById(id).orElse(null);
		
		if(entity == null) {
			throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
		}
		
		entity.update(params.getTitle(), params.getContent(), params.getWriter());
		
		return id;
	}
	
	/**
	 * 4. 게시글 삭제 
	 */
	@Transactional
	public Long delete(final Long id) {
		Board entity = boardRepository.findById(id).orElse(null);
		
		if(entity == null) {
			throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
		}
		
		entity.delete();
		
		return id;
	}
	
	/**
	 * 5-1. 게시글 상세정보 조회 
	 */
	@Transactional  // 5-2. 영속성 컨텍스트에서 관리되려면 트랜잭션으로 묶여있어야 변경감지(더티체킹)가 동작한다.
	public BoardResponseDto findById(final Long id) {
		Board entity = boardRepository.findById(id).orElse(null);
		
		if(entity == null) {
			throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
		}
		
		entity.increaseHits();
		
		return new BoardResponseDto(entity);
	}
	
	/**
	 * 6. 게시글 리스트 조회 - (삭제 여부 필터링) 
	 */
	public List<BoardResponseDto> findAllByDeleteYn(final char deleteYn) {
		Sort sort = Sort.by(Direction.DESC, "id", "createdDate");
		List<Board> list = boardRepository.findAllByDeleteYn(deleteYn, sort);
		
		List<BoardResponseDto> boardList = new ArrayList<>();
		
		for(Board entity : list) {
			boardList.add(new BoardResponseDto(entity));
		}
		
		return boardList;
	}
	
	/**
	 * 7-1. 게시글 리스트 조회 - (페이징 처리) 
	 */
	public Map<String, Object> findAll(CommonParams params)	 {
		// 7-2. 게시글 갯수 조회
		int count = boardMapper.count(params);
		
		// 7-3. 등록된 게시글이 없는 경우, 비어 있는 컬렉션 맵 반환
		if(count < 1) {
			return Collections.emptyMap();
		}
		
		// 7-4. 페이지네이션 정보 계산 후 CommonParams에 셋팅
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);
		
		// 7-5. 게시글 리스트 조회
		List<BoardResponseDto> list = boardMapper.findAll(params);
		
		// 7-6. 데이터 반환
		Map<String, Object> result = new HashMap<>();
		result.put("params", params);
		result.put("list", list);
		
		return result;
	}

	
}
