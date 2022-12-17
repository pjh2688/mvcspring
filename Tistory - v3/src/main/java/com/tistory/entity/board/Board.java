package com.tistory.entity.board;

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
	
	private int hits;  // 5. 조회수
	
	private char deleteYn; // 6. 삭제여부
	
	private LocalDateTime createdDate = LocalDateTime.now();  // 7. 생성일
	
	private LocalDateTime modifiedDate;  // 7. 수정일
	
	/**
	 *  8. 엔티티로 변환해주는 생성자 빌더.
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

/*
 * CREATE TABLE `board` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'PK',
	`title` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8_general_ci' COMMENT '제목',
	`content` VARCHAR(3000) NULL DEFAULT NULL COLLATE 'utf8_general_ci' COMMENT '내용',
	`writer` VARCHAR(20) NULL DEFAULT NULL COLLATE 'utf8_general_ci' COMMENT '작성자',
	`hits` INT(11) NOT NULL COMMENT '조회 수',
	`created_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '생성일시',
	`modified_date` DATETIME NULL DEFAULT NULL COMMENT '최종 수정일시',
	`notice_yn` TINYINT(1) NOT NULL COLLATE 'utf8_general_ci',
	`delete_yn` TINYINT(1) NOT NULL COLLATE 'utf8_general_ci',
	PRIMARY KEY (`id`) USING BTREE
) COMMENT '게시글'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
 
 */
 