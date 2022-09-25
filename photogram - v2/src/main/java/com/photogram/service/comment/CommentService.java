package com.photogram.service.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.photogram.domain.comment.Comment;
import com.photogram.domain.comment.CommentRepository;
import com.photogram.domain.image.Image;
import com.photogram.domain.image.ImageRepository;
import com.photogram.domain.user.User;
import com.photogram.domain.user.UserRepository;
import com.photogram.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final ImageRepository imageRepository;
	
	public Comment writeComment(String content, Long imageId, Long principalId) {
		
		// Tip : 객체를 만들 때 id값만 담아서 insert 할 수 있다.
		// 대신 return 시에 image 객체와 user 객체는 id값만 가지고 있는 빈 객체만 들고 온다.
		// 댓글 로직이  API 통신을 하기 때문에 CustomApiException 반환.
		Image imageEntity = imageRepository.findById(imageId).orElseThrow(() -> {
			throw new CustomApiException("해당 이미지를 찾을 수 없습니다.");
		});
		
		User userEntity = userRepository.findById(principalId).orElseThrow(() -> {
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다.");
		});
		
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(imageEntity);
		comment.setUser(userEntity);
		
		return commentRepository.save(comment);
	}
	
	public void deleteComment(Long commentId) {
		try {
			commentRepository.deleteById(commentId);
		} catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
		
	}
}
