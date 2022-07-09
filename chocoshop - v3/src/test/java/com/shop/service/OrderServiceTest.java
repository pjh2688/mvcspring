package com.shop.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.shop.constant.item.ItemSellStatus;
import com.shop.constant.order.OrderStatus;
import com.shop.dto.order.OrderDto;
import com.shop.entity.item.Item;
import com.shop.entity.item.ItemRepository;
import com.shop.entity.member.Member;
import com.shop.entity.member.MemberRepository;
import com.shop.entity.order.Order;
import com.shop.entity.order.OrderRepository;
import com.shop.entity.order_item.OrderItem;
import com.shop.service.order.OrderService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;

@SpringBootTest
@Transactional
class OrderServiceTest {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	MemberRepository memberRepository;

	// 1-1. 주문할 상품 엔티티 저장 후 반환.
	public Item saveItem() {
		Item item = new Item();
		item.setItemNm("테스트물품");
		item.setPrice(10000);
		item.setItemDetail("테스트물품 상세 설명");
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		
		return itemRepository.save(item);
	}
	
	// 1-2. 주문한 회원 엔티티 저장 후 반환
	public Member saveMember() {
		Member member = new Member();
		member.setEmail("test@test.com");
		return memberRepository.save(member);
	}
	
	// 1-3. 주문 테스트
	@Test
	@DisplayName("주문 테스트")
	public void order() {
		Item item = saveItem();
		Member member = saveMember();
		
		// 1-4. 주문할 상품과 수량을 OrderDto객체에 세팅
		OrderDto orderDto = new OrderDto();
		orderDto.setItemId(item.getId());
		orderDto.setCount(10);
		
		// 1-5. 주문 서비스 실행후 주문 아이디 반환 받는다.
		Long orderId = orderService.order(orderDto, member.getEmail());
		
		// 1-6. 1-5에서 반환된 주문 아이디로 DB에서 해당 주문을 가져온다
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		
		// 1-7. 주문 엔티티에 저장된 주문아이템 정보를 가지고 온다.
		List<OrderItem> orderItems = order.getOrderItems();
		
		for(OrderItem orderItem : orderItems) {
			System.out.println(orderItem.getItem().getItemNm());
		}
		
		// 1-8. 주문한 상품의 총 가격을 구한다.
		int totalPrice = orderDto.getCount() * item.getPrice();
		
		// 1-9. 1-8에서 구헌 주문한 상품의 총 가격과 데이터베이스에 저장된 상품의 총 가격을 비교하여 같으면 테스트 성공
		assertEquals(totalPrice, order.getTotalPrice());
	}
	
	@Test
	@DisplayName("주문 취소 테스트")
	public void cancelOrder() {
		Item item = saveItem();
		Member member = saveMember();
		
		OrderDto orderDto = new OrderDto();
		orderDto.setCount(10);
		orderDto.setItemId(item.getId());
		
		Long orderId = orderService.order(orderDto, member.getEmail());
		
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		
		orderService.cancelOrder(orderId);
		
		assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
		assertEquals(100, item.getStockNumber());
		
	}
	
}
