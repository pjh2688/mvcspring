package com.tistory.web.dto.post;

import java.util.List;

import org.springframework.data.domain.Page;

import com.tistory.entity.category.Category;
import com.tistory.entity.post.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostRespDto {

	private Page<Post> posts;
    private List<Category> categorys;
    private Long userId;
    private Integer prev;
    private Integer next;
    private List<Integer> pageNumbers;
    private Long totalCount;
    
    private Boolean isPrev;
    private Boolean isNext;
    
    private Boolean isFirst;
}
