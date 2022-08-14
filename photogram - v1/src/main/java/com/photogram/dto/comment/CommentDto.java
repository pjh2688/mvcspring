package com.photogram.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

// NotNull = NULL값 체크
// NotEmpty = 빈값이나 NULL 체크
// NotBlank = 빈값이거나 NULL 체크 그리고 빈 공백(스페이스) 체크

@Data
public class CommentDto {

	@NotBlank 
	private String content;
	
	@NotNull 
	private Long imageId;
}
