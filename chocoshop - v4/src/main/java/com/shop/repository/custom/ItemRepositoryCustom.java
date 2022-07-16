package com.shop.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shop.dto.item.ItemSearchDto;
import com.shop.dto.item.MainItemDto;
import com.shop.entity.item.Item;

public interface ItemRepositoryCustom {

	/* 
	 *  1-1. 상품 조회 조건을 담고 있는 itemSearchDto 객체와 페이징 정보를 담고 있는 pageable 객체를 파라미터 받는 getAdminItemPage 메소드를 정의합니다.
	 *       반환 데이터로 Page<Item> 객체를 반환 해줍니다.
	 */
	Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
	
	Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
