package com.photogram.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CommentDto {

	@NotBlank
	private String content;
	
	@NotNull
	private Long imageId;
	
	// toEntity 필요 없음.
	// @NotBlank : 빈값, NULL값, 공백 체크
	// @NotEmpty : 빈값, NULL값 체크
	// @NotNull : NULL값 체크
}
