package com.photogram.dto.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.photogram.domain.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupDto {

	@Size(min = 2, max = 20)
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String name;
	
	public User toEntity() {
		return User.builder()
				.username(username)
				.password(password)
				.email(email)
				.name(name)
				.build();
	}
}

/*
 *	https://bamdule.tistory.com/35
 * -> spring boot validation 어노테이션 정리 
 **/


