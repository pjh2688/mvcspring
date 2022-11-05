package com.tistory.web.api.user;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tistory.domain.user.User;
import com.tistory.service.auth.PrincipalDetails;
import com.tistory.service.user.UserService;
import com.tistory.web.dto.CMRespDto;
import com.tistory.web.dto.user.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;
	
	@PutMapping("/user/{principalId}/profileImageUrl")
	public ResponseEntity<?> profileImageUpdate(@PathVariable Long principalId, MultipartFile profileImageFile, @AuthenticationPrincipal PrincipalDetails principalDetails) { // 1-1. html input file의 name 값과 매핑해줘야 한다.(중요)
		
		User userEntity = userService.userProfileUpdate(principalId, profileImageFile);
		principalDetails.setUser(userEntity);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "프로필 사진 변경 성공", null), HttpStatus.OK);
	}
	
	@PutMapping("/user/{principalId}")
	public ResponseEntity<?> update(@PathVariable Long principalId, @Valid final UserUpdateDto userUpdateDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User userEntity = userService.userUpdate(principalId, userUpdateDto);
		principalDetails.setUser(userEntity);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "회원 정보 수정 성공", userUpdateDto), HttpStatus.OK);	
		
	}
	
}
