package com.tistory.web.dto.post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data // Getter, Setter, toString
public class PostUpdateDto {

	private Long categoryId;
	
	@NotBlank
	private String title;
	
	@NotNull  // 공백만 허용.
	private String content;
	
}
