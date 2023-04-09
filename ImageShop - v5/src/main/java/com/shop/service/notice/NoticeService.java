package com.shop.service.notice;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.domain.notice.Notice;
import com.shop.handler.exception.CustomApiException;
import com.shop.mapper.notice.INoticeMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class NoticeService {

	private final INoticeMapper noticeMapper;
	
	public int register(Notice notice) {
		
		int result = 0;
		
		try {
			result = noticeMapper.save(notice);
		} catch (Exception e) {
			throw new CustomApiException("공지글 저장 실패");
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public List<Notice> findAll() {
		
		List<Notice> result = null;
		
		try {
			result = noticeMapper.findAll();
			
		} catch (Exception e) {
			throw new CustomApiException("공지글 리스트 정보가 존재하지 않습니다.");
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public Notice findByNoticeNo(Long noticeNo) {
		Optional<Notice> noticeOp = noticeMapper.findByNoticeNo(noticeNo);
		
		if(noticeOp.isPresent()) {
			return noticeOp.get();
			
		} else {
			throw new CustomApiException("해당 게시글이 존재하지 않습니다.");
		}
	}
	
	public int update(Notice notice) {
		Optional<Notice> noticeOp = noticeMapper.findByNoticeNo(notice.getNoticeNo());
		
		if(noticeOp.isPresent()) {
			int result = noticeMapper.update(notice);
			
			return result;
			
		} else {
			throw new CustomApiException("해당 공지글이 존재하지 않습니다.");
		}	
	}
	
	public int delete(Long noticeNo) {
		Optional<Notice> noticeOp = noticeMapper.findByNoticeNo(noticeNo);
		
		if(noticeOp.isPresent()) {
			
			int result = noticeMapper.delete(noticeNo);
			
			return result;
			
		} else {
			throw new CustomApiException("해당 공지글이 존재하지 않습니다.");
		}	
	}
}
