package com.chocogram.entity.board;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 1. PK
	
	private String title; // 2. 제목
	
	private String content; // 3. 내용
	
	private String writer; // 4. 작성자
	
	private int hits; // 5. 조회수
	
	private char deleteYn; // 6. 삭제여부
	
	private LocalDateTime createdDate = LocalDateTime.now();  // 7. 생성일
	
	private LocalDateTime modifiedDate; // 7. 수정일
	
	/**
	 * 8. 엔티티로 변환해주는 생성자 빌더. 
	 */
	@Builder
	public Board(String title, String content, String writer, int hits, char deleteYn) {
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.hits = hits;
		this.deleteYn = deleteYn;
	}
	
	/**
	 * 9. 게시글 수정 
	 */
	public void update(String title, String content, String writer) {
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.modifiedDate = LocalDateTime.now();
	}
	
	/**
	 * 10. 조회수 증가 
	 */
	public void increaseHits() {
		this.hits++;
	}
	
	/**
	 * 11. 게시글 삭제 
	 */
	public void delete() {
		this.deleteYn = 'Y';
	}
}
