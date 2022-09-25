package com.photogram.dto.user;

import com.photogram.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {

	private Boolean isPageOwner; // 1-1. 페이지의 주인 여부
	private int imageCount;
	private boolean subscribeState;
	private int subscribeCount;
	private User user;
}
