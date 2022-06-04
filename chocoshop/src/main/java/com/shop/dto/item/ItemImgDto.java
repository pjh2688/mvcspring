package com.shop.dto.item;

import org.modelmapper.ModelMapper;

import com.shop.entity.item.ItemImg;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemImgDto {

	private Long id;
	
	private String imgName;
	
	private String oriImgName;
	
	private String imgUrl;
	
	private String repImgYn;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static ItemImgDto of(ItemImg itemImg) {
		return modelMapper.map(itemImg, ItemImgDto.class);
	}
}
