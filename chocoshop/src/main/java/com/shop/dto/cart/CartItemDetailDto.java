package com.shop.dto.cart;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartItemDetailDto {

	// 1-1. 장바구니 상품 아이디
	private Long cartItemId;
	
	// 1-2. 상품명
	private String itemNm;
	
	// 1-3. 상품 가격
	private int price;
	
	// 1-4. 수량
	private int count;
	
	// 1-5. 상품 이미지 경로
	private String imgUrl;
	
	// 1-6. 생성자
	public CartItemDetailDto(Long cartItemId, String itemNm, int price, int count, String imgUrl) {
		this.cartItemId = cartItemId;
		this.itemNm = itemNm;
		this.price = price;
		this.count = count;
		this.imgUrl = imgUrl;
	}
	
}
