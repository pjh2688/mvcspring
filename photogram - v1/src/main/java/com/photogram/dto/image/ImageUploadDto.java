package com.photogram.dto.image;

import org.springframework.web.multipart.MultipartFile;

import com.photogram.domain.image.Image;
import com.photogram.domain.user.User;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ImageUploadDto {
	
	private MultipartFile file;
	private String caption;
	
	// 1-1. entity 변환
	public Image toEntity(String imageName, String postImageUrl, User user) {
		return Image.builder()
				.name(imageName)
				.caption(caption)
				.postImageUrl(postImageUrl)
				.user(user)
				.build();
	}
}
