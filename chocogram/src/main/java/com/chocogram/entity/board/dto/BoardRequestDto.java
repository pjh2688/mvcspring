package com.chocogram.entity.board.dto;

import com.chocogram.entity.board.Board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequestDto {

	private String title; // 1. 제목
	
	private String content; // 2. 내용
	
	private String writer; // 3. 작성자
	
	private char deleteYn; // 4. 삭제여부
	
	public Board toEntity() {
		return Board.builder()
				.title(title)
				.content(content)
				.writer(writer)
				.hits(0)
				.deleteYn(deleteYn)
				.build();
	}
}
