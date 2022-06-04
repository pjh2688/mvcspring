package com.shop.entity.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.shop.constant.order.OrderStatus;
import com.shop.entity.member.Member;
import com.shop.entity.order_item.OrderItem;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

	@Id
	@Column(name = "order_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<OrderItem> orderItems = new ArrayList<>();
	
	private LocalDateTime orderDate;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@CreatedDate  
	@Column(updatable = false)
	private LocalDateTime regTime;
	
	@LastModifiedDate
	private LocalDateTime updateTime;
	
	// 1-1. 주문 상품 정보를 담아주는 메소드
	public void addOrderItem(OrderItem orderItem) {
		// 1-2. orderItems에 주문 상품 정보를 담는다.
		orderItems.add(orderItem);
		// 1-3. Order 엔티티와 OrderItem 엔티티는 양방향 관계이므로 orderItem 객체에서도 order를 참조할 수 있게 order 객체를 세팅
		orderItem.setOrder(this);
	}
	
	// 2-1. 주문 생성 메소드
	public static Order createOrder(Member member, List<OrderItem> orderItemList) {
		Order order = new Order();
		
		// 2-2. 상품을 주문한 회원 정보를 세팅
		order.setMember(member);
		
		// 2-3. 여러개를 주문 할수도 있으므로 리스트형태로 값을 받아서 세팅합니다.
		for(OrderItem orderItem : orderItemList) {
			// 2-4. 주문 엔티티에 OrderItem(주문상품) 셋팅
			order.addOrderItem(orderItem);
		}
		
		// 2-5. 주문 상태 ORDER로 세팅
		order.setOrderStatus(OrderStatus.ORDER);
		
		// 2-6. 주문 시간을 현재시간으로 세팅
		order.setOrderDate(LocalDateTime.now());
		
		return order;
	}
	
	// 3-1. 총 주문 금액을 구하는 메소드
	public int getTotalPrice() {
		int totalPrice = 0;
		
		for(OrderItem orderItem : orderItems) {
			totalPrice += orderItem.getTotalPrice();
		}
		
		return totalPrice;
	}
	
	// 4-1. 주문 취소 메소드
	public void cancelOrder() {
		this.orderStatus = OrderStatus.CANCEL;
		
		for(OrderItem orderItem : orderItems) {
			// 4-2. 주문 아이템들을 반복문을 돌며 취소.
			orderItem.cancel();
		}
	}
}
