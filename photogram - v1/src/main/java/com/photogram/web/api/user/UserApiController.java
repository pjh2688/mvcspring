package com.photogram.web.api.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.photogram.config.auth.PrincipalDetails;
import com.photogram.domain.user.User;
import com.photogram.dto.CMRespDto;
import com.photogram.dto.subscribe.SubscribeDto;
import com.photogram.dto.user.UserUpdateDto;
import com.photogram.service.subscribe.SubscribeService;
import com.photogram.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {
	
	private final UserService userService;
	private final SubscribeService subScribeService;
	
	@GetMapping("/user/{pageUserId}/subscribe")
	public ResponseEntity<?> subscribeList(@PathVariable Long pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		Map<String, Object> result = new HashMap<>();
		
		List<SubscribeDto> subscribeDtos = subScribeService.getSubscribeList(principalDetails.getUser().getId(), pageUserId);
		
		try {
			result.put("code", HttpStatus.OK);
			result.put("message", "구독자 정보 리스트 불러오기 성공");
			result.put("list", subscribeDtos);
		} catch (Exception e) {
			result.put("message", "fail");
		}
		
		return new ResponseEntity<>(new CMRespDto<>(1, "구독자 정보 리스트 조회 성공", result), HttpStatus.OK);
	}

	@PutMapping("/user/{id}")
	public CMRespDto<?> update(@PathVariable Long id, @Valid UserUpdateDto params, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) { // 2. 유효성 검사 @Valid, BindingResult
		User userEntity =  userService.updateUser(id, params.toEntity());
			
		// 1-1. 세션정보도 변경된 엔티티로 업데이트
		principalDetails.setUser(userEntity);
			
		/*
		 * 1-2. 응답시에 userEntity의 모든 getter 함수가 호출되고 JSON으로 파싱해서 응답한다.(양방향 매핑시 오류가 남, User에 있는 양방향 매핑된 객체에 rest통신시에는 무시하도록 @JsonIgnoreProperties를 해줘야 에러가 안난ㄷ.)
		*/
		return new CMRespDto<>(1, "회원수정완료", userEntity);
		
	}
	
	@PutMapping("/user/{principalId}/profileImageUrl")
	public ResponseEntity<?> updateProfileImage(@PathVariable Long principalId, MultipartFile profileImageFile, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 2-1. 사진 변경하고
			User userEntity = userService.updateProfileImage(principalId, profileImageFile);
			
			// 2-2. 변경된 엔티티를 set
			principalDetails.setUser(userEntity);
			
			result.put("code", HttpStatus.OK);
			result.put("message", "프로필 사진 변경 성공");
		
		} catch (Exception e) {
			result.put("message", "fail");
		}
		
		return new ResponseEntity<>(new CMRespDto<>(1, "프로필 이미지 변경 성공", result), HttpStatus.OK);
	}

}

/*
 *	 -- 로그인은 admin(id=1)이했고 구독 정보를 보는건 pjh2688(id=2) 
 	 -- 한마디로 pjh2688(id=2)가 구독하고 있는 구독 정보를 보기 위함
 	 -- pjh2688(id=2)는 1, 3, 4을 구독하고 있음
 
		SELECT * FROM subscribe;
 
		SELECT * FROM user;

		SELECT * FROM subscribe WHERE fromUserId = 2;

	-- 무식한 방법
		SELECT * FROM user WHERE id = 1 OR id = 3 OR id = 4;

	-- 조인(user.id = subscribe.toUserId)
		SELECT u.id, u.username, u.profileImageUrl 
			FROM user u INNER JOIN subscribe s ON u.id = s.toUserId
			WHERE s.fromUserId = 2;
	
	-- 로그인(1), 화면(1,3) -> 구독 여부 확인
		SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = 3;

	-- 가상 컬럼 추가
		SELECT u.id, u.username, u.profileImageUrl, 1 AS subscribeState
			FROM user u INNER JOIN subscribe s ON u.id = s.toUserId
			WHERE s.fromUserId = 2;
	
	-- 스칼라 서브 쿼리 -> select 절에 select가 한 번 더 나오는 쿼리(단일행을 리턴해야 함.)
		SELECT u.id, u.username, u.profileImageUrl, 
				(SELECT count(*) FROM user) AS subscribeState
			FROM user u INNER JOIN subscribe s ON u.id = s.toUserId
			WHERE s.fromUserId = 2;
	
	-- 구독 여부 완성 쿼리 -> 참고 :  SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = 3;
		SELECT u.id, u.username, u.profileImageUrl, 
				(SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = u.id) AS subscribeState
			FROM user u INNER JOIN subscribe s ON u.id = s.toUserId
			WHERE s.fromUserId = 2;
	
	-- 동일 유저인지 판단 쿼리1
		SELECT u.id, u.username, u.profileImageUrl, 
				(SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = u.id) AS subscribeState,
				1 equlaUserState
			FROM user u INNER JOIN subscribe s ON u.id = s.toUserId
			WHERE s.fromUserId = 2;
	
	-- 동일 유저인지 판단 쿼리2
		SELECT u.id, u.username, u.profileImageUrl, 
				(SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = u.id) AS subscribeState,
				(1=u.id) as equalUserState
			FROM user u INNER JOIN subscribe s ON u.id = s.toUserId
			WHERE s.fromUserId = 2;
	
	-- 동일 유저인지 판단 쿼리3 - if로 마무리
		SELECT u.id, u.username, u.profileImageUrl, 
			if((SELECT 1 FROM subscribe WHERE fromUserId = 1 AND toUserId = u.id), 1, 0) AS subscribeState,
			if((1=u.id), 1, 0) as equalUserState
			FROM user u INNER JOIN subscribe s ON u.id = s.toUserId
			WHERE s.fromUserId = 2;


 * 
 */
