package com.shop.service.cart;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.shop.dto.cart.CartDetailDto;
import com.shop.dto.cart.CartOrderDto;
import com.shop.dto.cart_item.CartItemDto;
import com.shop.dto.order.OrderDto;
import com.shop.entity.cart.Cart;
import com.shop.entity.cart.CartRepository;
import com.shop.entity.cart_item.CartItem;
import com.shop.entity.cart_item.CartItemRepository;
import com.shop.entity.item.Item;
import com.shop.entity.item.ItemRepository;
import com.shop.entity.member.Member;
import com.shop.entity.member.MemberRepository;
import com.shop.service.order.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {  // 1-1. 장바구니에 상품을 담는 로직 수행하는 서비스

	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final OrderService orderService;
	
	// 1-2. 장바구니엥 상품을 담는 기능을 수행하는 서비스
	public Long addCart(CartItemDto cartItemDto, String email) {
		// 1-3. 전달 받은 cartItemDto에 저장된 itemId로 해당 item 엔티티 조회
		Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
		
		// 1-4. 전달 받은 email 주소로 로그인한 회원 엔티티를 조회
		Member member = memberRepository.findByEmail(email);
		
		// 1-5. 1-4로 찾은 회원의 id로 Cart 엔티티를 가져온다.
		Cart cart = cartRepository.findByMemberId(member.getId());
		
		// 1-6. 처음 장바구니에 담을 때  
		if(cart == null) {
			// 1-7. 로그인한 회원 엔티티를 가지고 있는 장바구니 엔티티를 새로 생성해준다. 
			cart = Cart.createCart(member);
			
			// 1-8. 영속화
			cartRepository.save(cart);
		}
		
		// 1-9. 1-5에서 찾은 장바구니 아이디와 상품 아이디를 가지고 저장된 CartItem 엔티티를 가져온다.
		CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
		
		if(savedCartItem != null) { // 1-10. CartItem이 비어 있지 않다면
			// 1-11. 기존 장바구니에 있던 아이템 수량에다가  현재 장바구니에 새로 담을 아이템에 수량을 더해줍니다.
			savedCartItem.addCount(cartItemDto.getCount());
			
			// 1-12. 그리고 새로 추가한 아이템을 가지고 있는 장바구니에 id를 return
			return savedCartItem.getId();
		} else {
			// 1-13. 처음 장바구니에 담는 경우 1-6, 1-7, 1-8에서 생성한 장바구니를 가지고 CartItem을 만듭니다.
			CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
			
			// 1-14. 영속화
			cartItemRepository.save(cartItem);
			
			// 1-15. 저장된 CartItem id를 return
			return cartItem.getId();
		}
	}
	
	// 2-1. 장바구니 상품 조회 서비스
	@Transactional(readOnly = true)  // 2-2. 성능 향상을 위해 읽기 전용에는 트랜잭션을 걸어준다.
	public List<CartDetailDto> getCartList(String email) {
		// 2-3. 
		List<CartDetailDto> cartDetailDtoList = new ArrayList<>();
		
		// 2-4.
		Member member = memberRepository.findByEmail(email);
		
		// 2-5. 
		Cart cart = cartRepository.findByMemberId(member.getId());
		
		// 2-6. 장바구니가 비어 있다면 2-3에서 생성한 비어있는 장바구니 반환
		if(cart == null) {
			return cartDetailDtoList;
		}
		
		// 2-7. 장바구니가 비어 있지 않다면
		cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
		
		return cartDetailDtoList;
		
	}
	
	// 3-1. 현재 로그인한 사용자인지 검증
	@Transactional(readOnly = true)
	public boolean validateCartItem(Long cartItemId, String email) {
		// 3-2. 로그인한 회원 가져오기
		Member curMember = memberRepository.findByEmail(email);
		
		// 3-3. 3-2가 저장한 장바구니아이템 가져오기
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
		
		// 3-4. 3-3에서 가져온 장바구니 아이템에 등록된 회원 가져오기
		Member savedMember = cartItem.getCart().getMember();
		
		// 3-5. 3-2, 3-4이 동일한지 비교
		if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
			return false;
		}
		
		return true;
	}
	
	// 3-6. 
	public void updateCartItemCount(Long cartItemId, int count) {
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
		cartItem.updateCount(count);
	}
	
	// 4-1. 장바구니 상품 삭제 서비스
	public void deleteCartItem(Long cartItemId) {
		// 4-2. 
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
		
		// 4-3.
		cartItemRepository.delete(cartItem);
	}
	
	// 5-1. 장바구니 상품 주문 서비스
	public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
		// 5-2. 장바구니에 있는 상품 정보를 가지고 주문서를 만들기 위해 list형 객체 생성(상품이 여러개 일 수도 있으므로)
		List<OrderDto> orderDtoList = new ArrayList<>();
		
		for(CartOrderDto cartOrderDto : cartOrderDtoList) {  // 5-3. 장바구니에 담긴 상품들을 하나씩 꺼내온다.
			// 5-4. cartOrderDtoList를 향상된 for문으로 돌려 CartOrderDto에 하나씩 저장된 장바구니 상품 아이디로 장바구니상품 엔티티를 가져온다.
			CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
			
			// 5-5. 주문서를 생성하고 5-4에서 찾아온 장바구니 상품의 아이디와 수량을 세팅해준다. 
			OrderDto orderDto = new OrderDto();
			orderDto.setItemId(cartItem.getItem().getId());
			orderDto.setCount(cartItem.getCount());
			
			// 5-6. 주문서 리스트에 추가
			orderDtoList.add(orderDto);
		}
		
		// 5-7. 장바구니에 담은 상품을 주문하도록 주문 로직을 호출한다.
		Long orderId = orderService.orders(orderDtoList, email);
				
		// 5-8. 주문한 상품을 장바구니에서 제거한다.
		for(CartOrderDto cartOrderDto : cartOrderDtoList) {
			// 5-9. cartOrderDtoList를 향상된 for문으로 돌려 CartOrderDto에 저장된 장바구니상품 아이디로 장바구니상품 엔티티를 가져온다.
			CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
						
			// 5-10. 장바구니에 담겨 있는 상품들을 차례대로 삭제
			cartItemRepository.delete(cartItem);
		}
		
		return orderId;
	}
}
