package com.tistory.entity.board;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>{
	/**
	 *  1. 게시글 리스트 조회 - (삭제 여부 필터링) 
	 */
	List<Board> findAllByDeleteYn(final char deleteYn, final Sort sort);
}
