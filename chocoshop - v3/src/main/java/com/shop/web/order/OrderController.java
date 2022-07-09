package com.shop.web.order;

import java.security.Principal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shop.dto.order.OrderHistDto;
import com.shop.service.order.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	
	// 1-1. 주문 내역 조회
	@GetMapping(value = {"/orders", "/orders/{page}"})
	public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
		// 1-2. 한 번에 가지고 올 주문의 개수를 4개로 설정하고 Pageable 객체 생성 
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
		
		try {
			// 1-3. 현재 로그인한 회원의 이메일과 페이징 객체를 파라미터로 전달하여 화면에 전달할 주문 목록 데이터를 리턴 받는다.
			Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);
			
			// 1-4. 2-3을 orders란 이름으로 Model에 담는다.
			model.addAttribute("orders", orderHistDtoList);
			
			// 1-5. 
			model.addAttribute("page", pageable.getPageNumber());

			// 1-6.
			model.addAttribute("maxPage", 5);
				
		} catch (Exception e) {
			model.addAttribute("errorMessage", "주문 정보 조회 중 에러가 발생하였습니다.");
			return "redirect:/";
		}
		
		return "order/orderHist";
	}
	
}