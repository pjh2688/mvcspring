package com.shop.entity.cart_item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.shop.entity.BaseEntity;
import com.shop.entity.cart.Cart;
import com.shop.entity.item.Item;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity {
	
	@Id
	@Column(name = "cart_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id")
	private Cart cart;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	private int count;
	
	// 1-1. 장바구니 엔티티와 상품 엔티티를 연결해서 CartItem 엔티티를 만들어주는 메소드
	public static CartItem createCartItem(Cart cart, Item item, int count) {
		CartItem cartItem = new CartItem();
		cartItem.setCart(cart);
		cartItem.setItem(item);
		cartItem.setCount(count);
		return cartItem;
	}
	
	// 1-2. 장바구니에 기존에 담겨 있는 상품인데, 해당 상품을 추가로 장바구니에 담을 때 기존 수량에 현재 담을 수량을 추가해줄 때 사용할 메소드.
	public void addCount(int count) {
		this.count += count;
	}
	
	// 1-3. 현재 장바구니에 담겨있는 수량을 변경하는 메소드
	public void updateCount(int count) {
		this.count = count;
	}
}
