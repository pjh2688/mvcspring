package com.shop.dto.order_item;

import com.shop.entity.order_item.OrderItem;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDto {

	// 1-1. 상품명
	private String itemNm;
	
	// 1-2. 주문 수량
	private int count;
	
	// 1-3. 주문 금액
	private int orderPrice;
	
	// 1-4. 상품 이미지 경로
	private String imgUrl;
	
	// 1-5. orderItem 엔티티와 이미지 경로를 파라미터로 받아서 OrderItemDto 멤버 변수들 초기화
	public OrderItemDto(OrderItem orderItem, String imgUrl) {
		this.itemNm = orderItem.getItem().getItemNm();
		this.count = orderItem.getCount();
		this.orderPrice = orderItem.getOrderPrice();
		this.imgUrl = imgUrl;
	}
}
