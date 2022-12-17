package com.tistory.web.dto.board;

import java.time.LocalDateTime;

import com.tistory.entity.board.Board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponseDto {
	
	private Long id; // 1. PK
	
	private String title; // 2. 제목
	
	private String content; // 3. 내용
	
	private String writer; // 4. 작성자
	
	private int hits;  // 5. 조회수
	
	private char deleteYn; // 6. 삭제여부
	
	private LocalDateTime createdDate = LocalDateTime.now();  // 7. 생성일
	
	private LocalDateTime modifiedDate;  // 7. 수정일
	
	public BoardResponseDto(Board entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.content = entity.getContent();
		this.writer = entity.getWriter();
		this.hits = entity.getHits();
		this.deleteYn = entity.getDeleteYn();
		this.createdDate = entity.getCreatedDate();
		this.modifiedDate = entity.getModifiedDate();
	}
}
