package com.tistory.web.dto.post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tistory.entity.category.Category;
import com.tistory.entity.post.Post;
import com.tistory.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data // Getter, Setter, toString
public class PostWriteReqDto {

	private Long categoryId;
	
	@NotBlank
	private String title;
	
	@NotNull  // 공백만 허용.
	private String content;
	
	@JsonIgnore
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

/*
 * - 참고 : com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class java.io.FileDescriptor and no properties discovered to create BeanSerializer
     -> 파일은 json형태로 dto객체에 반환받을 때 json 형태의 파일 데이터는 @Getter @Setter가 불가능하다. 그래서 @JsonIgnore 해줘야한다. 
 */
 