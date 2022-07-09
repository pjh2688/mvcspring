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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.dto.cart.CartOrderDto;
import com.shop.dto.cart_item.CartItemDto;
import com.shop.service.cart.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartApiController {

	private final CartService cartService;
	
	// 1-1. 장바구니에 담는 기능
	@PostMapping(value = "")
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
			
			return ResponseEntity.ok(result);		
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
	
	// 2. 장바구니 상품 수정
	@PatchMapping(value = "/{cartItemId}/updateCartItem")
	public ResponseEntity<Map<String, Object>> updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal) {
		Map<String, Object> result = new HashMap<String, Object>();

		if(count <= 0) {
			result.put("code", HttpStatus.BAD_REQUEST);
			result.put("message", "최소 1개 이상 담아주세요.");
			
			return ResponseEntity.ok(result);
		} else if(!cartService.validateCartItem(cartItemId, principal.getName())) {
			result.put("code", HttpStatus.FORBIDDEN);
			result.put("message", "수정 권한이 없습니다.");
			
			return ResponseEntity.ok(result);
		}
		
		try {
			cartService.updateCartItemCount(cartItemId, count);
			
			result.put("cartItemId", cartItemId);
			result.put("code", HttpStatus.OK);
			
		} catch (Exception e) {
			result.put("message", e.getMessage());
			result.put("code", HttpStatus.BAD_REQUEST);
		}
		
		return ResponseEntity.ok(result);	
	}
	
	// 3. 장바구니 상품 삭제
	@DeleteMapping(value = "/{cartItemId}/deleteCartItem")
	public ResponseEntity<Map<String, Object>> deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal) {
		Map<String, Object> result = new HashMap<String, Object>();

		if(!cartService.validateCartItem(cartItemId, principal.getName())) {
			result.put("code", HttpStatus.FORBIDDEN);
			result.put("message", "수정 권한이 없습니다.");
			
			return ResponseEntity.ok(result);
		}
		
		try {
			
			cartService.deleteCartItem(cartItemId);
			
			result.put("cartItemId", cartItemId);
			result.put("code", HttpStatus.OK);
			
		} catch (Exception e) {
			result.put("message", e.getMessage());
			result.put("code", HttpStatus.BAD_REQUEST);
		}
		
		return ResponseEntity.ok(result);	
	}
	
	// 4-1. 장바구니 상품 주문
	@PostMapping(value = "/orders")
	public ResponseEntity<Map<String, Object>> orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 4-2. 장바구니에 있는 상품들을 담을 list 
		List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
		
		// 4-3. 주문할 상품을 선택했는지 여부 확인 
		if(cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
			result.put("message", "주문할 상품을 선택해주세요.");
			result.put("code", HttpStatus.FORBIDDEN);
			
			return ResponseEntity.ok(result);
		}
		
		// 4-4. 주문 권한 체크
		for(CartOrderDto cartOrder : cartOrderDtoList) {
			// 4-5.
			if(!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())) {
				result.put("message", "주문 권한이 없습니다.");
				result.put("code", HttpStatus.FORBIDDEN);
				
				return ResponseEntity.ok(result);
			}
		}
		
		// 4-6. 장바구니 상품 주문
		try {
			Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
			
			result.put("orderId", orderId);
			result.put("code", HttpStatus.OK);
		} catch (Exception e) {
			result.put("message", e.getMessage());
			result.put("code", HttpStatus.BAD_REQUEST);
		}
				
		
		return ResponseEntity.ok(result);	
	}
}
