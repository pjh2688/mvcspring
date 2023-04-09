package com.shop.mapper.notice;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.shop.domain.notice.Notice;

@Mapper
public interface INoticeMapper {

	public int save(Notice notice);
	
	public List<Notice> findAll();
	
	public Optional<Notice> findByNoticeNo(Long noticeNo);
	
	public int update(Notice notice);
	
	public int delete(Long noticeNo);
	
}
