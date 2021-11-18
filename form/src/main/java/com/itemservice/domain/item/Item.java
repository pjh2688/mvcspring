package com.itemservice.domain.item;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Item {

	private Long id;  // 3. pk
	private String itemName;  // 4. 상품 이름
	private Integer price;  // 5. 상품 가격
	private Integer quantity;  // 6. 상품 수량
	
	private Boolean open; // 7. 판매 여부
	private List<String> regions; // 8. regions
	private ItemType itemType;  // 9. 상품 종류
	private String deliveryCode;  // 10. 배송 방식
	
	// 1. 기본 생성자
	public Item() {
		
	}
	
	// 2. id를 제외한 생성자
	public Item(String itemName, Integer price, Integer quantity) {
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
	}
	
	
}

/*
 *	CREATE TABLE item(
		`item_id` INT(11) UNSIGNED  NOT NULL AUTO_INCREMENT,
		`item_name` VARCHAR(40) NOT NULL,
		`item_price` INTEGER DEFAULT 0,
		`item_quantity` INTEGER DEFAULT 0,
		PRIMARY KEY(item_id)
	); 
 */
