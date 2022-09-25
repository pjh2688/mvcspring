package com.photogram.web.api.image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photogram.config.auth.PrincipalDetails;
import com.photogram.domain.image.Image;
import com.photogram.dto.CMRespDto;
import com.photogram.service.image.ImageService;
import com.photogram.service.likes.LikesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImageApiController {

	private final ImageService imageService;
	private final LikesService likesService;
	
	@GetMapping("/image")
	public ResponseEntity<?> imageStory(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Image> images = imageService.imageStory(principalDetails.getUser().getId(), pageable);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "이미지 스토리 리스트 불러오기 성공", images), HttpStatus.OK);
	}
	
	@PostMapping("/image/{imageId}/likes")
	public ResponseEntity<?> likes(@PathVariable Long imageId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		likesService.likes(imageId, principalDetails.getUser().getId());
		
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 성공", null), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/image/{imageId}/likes")
	public ResponseEntity<?> unLikes(@PathVariable Long imageId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		likesService.unLikes(imageId, principalDetails.getUser().getId());
		
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 취소 성공", null), HttpStatus.OK);
	}
}