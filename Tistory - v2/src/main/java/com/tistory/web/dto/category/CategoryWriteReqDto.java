package com.tistory.web.dto.category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.tistory.domain.category.Category;
import com.tistory.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryWriteReqDto {
	
	@Size(min = 1, max = 60)
    @NotBlank
    private String title;

    public Category toEntity(User principal) {
        Category category = new Category();
        category.setTitle(title);
        category.setUser(principal);
        return category;
    }
}
