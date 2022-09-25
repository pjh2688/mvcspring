package com.photogram.service.user;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
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

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final SubscribeRepository subscribeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${file.path}")
	private String uploadFolder;
	
	
	public User userUpdate(Long id, User user) {
	
		/* 1-1. 영속화 1 -> 람다식 X
		User userEntity = userRepository.findById(id).orElseThrow(new Supplier<CustomValidationApiException>() {
			@Override
			public CustomValidationApiException get() {
				return new CustomValidationApiException("찾을 수 없는 ID 입니다.");
			}
		});
		
		*/
		/* 1-3. 영속화 2 -> 람다식 O */
		User userEntity = userRepository.findById(id).orElseThrow( () -> {
			return new CustomValidationApiException("찾을 수 없는 ID 입니다.");
		});
		
		String rawPassword = user.getPassword();
		String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
		
		// 1-2. 영속화된 엔티티를 수정 -> 더티체킹
		userEntity.setName(user.getName());
		userEntity.setPassword(encodedPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		
		return userEntity;
	}
	
	@Transactional(readOnly = true)
	public UserProfileDto userProfile(Long pageUserId, Long principalId) {
		
		UserProfileDto dto = new UserProfileDto();
		
		// 2-1. SELECT * FROM image WHEHRE userId = :userId
		User userEntity = userRepository.findById(pageUserId).orElseThrow(() -> {
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
		});
		
		int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
		int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
		
		dto.setUser(userEntity);
		dto.setIsPageOwner(pageUserId == principalId);  // 2-2. true는 주인 O , false는 주인 X
		dto.setImageCount(userEntity.getImages().size());  // 2-3. 이미지 갯수 셋팅
		dto.setSubscribeCount(subscribeCount);
		dto.setSubscribeState(subscribeState == 1); 
		
		return dto;
	}
	
	public User userProfileUpdate(Long PrincipalId, MultipartFile profileImageFile) {
		// 3-1. 파일명 + 확장자 가져오기
		String originalFileName = profileImageFile.getOriginalFilename();
				
		// 3-2. 확장자 추출
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
				
		// 3-3. UUID[Universally Unique IDentifier] : 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준 규약. 범용고유식별자.
		UUID uuid = UUID.randomUUID();
				
		// 3-4. UUID + 확장자 명으로 저장
		String imageFileName = uuid.toString() + extension;
				
		// 3-5. 실제 파일이  저장될 경로 + 파일명 저장.
		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
				
		// 3-6. 파일을 실제 저장 경로에 저장하는 로직 -> 통신 or I/O -> 예외가 발생할 수 있다(try-catch로 묶어서 처리)
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 3-7.
		User userEntity = userRepository.findById(PrincipalId).orElseThrow(() -> {
			throw new CustomApiException("해당 유저를 찾을 수 없습니다.");
		});
		
		userEntity.setProfileImageUrl(imageFileName);
		
		return userEntity;
	}
}