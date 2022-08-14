package com.photogram.service.user;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

// import java.util.function.Supplier;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.photogram.domain.subscribe.SubscribeRepository;
import com.photogram.domain.user.User;
import com.photogram.domain.user.UserRepository;
import com.photogram.dto.user.UserProfileDto;
import com.photogram.handler.exception.CustomApiException;
import com.photogram.handler.exception.CustomException;
import com.photogram.handler.exception.CustomValidationApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final SubscribeRepository subscribeRepository;
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Transactional
	public User updateUser(Long id, User user) {
		/* 1-1. 영속화(일반)
		 *  - userRepository.findById(id).get();
		 *   -> DB에 해당 엔티티가 무조건 있다고 가정, 없으면 에러가 발생.
		 *  - userRepository.findById(id).orElseThrow()~~ 
		 *   -> 못찾았으면 exception 발생.
		 
		User userEntity = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {

			@Override
			public IllegalArgumentException get() {
				
				return new IllegalArgumentException("찾을 수 없는 id입니다.");
			}
			
		});
		*/
		
		// 1-2. 영속화(람다식)
		User userEntity = userRepository.findById(id).orElseThrow(() -> { 
			return new CustomValidationApiException("찾을 수 없는 id 입니다."); 
		});
		
		// 2-1. 영속화된 오브젝트를 수정 -> 더티체킹(변경감지)이 일어나서 업데이트 완료
		userEntity.setName(user.getName());
		
		String rawPassword = user.getPassword();
		String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
		
		userEntity.setPassword(encodedPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		
		return userEntity;
	}
	
	// 3-1. 프로필 보기
	@Transactional(readOnly = true)  // 3-3. 성능 향상을 위해 select도 transaction을 걸어준다.(select하는 작업은 읽기전용으로 만들어서 더티체킹을 안하게 하기 때문에 성능이 향상되는 효과가 있다. 만약 안걸어주면 jpa는 게속해서 변경감지를 하기 때문)
	public UserProfileDto selectProfile(Long pageUserId, Long principalId) {
		// 3-2. SELECT * FROM image WHERE userId = :userId
		User userEntity = userRepository.findById(pageUserId).orElseThrow( () -> {
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
		});
		
		// 3-4.
		UserProfileDto userProfileDto = new UserProfileDto();
		userProfileDto.setUser(userEntity);		
		userProfileDto.setPageOwnerState(pageUserId == principalId);  // 1 : 주인 , -1 : 주인 X
		userProfileDto.setImageCount(userEntity.getImages().size());
		
		// 4-1. 구독 상태 여부
		int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
		
		// 4-2. 구독 수
		int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
		
		userProfileDto.setSubscribeState(subscribeState  == 1);
		userProfileDto.setSubscribeCount(subscribeCount);
		
		// 5-1. 좋아요 수 같이 가져가기 -> 2022-07-17
		userEntity.getImages().forEach((image) -> {
			image.setLikeCount(image.getLikes().size());
		});
		
		return userProfileDto;
	}
	
	@Transactional
	public User updateProfileImage(Long principalId, MultipartFile profileImageFile) {
		// 6-1. 파일명 + 확장자 가져오기
		String originalFileName = profileImageFile.getOriginalFilename();
		
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
			Files.write(imageFilePath, profileImageFile.getBytes());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		User userEntity = userRepository.findById(principalId).orElseThrow( () -> {
			throw new CustomApiException("해당 유저를 찾을 수 없습니다.");
		});
		
		userEntity.setProfileImageUrl(imageFileName);
		
		return userEntity;
	}
	
}
