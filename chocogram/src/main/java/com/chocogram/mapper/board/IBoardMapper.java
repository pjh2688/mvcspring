package com.chocogram.mapper.board;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.chocogram.entity.board.dto.BoardResponseDto;
import com.chocogram.paging.CommonParams;

@Mapper
public interface IBoardMapper {

	/**
	 * 1. 게시글 수 조회 
	 */
	int count(final CommonParams params);
	
	List<BoardResponseDto> findAll(final CommonParams params);
}
