package com.tistory.web.dto.love;

import com.tistory.web.dto.post.PostInfoDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoveDto {

	private Long loveId;
	private PostInfoDto post;
}
