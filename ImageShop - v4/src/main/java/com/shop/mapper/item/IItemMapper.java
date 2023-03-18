package com.shop.mapper.item;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.shop.domain.item.Item;

@Mapper
public interface IItemMapper {

	public int save(Item item);
	
	public List<Item> findAll();
	
	public Optional<Item> findByItemId(Long itemId);

	public int update(Item item);
	
	public int delete(Long itemId);
}
