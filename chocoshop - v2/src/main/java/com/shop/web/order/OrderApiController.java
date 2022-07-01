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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.dto.order.OrderDto;
import com.shop.service.order.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderApiController {

	private final OrderService orderService;
	
	// 1-1. 주문 API
	@PostMapping(value = "")
	public ResponseEntity<Map<String, Object>> order(@RequestBody final OrderDto orderDto, BindingResult bindingResult, Principal principal) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 1-2. 주문 정보를 받는 orderDto 객체에 데이터 바인딩 시 에러가 있는지 검사.
		if(bindingResult.hasErrors()) {
			// 1-3. 에러가 있을 경우 Map에 담아서 에러 코드와 함께 return
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			
			for(FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage());
			}
			
			result.put("message", sb.toString());
			result.put("code", HttpStatus.BAD_REQUEST);
			
			return ResponseEntity.ok(result);
		}
		
		// 1-4. principal 객체로 현재 로그인한 회원 아이디를 가져온다.
		String email = principal.getName();
		Long orderId;
		
		try {
			// 1-5. 화면으로부터 넘어온 주문 정보와 회원의 이메일 정보로 주문 로직 호출 성공하면 주문 id와 OK HttpStatus 코드를 result에 담는다.
			orderId = orderService.order(orderDto, email);
			
			result.put("orderId", orderId);
			result.put("code", HttpStatus.OK);
			
		} catch (Exception e) {
			// 1-6. 1-5에서 실패했다면 BAD_REQUEST 상태코드를 result에 담는다.
			result.put("message", e.getMessage());
			result.put("code", HttpStatus.BAD_REQUEST);
		}
		
		return ResponseEntity.ok(result);
	}
	
	// 2-1. 주문 취소 API
	@PostMapping(value = "/{orderId}/cancel")
	public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 2-2. 주문자와 로그인한 사람을 비교해서 로그인한 사람이 주문자가 아니면
		if(!orderService.validateOrder(orderId, principal.getName())) {
			result.put("message", "주문 취소 권한이 없습니다.");
			result.put("code", HttpStatus.FORBIDDEN);
			
			return ResponseEntity.ok(result);
		}
		
		// 2-3. 주문 취소 로직 호출
		try {
			orderService.cancelOrder(orderId);
			
			// 2-4.
			result.put("ordedrId", orderId);
			result.put("code", HttpStatus.OK);
		} catch (Exception e) {
			result.put("message", e.getMessage());
			result.put("code", HttpStatus.BAD_REQUEST);
		}
		
		
		return ResponseEntity.ok(result);
	}
}