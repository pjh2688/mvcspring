package com.photogram.web.api.image;

import java.util.HashMap;
import java.util.Map;

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
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageApiController {
	
	private final ImageService imageService;
	private final LikesService likesService;
	
	@GetMapping("")
	public ResponseEntity<?> imageStory(@AuthenticationPrincipal PrincipalDetails principalDetails, 
			@PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			
			Page<Image> images = imageService.imageStory(principalDetails.getUser().getId(), pageable);
			result.put("message", "success");
			result.put("principalId", principalDetails.getUser().getId());
			result.put("images", images);
			
		} catch (Exception e) {
			result.put("message", "fail");
		}
		
		return new ResponseEntity<>(new CMRespDto<>(1, "스토리 페이지 이미지 로딩 성공", result), HttpStatus.OK);
	}
	
	@PostMapping("/{imageId}/likes")
	public ResponseEntity<?> likes(@PathVariable("imageId") Long imageId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			
			likesService.likes(imageId, principalDetails.getUser().getId());
			result.put("code", HttpStatus.CREATED);
			result.put("message", "success");
			
		} catch (Exception e) {
			
			result.put("code", HttpStatus.BAD_REQUEST);
			result.put("message", "fail");
		}
		
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 성공.", result), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{imageId}/likes")
	public ResponseEntity<?> unLikes(@PathVariable("imageId") Long imageId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			likesService.unLikes(imageId, principalDetails.getUser().getId());
			result.put("code", HttpStatus.OK);
			result.put("message", "success");
			
		} catch (Exception e) {
			result.put("code", HttpStatus.BAD_REQUEST);
			result.put("message", "fail");
		}
		
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 취소 성공.", result), HttpStatus.OK);
	}
	
}
