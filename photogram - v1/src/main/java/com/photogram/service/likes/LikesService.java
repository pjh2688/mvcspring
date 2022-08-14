package com.photogram.service.likes;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.photogram.domain.likes.LikesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikesService {

	private final LikesRepository likesRepository;
	
	@Transactional
	public void likes(Long imageId, Long principalId) {
		likesRepository.mLikes(imageId, principalId);
	}
	
	@Transactional
	public void unLikes(Long imageId, Long principalId) {
		likesRepository.mUnLikes(imageId, principalId);
	}
}
