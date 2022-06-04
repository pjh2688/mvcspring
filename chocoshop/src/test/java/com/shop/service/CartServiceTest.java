package com.shop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.shop.constant.item.ItemSellStatus;
import com.shop.dto.cart.CartItemDto;
import com.shop.entity.cart_item.CartItem;
import com.shop.entity.cart_item.CartItemRepository;
import com.shop.entity.item.Item;
import com.shop.entity.item.ItemRepository;
import com.shop.entity.member.Member;
import com.shop.entity.member.MemberRepository;
import com.shop.service.cart.CartService;

@SpringBootTest
@Transactional
class CartServiceTest {
	
	@Autowired
	ItemRepository itemRepository;

	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	CartItemRepository cartItemRepository;
	
	// 1-1. 장바구니에 담을 상품 생성 메소드
	public Item saveItem() {
		Item item = new Item();
		item.setItemNm("윈도우 CD-KEY");
		item.setPrice(10000);
		item.setItemDetail("윈도우10 Professial");
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(10);
		return itemRepository.save(item);
	}
	
	// 1-2. 장바구니를 이용하는 회원 생성 메소드
	public Member saveMember() {
		Member member = new Member();
		member.setEmail("test1234@google.com");
		return memberRepository.save(member);
	}
	
	@Test
	@DisplayName("장바구니 담기 테스트")
	public void addCart() {
		Item item = saveItem();
		Member member = saveMember();
		
		// 1-3. 장바구니에 담을 상품의 itemId와 수량을 CartItemDto에 셋팅
		CartItemDto cartItemDto = new CartItemDto();
		cartItemDto.setItemId(item.getId());
		cartItemDto.setCount(5);
		
		// 1-4. CartItemDto랑, 주문자 이메일로 장바구니를 만든다.
		Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());
		
		// 1-5. 장바구니에 등록된 cartItemId로 해당 장바구니 조회
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
		
		// 1-6. 해당 아이템 id와 장바구니에 저장된 아이템 id를 비교 
		assertEquals(item.getId(), cartItem.getItem().getId());
		
		// 1-7. 장바구니에 담았던 수량ㅇ과 실제로 장바구니에 저장된 상품 수량 비교.
		assertEquals(cartItemDto.getCount(), cartItem.getCount());
		
	}

	// 2022-05-26 -> 내일 339p부터
}
