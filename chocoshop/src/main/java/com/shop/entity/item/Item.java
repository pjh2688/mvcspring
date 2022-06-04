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

	@Id
	@Column(name = "item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 1. 상품 코드
	
	@Column(nullable = false, length = 50)
	private String itemNm;  // 2. 상품명
	
	@Column(name = "price", nullable = false)
	private int price;  // 3. 상품가격
	
	@Column(nullable = false)
	private int stockNumber;  // 4. 재고수량
	
	@Lob  // BLOB, COLOB 타입 매핑
	@Column(nullable = false)
	private String itemDetail;  // 5. 상품 상세 설명
	
	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus;  // 6. 상품 판매 상태
	
	// 7. 상품 업데이트 기능
	public void updateItem(ItemFormDto itemFormDto) {
		this.itemNm = itemFormDto.getItemNm();
		this.price = itemFormDto.getPrice();
		this.stockNumber = itemFormDto.getStockNumber();
		this.itemDetail = itemFormDto.getItemDetail();
		this.itemSellStatus = itemFormDto.getItemSellStatus();
	}
	
	// 8-1. 상품 주문시 재고 감소시키는 기능
	public void removeStock(int stockNumber) {
		// 8-2. 남은 재고 = 현재 재고 - 주문 수량
		int restStock = this.stockNumber - stockNumber;
		
		// 8-3. 남은 재고가 0보다 작으면
		if(restStock < 0) {
			throw new OutOfStockException("상품의 재고가 부족합니다(현재 재고 수량 : " + this.stockNumber + ")");
		}
		
		// 8-4. 남은 재고량으로 세팅.
		this.stockNumber = restStock;
	}
	
	// 9-1. 상품 주문시 다시 재고 증가시키는 기능
	public void addStock(int stockNumber) {
		// 9-2. 주문했던 수량만큼 다시 증가시켜준다.
		this.stockNumber += stockNumber;
	}
	
}
