package com.tistory.service.user;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tistory.domain.user.User;
import com.tistory.domain.user.UserRepository;
import com.tistory.domain.visit.Visit;
import com.tistory.domain.visit.VisitRepository;
import com.tistory.handler.exception.CustomApiException;
import com.tistory.handler.exception.CustomException;
import com.tistory.util.email.EmailUtil;
import com.tistory.web.dto.user.PasswordResetDto;
import com.tistory.web.dto.user.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository; 
	private final VisitRepository visitRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailUtil emailUtil;
	
	@Value("${file.path}")
	private String uploadFolder;
	
	public User join(User user) {
		// 1-1. 비밀번호 암호화해서 저장.
		String rawPassword = user.getPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encodedPassword);
		
		// 1-3. 중복되는 아이디  처리
		User findUser = userRepository.findByUsername(user.getUsername());
		
		Visit visit = new Visit(0L, user);
	
		if(findUser == null) {
			// 1-2. 영속화
			User userEntity = userRepository.save(user);
			
			// 1-3. 회원가입시 방문정보 엔티티 영속화
			visitRepository.save(visit);
			
			return userEntity;
			
		} else {
			throw new CustomException("이미 가입한 유저입니다.");
		}
	}
	
	public void resetPassword(PasswordResetDto passwordResetReqDto) {
		// 2-1. 전송 받은  username, email이 DB에 있는지 체크
		Optional<User> findUser = userRepository.findByUsernameAndEmail(passwordResetReqDto.getUsername(), passwordResetReqDto.getEmail());
	
		String email = "";
		
		// 2-2. 같은 것이 있다면
		if(findUser.isPresent()) {
			// 2-3. 찾은 User를 영속화해서 가져온다.
			User userEntity = findUser.get();
			
			email = userEntity.getEmail();
			
			// 2-4. 비밀번호를 9999로 초기화한 뒤 encoded
			String encodedPaswoord = passwordEncoder.encode("9999");
			
			// 2-5. 변경된 인코딩된 비밀번호를 User에 셋팅
			userEntity.setPassword(encodedPaswoord);
		} else {
            throw new CustomException("해당 이메일이 존재하지 않습니다.");
        }
		
		// 2-6. 초기화된 비밀번호를 해당 계정 이메일로 전송해서 알려준다.
		emailUtil.sendEmail(email, "비밀번호 초기화", "초기화된 비밀번호 : 9999");
	}
	
	public User userProfileUpdate(Long principalId, MultipartFile profileImageFile) {
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
		
		// 3-7. 로그인 유저 정보를 가져온다.
		User userEntity = userRepository.findById(principalId).orElseThrow(() -> {
			throw new CustomApiException("해당 유저를 찾을 수 없습니다.");
		});
		
		// 3-8. 변경감지를 이용해 set을 호출하면 업데이트
		userEntity.setProfileImageUrl(imageFileName);
		
		return userEntity;
	}
	
	public User userUpdate(Long principalId, UserUpdateDto userUpdateDto) {
		// 4-1. 로그인 유저 정보를 가져온다.
		User userEntity = userRepository.findById(principalId).orElseThrow(() -> {
			throw new CustomApiException("해당 유저를 찾을 수 없습니다.");
		});
		
		// 4-2. 회원 정보 변경 화면에서 가져온 계정의 비밀번호를 가져온다.
		String rawPassword = userUpdateDto.getPassword();
		
		// 4-3. 회원 정보 변경 화면에서 가져온 계정의 변경할 비밀번호를 가져온다.
		String changedPassword = userUpdateDto.getConvertPassword();
		
		// 4-4. passwordEncoder의 matches 메소드는 인코딩된 비밀번호가 매개변수로 들어온 문자열과 일치하는지 내부적으로 확인해주고 맞으면 true, 아니면 false를 반환. 
		if(passwordEncoder.matches(rawPassword, userEntity.getPassword())) {
			// 4-5. 4-4를 통과했다면 계정의 원래 비밀번호를 정상적으로 맞게 입력했다는 의미이므로 변경할 비밀번호를 인코딩해서 encodedPassword에 저장한다.
			String encodedPassword = passwordEncoder.encode(changedPassword);
			
			// 4-6. 비밀번호 변경(영속화) -> 변경 감지.
			userEntity.setPassword(encodedPassword);
		} else {
			throw new CustomApiException("현재 비밀번호가 일치하지 않습니다.");
		}
	
		return userEntity;
	}
}

/*
 * https://kitty-geno.tistory.com/43 -> Spring Boot 메일 발송하기 
 */
