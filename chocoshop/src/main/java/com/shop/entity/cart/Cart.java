package com.shop.entity.cart;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.shop.entity.BaseEntity;
import com.shop.entity.member.Member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Getter @Setter
@ToString
public class Cart extends BaseEntity {

	@Id
	@Column(name = "cart_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	// 1-1. 회원 1명당 1개의 장바구니를 갖으므로 처음 장바구니에 상품을 담을 때는 해당 회원의 장바구니를 생성해줘야 합니다.
	public static Cart createCart(Member member) {
		Cart cart = new Cart();
		cart.setMember(member);
		return cart;
	}
	
	
}
