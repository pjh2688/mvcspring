package com.photogram.web.api.comment;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photogram.config.auth.PrincipalDetails;
import com.photogram.domain.comment.Comment;
import com.photogram.dto.CMRespDto;
import com.photogram.dto.comment.CommentDto;
import com.photogram.service.comment.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentApiController {

	private final CommentService commentService;
	
	@PostMapping("/comment")
	public ResponseEntity<?> commentSave(@Valid @RequestBody CommentDto commentDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		Comment comment = commentService.writeComment(commentDto.getContent(), commentDto.getImageId(), principalDetails.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "댓글 쓰기 성공", comment), HttpStatus.CREATED);
		
	}
	
	@DeleteMapping("/comment/{id}")
	public ResponseEntity<?> commentDelete(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		commentService.deleteComment(id);
		return new ResponseEntity<>(new CMRespDto<>(1, "댓글 삭제 성공", null), HttpStatus.OK);
	}
}
