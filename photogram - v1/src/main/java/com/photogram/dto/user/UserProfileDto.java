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

	// 1-1. 페이지의 주인인지 여부.
	private boolean pageOwnerState;
	
	// 1-2.
	private User user;
	
	// 1-3. 
	private int imageCount;
	
	// 1-4. 구독 상태
	private boolean subscribeState;
	
	// 1-5. 구독 수
	private int subscribeCount;

}
