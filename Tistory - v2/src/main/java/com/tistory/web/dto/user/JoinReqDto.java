package com.tistory.web.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.tistory.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinReqDto {

	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9]{3,20}", message = "아이디는 영문과 숫자 혼합이며 3자리부터 20자리까지입니다.")
    @Size(min = 3, max = 20)
    private String username;

    @Size(min = 4, max = 20, message = "패스워드는 최소 4자 ~ 최대 20자까지입니다.")
    @NotBlank
    private String password;

    @Size(min = 8, max = 60)
    @NotBlank // @NotNull, @NotEmpty 두개의 조합
    @Email
    private String email;

	public User toEntity() {
		return User.builder()
				.username(username)
				.password(password)
				.email(email)
				.build();
	}
}
