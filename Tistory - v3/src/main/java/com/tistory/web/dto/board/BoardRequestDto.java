package com.tistory.web.dto.board;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.tistory.entity.board.Board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardRequestDto {
	
	@NotBlank
	private String title; // 1. 제목
	
	@NotNull
	private String content; // 2. 내용
	
	@NotNull
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
