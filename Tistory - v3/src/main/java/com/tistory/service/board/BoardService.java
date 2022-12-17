package com.tistory.service.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tistory.entity.board.Board;
import com.tistory.entity.board.BoardRepository;
import com.tistory.entity.user.User;
import com.tistory.handler.exception.CustomApiException;
import com.tistory.mapper.board.IBoardMapper;
import com.tistory.web.dto.board.BoardRequestDto;
import com.tistory.web.dto.board.BoardResponseDto;
import com.tistory.web.dto.board.paging.CommonParams;
import com.tistory.web.dto.board.paging.Pagination;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final IBoardMapper boardMapper;

	/**
	 *  1. 게시글 생성
	 */
	@Transactional
	public void writeBoard(final BoardRequestDto params, User principal) {
		
		if(principal == null) {
			throw new CustomApiException("로그인을 해주세요.");
		}
		
		try {
			boardRepository.save(params.toEntity());
		} catch(Exception e) {
			throw new CustomApiException("글쓰기 실패");
		}
	}
	/**
	 *  2-1. 게시글 조회
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
	 *  3-1. 게시글 수정
	 */
	@Transactional
	public Long update(final Long boardId, final BoardRequestDto params) {
		
		Board boardEntity = null;
		
		// 3-2. Java 8 Stream API 사용 O
	//	Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
    //	entity.update(params.getTitle(), params.getContent(), params.getWriter());
		
		// 3-3. Java 8 Stream API 사용 X
		Optional<Board> boardOp = boardRepository.findById(boardId);
		
		if (boardOp.isPresent()) {
			boardEntity = boardOp.get();
	    } else {
	        throw new CustomApiException("해당 게시글이 존재하지 않습니다");
	    }
	
		boardEntity.update(params.getTitle(), params.getContent(), params.getWriter());
		
		return boardId;
	}
	
	/**
	 * 5-1. 게시글 상세정보 조회 
	 */
	@Transactional  // 5-2. 영속성 컨텍스트에서 관리되려면 트랜잭션으로 묶여있어야 변경감지가 동작한다.
	public BoardResponseDto findById(final Long id) {
		
		Board boardEntity = null;
		
		Optional<Board> boardOp = boardRepository.findById(id);
		
		if (boardOp.isPresent()) {
			boardEntity = boardOp.get();
	    } else {
	        throw new CustomApiException("해당 게시글이 존재하지 않습니다");
	    }
		
		boardEntity.increaseHits();
		
		return new BoardResponseDto(boardEntity);
		
	}
	
	// 2022-12-14 -> 조회수 한 번만 증가하게 수정, 게시글 수정 기능 추가 
	/**
	 * 5-3. 게시글 상세정보 조회(글 수정 폼에서 조회 - 조회수 증가 X)  
	 */
	@Transactional  // 5-4. 글 수정때 조회할땐 조회수 증가 X
	public BoardResponseDto findById(final Long id, User principal) {
		
		Board boardEntity = null;
		
		Optional<Board> boardOp = boardRepository.findById(id);
		
		if (boardOp.isPresent()) {
			boardEntity = boardOp.get();
	    } else {
	        throw new CustomApiException("해당 게시글이 존재하지 않습니다");
	    }
	
		return new BoardResponseDto(boardEntity);
		
	}
	
	/**
	 * 7-1. 게시글 리스트 조회 - (페이징 처리) 
	 */
	public Map<String, Object> findAll(final CommonParams params) {
		Map<String, Object> result = new HashMap<>();
		
		// 7-2. 게시글 갯수 조회
		int count = boardMapper.count(params);
		
		// 7-3. 페이지네이션 정보 계산 후 CommonParams에 셋팅
		Pagination pagination = new Pagination(count, params);
		params.setPagination(pagination);

		// 7-4. 등록된 게시글이 없는 경우, 페이징 데이터 1로 초기화해 세팅 후 비어있는 list를 보내준다.
		if(count < 1) {
			List<BoardResponseDto> list = new ArrayList<>();
			
			pagination = new Pagination(1, params);
			params.setPagination(pagination);
			
			result.put("params", params);
			result.put("list", list);
			
			return result;
		}
		
		// 7-5. 게시글 리스트 조회
		List<BoardResponseDto> list = boardMapper.findAll(params);
		
		// 7-6. 데이터 반환
		result.put("params", params);
		result.put("list", list);
		
		return result;
	}
}
