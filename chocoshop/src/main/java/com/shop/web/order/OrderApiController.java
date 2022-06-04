package com.shop.web.order;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shop.dto.order.OrderDto;
import com.shop.service.order.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

	private final OrderService orderService;
	
	// 1-1. 주문 API
	@PostMapping(value = "/order")
	public ResponseEntity<Map<String, Object>> order(@RequestBody final OrderDto orderDto, BindingResult bindingResult, Principal principal) {
		
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
		
		String email = principal.getName();
		
		Long orderId;
		
		try {
			orderId = orderService.order(orderDto, email);
			
			result.put("orderId", orderId);
			result.put("code", HttpStatus.OK);
			
		} catch (Exception e) {
			result.put("message", e.getMessage());
			result.put("code", HttpStatus.BAD_REQUEST);
		}
		
		return ResponseEntity.ok(result);
	}
	
	// 2-1. 주문 취소 API
	@PostMapping("/order/{orderId}/cancel")
	public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 2-2. 주문자와 로그인한 사람을 비교해서 로그인한 사람이 주문자가 아니면
		if(!orderService.validateOrder(orderId, principal.getName())) {
			result.put("message", "주문 취소 권한이 없습니다.");
			result.put("code", HttpStatus.FORBIDDEN);
			
			return ResponseEntity.ok(result);
		}
				
		// 2-3. 주문 취소 로직 호출
		orderService.cacelOrder(orderId);
				
		// 2-4.
		result.put("ordedrId", orderId);
		result.put("code", HttpStatus.OK);
		
		return ResponseEntity.ok(result);
	}
}
