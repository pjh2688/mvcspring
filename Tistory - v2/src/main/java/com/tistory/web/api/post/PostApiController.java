package com.tistory.web.api.post;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tistory.domain.user.User;
import com.tistory.service.auth.PrincipalDetails;
import com.tistory.service.post.PostService;
import com.tistory.web.dto.CMRespDto;
import com.tistory.web.dto.love.LoveDto;
import com.tistory.web.dto.post.PostUpdateDto;
import com.tistory.web.dto.post.PostWriteReqDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostApiController {

	private final PostService postService;
	
	@PostMapping("/user/{principalId}/post/write")
	public ResponseEntity<?> writePost(@Valid final PostWriteReqDto postWriteReqDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) { 
		
		
		User principal = principalDetails.getUser();
		
		postService.writePost(postWriteReqDto, principal);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "포스팅 글쓰기 성공", postWriteReqDto), HttpStatus.OK);
	}
	
	@PutMapping("/user/{pageOwnerId}/post/{postId}/update")
	public ResponseEntity<?> updatePost(@PathVariable Long postId, @Valid final PostUpdateDto postUpdateDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		
		User principal = principalDetails.getUser();
		
		postService.updatePost(postUpdateDto, postId, principal);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "포스팅 정보 수정 성공", postUpdateDto), HttpStatus.OK);	
		
	}
	
	@DeleteMapping("/user/{pageOwnerId}/post/{postId}")
	public ResponseEntity<?> deletePost(@PathVariable Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User principal = principalDetails.getUser();
		
		postService.deletePost(postId, principal);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "포스팅 삭제 성공", null), HttpStatus.OK);	
	}
	
	@PostMapping("/user/{pageOwnerId}/post/{postId}/love")
	public ResponseEntity<?> lovePost(@PathVariable Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User principal = principalDetails.getUser();
		
		LoveDto loveDto = postService.lovePost(postId, principal);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "포스팅 좋아요 성공", loveDto), HttpStatus.OK);	
	}
	
	@DeleteMapping("/user/{pageOwnerId}/post/{postId}/love/{loveId}")
	public ResponseEntity<?> unLovePost(@PathVariable Long loveId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User principal = principalDetails.getUser();
		
		LoveDto unLoveDto = postService.unLovePost(loveId, principal);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "포스팅 좋아요 취소 성공", unLoveDto), HttpStatus.OK);	
	}
}

// 2022-11-01 -> 햄버거 메뉴에 방문객 수, 카테고리명 표시까지 완료.
