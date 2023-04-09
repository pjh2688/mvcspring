package com.shop.mapper.item;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.shop.domain.item.UserItem;

@Mapper
public interface IUserItemMapper {

	public int save(UserItem userItem);
	
	public List<UserItem> findAllByUserNo(Long userNo);
	
	public Optional<UserItem> findByUserItemNo(Long userItemNo);
}
