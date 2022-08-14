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

@RequiredArgsConstructor
@Service
public class ImageService {

	// 1-2. 
	private final ImageRepository imageRepository;
	
	// 1-6. 스프링부트 설정 파일(application.properties)에 등록된 경로 가져오기.
	@Value("${file.path}")
	private String uploadFolder;
	
	// 1-1. 사진 업로드 메소드
	@Transactional
	public void uploadPicture(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		
		// 1-3. 파일명 + 확장자 가져오기
		String originalFileName = imageUploadDto.getFile().getOriginalFilename();
		
		// 1-4. 확장자 추출
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		// 1-5. UUID[Universally Unique IDentifier] : 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준 규약. 범용고유식별자.
		UUID uuid = UUID.randomUUID();
		
		// 1-6. UUID + 확장자 명으로 저장
		String imageFileName = uuid.toString() + extension;
		
//		System.out.println("이미지 파일이름 : " + imageFileName);
		
		// 1-5. 실제 파일이  저장될 경로 + 파일명 저장.
		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
		
		// 1-6. 통신, DB I/O 할때는 에외 처리 꼭 해줘야 한다.
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 1-7. DB image 테이블에 저장
		Image image = imageUploadDto.toEntity(originalFileName, imageFileName, principalDetails.getUser());
		imageRepository.save(image);
		
		// 1-8. 양방향 매핑시 이걸 출력하면 에러가난다. -> JPA에선 sysout 주석 확인
	//	System.out.println(imageEntity);
	}
	
	// 2-1.
	@Transactional(readOnly = true)
	public Page<Image> imageStory(Long principalId, Pageable pageable) {
		// 2-2. 해당 페이지의 주인이 구독하고 있는 회원의 이미지들을 가져온다.
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		
		// 2-3. 2-2에서 가져온 이미지를 for문을 돌려 하나하나 가져온다.
		images.forEach( (image) -> {			
			// 2-4. 2-3에서 가져온 이미지의 좋아요 정보를 가져온다. 
			image.getLikes().forEach( (like) -> {
				// 2-5. 2-4에서 가져온 좋아요 정보가 principal에서 가져온 User 정보랑 같은지 비교
				if(like.getUser().getId() == principalId) {  // 2-6. 해당 이미지에 좋아요를 누른 사람들을 찾아서 현재 로그인한 사람이 좋아요를 한것인지 비교
					image.setLikeState(true);
				}
				
				// 2-7. 좋아요 수 세팅
				image.setLikeCount(image.getLikes().size());
				
			});
					
		});
		
		return images;
	}
	
	// 3-1. 인기 사진 서비스(좋아요가 많은 순)
	
	public List<Image> imagePopular() {
		return imageRepository.mPopular();
	}
	
}
