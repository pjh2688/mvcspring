package com.photogram.web.api.user;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.photogram.config.auth.PrincipalDetails;
import com.photogram.domain.user.User;
import com.photogram.dto.CMRespDto;
import com.photogram.dto.subscribe.SubscribeDto;
import com.photogram.dto.user.UserUpdateDto;
import com.photogram.service.subscribe.SubscribeService;
import com.photogram.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {
	
	private final UserService userService;
	private final SubscribeService subscribeService;

	@PutMapping("/user/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid final UserUpdateDto userUpdateDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		User userEntity = userService.userUpdate(id, userUpdateDto.toEntity());	
		// 1-1. 업데이트된 유저 정보로 세션 정보 변경.
		principalDetails.setUser(userEntity);

		return new ResponseEntity<>(new CMRespDto<>(1, "회원 정보 수정 성공", userEntity), HttpStatus.OK);	
		
		
	}
	
	@GetMapping("/user/{pageUserId}/subscribe")
	public ResponseEntity<?> subscribeList(@PathVariable Long pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		List<SubscribeDto> subscribeDto = subscribeService.subscribeList(principalDetails.getUser().getId(), pageUserId);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "구매자 정보 리스트 가져오기 성공", subscribeDto), HttpStatus.OK);
	}
	
	@PutMapping("/user/{principalId}/profileImageUrl")
	public ResponseEntity<?> profileImageUpdate(@PathVariable Long principalId, MultipartFile profileImageFile, @AuthenticationPrincipal PrincipalDetails principalDetails) { // 1-1. html input file의 name 값과 매핑해줘야 한다.
		User userEntity = userService.userProfileUpdate(principalId, profileImageFile);
		principalDetails.setUser(userEntity);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "프로필 사진 변경 성공", null), HttpStatus.OK);
	}
	
	
}