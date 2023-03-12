package com.shop.mapper.item;

import org.apache.ibatis.annotations.Mapper;

import com.shop.domain.item.UserItem;

@Mapper
public interface IUserItemMapper {

	public int save(UserItem userItem);
}
