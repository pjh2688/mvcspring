package com.itemservice.repository.item;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.itemservice.domain.item.Item;
import com.itemservice.domain.item.ItemDto;

@Mapper
public interface IItemMapper {
	
	public int save(Item item);
	
	public List<Item> findAll();
	
	public Item findById(Long id);
	
	public ItemDto findByItem(Long id);

	public int update(Item updateParam);
	
	public int delete(Long id);
}
