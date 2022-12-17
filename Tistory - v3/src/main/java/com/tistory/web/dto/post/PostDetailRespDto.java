package com.tistory.web.dto.post;

import com.tistory.entity.post.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDetailRespDto {

	// 1-1. Post 엔티티
	private Post post;
	    
	// 1-2. 페이지 주인 여부
	private boolean isPageOwner; 
		
	// 1-3. 좋아요 여부 : 좋아요를 했으면 true, false
	private boolean isLove; 
	
	private Long loveId;
}
