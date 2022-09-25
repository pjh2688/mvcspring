package com.photogram.service.image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.photogram.config.auth.PrincipalDetails;
import com.photogram.domain.image.Image;
import com.photogram.domain.image.ImageRepository;
import com.photogram.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;
	
	@Value("${file.path}")
	private String uploadFolder;
	
	// 1-1. 사진 업로드
	public void imageUpload(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		// 1-3. 파일명 + 확장자 가져오기
		String originalFileName = imageUploadDto.getFile().getOriginalFilename();
		
		// 1-4. 확장자 추출
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		// 1-5. UUID[Universally Unique IDentifier] : 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준 규약. 범용고유식별자.
		UUID uuid = UUID.randomUUID();
		
		// 1-6. UUID + 확장자 명으로 저장
		String imageFileName = uuid.toString() + extension;
		
		// 1-7. 실제 파일이  저장될 경로 + 파일명 저장.
		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
		
		// 1-8. 파일을 실제 저장 경로에 저장하는 로직 -> 통신 or I/O -> 예외가 발생할 수 있다(try-catch로 묶어서 처리)
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 1-6. 매개변수로 전달된 정보를 Image entity로 변환(Builder 패턴 이용)
		Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());
		
		// 1-7. image 엔티티 영속화하고 반환받는다.
		imageRepository.save(image);
	
	}
	
	// 2-1. 이미지스토리 불러오기 1 - 페이징 X
	@Transactional(readOnly = true)
	public List<Image> imageStory(Long principalId) {
		List<Image> images = imageRepository.mStory(principalId);
		return images;
	}
	
	// 3-1. 이미지스토리 불러오기 2 - 페이징 O
	@Transactional(readOnly = true)
	public Page<Image> imageStory(Long principalId, Pageable pageable) {
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		
		// 4-1. images에 좋아요 상태 값 담기
		images.forEach((image) -> {
			// 4-2. 이미지에 담겨있는 좋아요 데이터 가져오기
			image.getLikes().forEach((like) -> {
				// 4-3. 해당 이미지를 좋아요한 사람이 현재 로그인한 유저라면
				if(like.getUser().getId() == principalId) {
					// 4-4. 좋아요를 한 유저가 로그인한 유저라면 좋아요 상태 값을 true로 세팅 -> 로그인한 유저라면 story 페이지에서  이미지의 좋아요 하트 색깔을 빨간색으로 표시해주기 위해
					image.setLikeState(true);
				}
				
				image.setLikeCount(image.getLikes().size());
			});
			
		});
		return images;
	}
	
	// 4-1. 인기 사진 리스트 불러오기
	@Transactional(readOnly = true)
	public List<Image> imagePopular() {
		return imageRepository.mPopular();
	}
	
}