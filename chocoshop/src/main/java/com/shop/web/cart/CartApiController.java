package com.shop.web.cart;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shop.dto.cart.CartItemDto;
import com.shop.dto.cart.CartOrderDto;
import com.shop.service.cart.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CartApiController {

	private final CartService cartService;

	// 1-1. 장바구니에 담는 기능
	@PostMapping(value = "/cart")
	public ResponseEntity<Map<String, Object>> cart(@RequestBody final @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			for(FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage());
			}
			
			result.put("message", sb.toString());
			result.put("code", HttpStatus.BAD_REQUEST);
		}
		
		// 1-2. 로그인한 회원의 email 가져온다.
		String email = principal.getName();
		Long cartItemId;
		
		try {
			// 1-3. 화면에서 넘어온 장부구니에 담아 있는 상품 정보와 현재 로그인한 회원 email주소를 가지고 장바구니에 상품을 담는다.
			cartItemId = cartService.addCart(cartItemDto, email);
			
			result.put("cartItemId", cartItemId);
			result.put("code", HttpStatus.OK);
			
		} catch (Exception e) {
			result.put("message", e.getMessage());
			result.put("code", HttpStatus.BAD_REQUEST);
		}
		
		return ResponseEntity.ok(result);
	}
	
	// 2-1. 장바구니 수정 API - HTTP 메소드에서 PATCH는 요청된 자원의 일부를 업데이트할때 사용된다.
	@PatchMapping(value = "/cartItem/{cartItemId}")
	public ResponseEntity<Map<String, Object>> cartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 2-2. 장바구니에서 0이하로 업데이트 요청을 하면 
		if(count <= 0) {
			result.put("message", "최소 1개 이상 담아주세요.");
			result.put("code", HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.ok(result);
		} else if(!cartService.validateCartItem(cartItemId, principal.getName())) {  // 2-3. 장바구니에 담은 사용자와 로그인한 사용자가 다르면
			result.put("message", "수정 권한이 없습니다.");
			result.put("code", HttpStatus.FORBIDDEN);
			
			return ResponseEntity.ok(result);
		} 

		cartService.updateCartItemCount(cartItemId, count);  // 2-4. 2-2, 2-3이 모두 아니면 장바구니 수량 업데이트
			
		result.put("cartItemId", cartItemId);
		result.put("code", HttpStatus.OK);
		
		return ResponseEntity.ok(result);
	}
	
	// 3-1. 장바구니 상품 삭제 API
	@DeleteMapping(value = "/cartItem/{cartItemId}")
	public ResponseEntity<Map<String, Object>> deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(!cartService.validateCartItem(cartItemId, principal.getName())) {  // 3-2. 장바구니에 담은 사용자와 로그인한 사용자가 다르면
			result.put("message", "삭제 권한이 없습니다.");
			result.put("code", HttpStatus.FORBIDDEN);
			
			return ResponseEntity.ok(result);
		}
		
		cartService.deleteCartItem(cartItemId);
		
		result.put("cartItemId", cartItemId);
		result.put("code", HttpStatus.OK);
		
		return ResponseEntity.ok(result);
	}
	
	// 4-1. 장바구니 상품 주문 API
	@PostMapping(value = "/cart/orders")
	public ResponseEntity<Map<String, Object>> orderCartItem(@RequestBody final CartOrderDto cartOrderDto, Principal principal) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 4-2.
		List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
		
		// 4-3. 주문할 상품을 선택했는지 여부 확인 
		if(cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
			result.put("message", "주문할 상품을 선택해주세요.");
			result.put("code", HttpStatus.FORBIDDEN);
			
			return ResponseEntity.ok(result);
		}
		
		// 4-4.
		for(CartOrderDto cartOrder : cartOrderDtoList) {
			// 4-5.
			if(!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())) {
				result.put("message", "주문 권한이 없습니다.");
				result.put("code", HttpStatus.FORBIDDEN);
				
				return ResponseEntity.ok(result);
			}
		}
		
		// 4-6. 
		Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
		
		result.put("orderId", orderId);
		result.put("code", HttpStatus.OK);
		
		return ResponseEntity.ok(result);
		
	}
}