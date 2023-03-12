package com.shop.service.board;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.domain.board.Board;
import com.shop.handler.exception.CustomApiException;
import com.shop.mapper.board.IBoardMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {

	private final IBoardMapper boardMapper;
	
	public int register(Board board) {
		
		int result = 0;
		
		try {
			result = boardMapper.save(board);
		} catch (Exception e) {
			throw new CustomApiException("게시글 저장 실패");
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public List<Board> findAll() {
		
		List<Board> result = null;
		
		try {
			result = boardMapper.findAll();
			
		} catch (Exception e) {
			throw new CustomApiException("게시글 리스트 정보가 존재하지 않습니다.");
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public Board findByBoardNo(Long boardNo) {
		
		Optional<Board> boardOp = boardMapper.findByBoardNo(boardNo);
		
		if(boardOp.isPresent()) {
			return boardOp.get();
			
		} else {
			throw new CustomApiException("해당 게시글이 존재하지 않습니다.");
		}
		
	}
	
	public int update(Board board) {
		Optional<Board> boardOp = boardMapper.findByBoardNo(board.getBoardNo());
		
		if(boardOp.isPresent()) {
			int result = boardMapper.update(board);
			
			return result;
			
		} else {
			throw new CustomApiException("해당 게시글이 존재하지 않습니다.");
		}	
	}
	
	public int delete(Long boardNo) {
		Optional<Board> boardOp = boardMapper.findByBoardNo(boardNo);
		
		if(boardOp.isPresent()) {
			
			int result = boardMapper.delete(boardNo);
			
			return result;
			
		} else {
			throw new CustomApiException("해당 게시글이 존재하지 않습니다.");
		}	
	}
}
