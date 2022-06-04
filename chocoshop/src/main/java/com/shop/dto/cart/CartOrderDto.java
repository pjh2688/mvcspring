package com.shop.dto.cart;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartOrderDto {

	private Long cartItemId;
	
	// 1-1. 장바구니에 여러 개의 상품을 주문할 수 있으므로 CartOrderDto 클래스가 자기 자신을 List로 가지도록 하기 위해 생성.
	private List<CartOrderDto> cartOrderDtoList;
}
