package com.chocogram.domain.board;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

	private Long id; // 1. PK
	
	private String title; // 2. 제목
	
	private String content; // 3. 내용
	
	private String writer; // 4. 작성자
	
	private int hits; // 5. 조회수
	
	private char deleteYn; // 6. 삭제여부
	
	private LocalDateTime createdDate = LocalDateTime.now();  // 7. 생성일
	
	private LocalDateTime modifiedDate; // 7. 수정일
}
