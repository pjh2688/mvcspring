package com.shop.entity.order_item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.shop.entity.BaseEntity;
import com.shop.entity.item.Item;
import com.shop.entity.order.Order;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {

	@Id
	@Column(name = "order_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;
	
	private int orderPrice;
	
	private int count;

	// 1-1. 주문할 상품(Item)과 주문 수량(count)을 가지고 주문 상품(OrderItem) 객체를 만드는 메소드
	public static OrderItem createOrderItem(Item item, int count) {
		// 1-2. item과 count로 주문 상품(OrderItem) 객체 생성 및 세팅
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setCount(count);
		
		// 1-3. 상품 가격을 주문 가격으로 세팅.
		orderItem.setOrderPrice(item.getPrice());
		
		// 1-4. 상품에 주문 수량을 전달해 재고를 감소 시킨다.(더티체킹)
		item.removeStock(count);
		
		return orderItem;
	}
	
	// 2-1. 총 주문 가격 계산 메소드
	public int getTotalPrice() {
		// 2-2. 1-1에서 세팅해 놓은 걸로 총 주문 가격 구해서 리턴.
		return orderPrice * count;
	}
	
	// 3-1. 주문 취소 (재고 원복)
	public void cancel() {
		this.getItem().addStock(count);
	}
	
}

