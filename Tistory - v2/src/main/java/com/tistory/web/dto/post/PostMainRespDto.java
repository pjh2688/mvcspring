package com.tistory.web.dto.post;

import java.util.List;

import org.springframework.data.domain.Page;

import com.tistory.domain.post.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostMainRespDto {

	private Page<Post> posts;
	private Integer prev;
    private Integer next;
    private List<Integer> pageNumbers;
    
    private Boolean isPrev;
    private Boolean isNext;
    
}
