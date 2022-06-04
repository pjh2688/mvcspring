package com.shop.entity.cart_item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shop.dto.cart.CartItemDetailDto;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	
	// 1-1. Cart 아이디와 Item 아이디를 이용하여 상품이 장바구니에 들어 있는지 조회.
	CartItem findByCartIdAndItemId(Long cartId, Long itemId);
	
	// 1-2. 
	@Query("select new com.shop.dto.cart.CartItemDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " + 
			"from CartItem ci, ItemImg im " + 
			"join ci.item i " +
			"where ci.cart.id = :cartId " + 
			"and im.item.id = ci.item.id " + 
			"and im.repimgYn = 'Y' " + 
			"order by ci.regTime desc")
	List<CartItemDetailDto> findCartItemDetailDtoList(@Param("cartId") Long cartId);
	
}
