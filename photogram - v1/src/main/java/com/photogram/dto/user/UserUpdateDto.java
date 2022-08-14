package com.photogram.dto.user;

import javax.validation.constraints.NotBlank;

import com.photogram.domain.user.User;

import lombok.Data;

@Data
public class UserUpdateDto {
	
	@NotBlank  // javax.validation을 걸어주면 컨트롤러에서 @Valid어노테이션 붙여줘야 동작.
	private String name;
	
	@NotBlank
	private String password;
	private String website;
	private String bio;
	private String phone;
	private String gender;
	
	public User toEntity() {
		return User.builder()
				.name(name)
				.password(password)
				.website(website)
				.bio(bio)
				.phone(phone)
				.gender(gender)
				.build();
	}
}
