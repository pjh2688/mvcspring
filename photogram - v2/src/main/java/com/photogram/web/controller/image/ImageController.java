package com.photogram.web.controller.image;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.photogram.config.auth.PrincipalDetails;
import com.photogram.domain.image.Image;
import com.photogram.dto.image.ImageUploadDto;
import com.photogram.handler.exception.CustomValidationException;
import com.photogram.service.image.ImageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ImageController {
	
	private final ImageService imageService;

	@GetMapping(value = {"/", "/image/story"})
	public String story(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		model.addAttribute("principal", principalDetails.getUser());
		return "image/story";
	}

	@GetMapping(value = "/image/popular")
	public String popular(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		List<Image> images = imageService.imagePopular();
		
		model.addAttribute("principal", principalDetails.getUser());
		model.addAttribute("images", images);
		
		return "image/popular";
	}
	
	@GetMapping(value = "/image/upload")
	public String upload(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		model.addAttribute("principal", principalDetails.getUser());
		
		return "image/upload";
	}
	
	@PostMapping(value = "/image")
	public String imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		if(imageUploadDto.getFile().isEmpty()) {
			throw new CustomValidationException("이미지가 첨부되지 않았습니다", null);
		}
		
		imageService.imageUpload(imageUploadDto, principalDetails);
		
		return "redirect:/user/" + principalDetails.getUser().getId();
	}
}
