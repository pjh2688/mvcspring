package com.shop.entity.item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.shop.constant.item.ItemSellStatus;
import com.shop.dto.item.ItemFormDto;
import com.shop.entity.BaseEntity;
import com.shop.exception.order.OutOfStockException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "item")
@Getter @Setter
@ToString
public class Item extends BaseEntity {

	// 1-1. 상품 코드(PK)
	@Id
	@Column(name = "item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 1-2. 상품 명
	@Column(nullable = false, length = 50)
	private String itemNm;
	
	// 1-3. 가격
	@Column(name = "price", nullable = false)
	private int price;
	
	// 1-4. 재고 수량
	@Column(nullable = false)
	private int stockNumber;
	
	// 1-5. 상품 상세 설명
	@Lob
	@Column(nullable = false)
	private String itemDetail;
	
	// 1-6. 상품 판매 상태(SELL, SOLD_OUT)
	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus;
	
	// 1-7. 상품 업데이트(더티 체킹 이용)
	public void updateItem(ItemFormDto itemFormDto) {
		this.itemNm = itemFormDto.getItemNm();
		this.price = itemFormDto.getPrice();
		this.stockNumber = itemFormDto.getStockNumber();
		this.itemDetail = itemFormDto.getItemDetail();
		this.itemSellStatus = itemFormDto.getItemSellStatus();
	}
	
	// 1-8. 재고 감소(더티 체킹 이용)
	public void removeStock(int stockNumber) {
		int restStock = this.stockNumber - stockNumber;
		
		if(restStock < 0) {
			throw new OutOfStockException("상품의 제고가 부족합니다. (현재 재고 수량 : " + this.stockNumber + ")");
		}
		
		this.stockNumber = restStock;
	}
	
	// 1-9. 상품 재고 증가
	public void addStock(int stockNumber) {
		this.stockNumber += stockNumber;
	}
	
}
