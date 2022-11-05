package com.tistory.web.dto.post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.tistory.domain.category.Category;
import com.tistory.domain.post.Post;
import com.tistory.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data // Getter, Setter, toString
public class PostWriteDto {

	private Long categoryId;
	
	@NotBlank
	private String title;
	
	@NotNull  // 공백만 허용.
	private String content;
	
	private MultipartFile thumnailFile;
	
	public Post toEntity(String thumnail, User principal, Category category) {
        Post post = new Post();
        
        post.setTitle(title);
        post.setContent(content);
        post.setThumnail(thumnail);
        post.setUser(principal);
        post.setCategory(category);
        
        return post;
    }
	
}
