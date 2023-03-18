package com.shop.mapper.board;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.shop.domain.board.Board;

@Mapper
public interface IBoardMapper {

	public int save(Board board);
	
	public List<Board> findAll();
	
	public Optional<Board> findByBoardNo(Long boardNo);
	
	public int update(Board board);
	
	public int delete(Long boardNo);
}
