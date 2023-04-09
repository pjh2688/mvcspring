package com.shop.domain.board;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Board {

	private Long boardNo;
	
	@NotBlank
	private String title;
	
	@NotBlank
	private String content;
	
	@NotBlank
	private String writer;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime modifiedDate;
}
