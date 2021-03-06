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
	
	@OneToOne(fetch = FetchType.LAZY)  // 1-1. @OneToOne 어노테이션을 이용해 회원 엔티티와 일대일로 매핑(fetch 전략은 default가 EAGER)
	@JoinColumn(name = "member_id")  // 1-2. @JoinColumn 어노테이션을 이용해 외래키를 지정합니다. (Member table의 member_id를 외래키로 지정)
	private Member member;
	
	// 1-3. 회원 엔티티를 파라미터로 받아서 장바구니 엔티티를 생성해주는 메소드
	public static Cart createCart(Member member) {
		Cart cart = new Cart();
		cart.setMember(member);
		return cart;
	}
	
}
