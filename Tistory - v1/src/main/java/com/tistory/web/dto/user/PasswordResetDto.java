package com.tistory.web.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordResetDto {

	@Size(min = 4, max = 20, message = "유저네임 길이는 4이상 20이하입니다.")
    @NotBlank
    private String username;

    @Size(min = 4, max = 20, message = "이메일 길이는 4이상 20이하입니다.")
    @Email
    @NotBlank
    private String email;
}
