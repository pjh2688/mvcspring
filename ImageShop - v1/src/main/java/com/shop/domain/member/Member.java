package com.shop.domain.member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Member {

	private Long userNo;
	
	@NotBlank
	private String userId;

	@NotBlank
	private String userPw;
	
	@NotBlank
	private String userName;
	
	private String job;
	
	private Integer coin;
	
	private boolean enabled;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime modifiedDate;
	
	private List<MemberAuth> authList = new ArrayList<>();
	
	private String refreshToken;
	

}

