package com.shop.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.shop.constant.item.ItemSellStatus;
import com.shop.entity.item.Item;
import com.shop.entity.item.ItemRepository;
import com.shop.entity.member.Member;
import com.shop.entity.member.MemberRepository;
import com.shop.entity.order.Order;
import com.shop.entity.order.OrderRepository;
import com.shop.entity.order_item.OrderItem;
import com.shop.entity.order_item.OrderItemRepository;

@SpringBootTest
@Transactional
class OrderTest {

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Autowired
	EntityManager em;
	
	@Test
	public Item createItem() {
		Item item = new Item();
		item.setItemNm("테스트 상품");
		item.setPrice(10000);
		item.setItemDetail("상세설명");
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		item.setRegTime(LocalDateTime.now());
		
		return item;
	}
	
	@Test
	@DisplayName("영속성 전이 테스트")
	public void cascadeTest() {
		
		Order order = new Order();
		
		for(int i = 0; i < 3; i++) {
			Item item = createItem();
			itemRepository.save(item);
			
			OrderItem orderItem = new OrderItem();
			orderItem.setItem(item);
			orderItem.setCount(10);
			orderItem.setOrderPrice(1000);
			orderItem.setOrder(order);
			order.getOrderItems().add(orderItem);
		}
		
		orderRepository.saveAndFlush(order);
		em.clear();
		
		Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);
		
		assertEquals(3, savedOrder.getOrderItems().size());
	}
	
	@Test
	public Order createOrder() {
		Order order = new Order();
		
		for(int i = 0; i < 3; i++) {
			Item item = createItem();
			itemRepository.save(item);
			
			OrderItem orderItem = new OrderItem();
			orderItem.setItem(item);
			orderItem.setCount(10);
			orderItem.setOrderPrice(1000);
			orderItem.setOrder(order);
			order.getOrderItems().add(orderItem);
		}
		
		Member member = new Member();
		memberRepository.save(member);
		
		order.setMember(member);
		orderRepository.save(order);
		return order;
	}
	
	@Test
	@DisplayName("고아객체 제거 테스트")
	public void orphanRemovalTest() {
		Order order = createOrder();
		order.getOrderItems().remove(0);
		em.flush();
	}
	
	@Test
	@DisplayName("지연 로딩 테스트")
	public void lazyLoadingTest() {
		Order order = createOrder();
		
		Long orderItemId = order.getOrderItems().get(0).getId();
		
		em.flush();
		em.clear();
		
		OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);
		
		System.out.println("Order class : " + orderItem.getOrder().getClass());
		System.out.println("==================================================");
		orderItem.getOrder().getOrderDate();
		System.out.println("==================================================");
	}

}
