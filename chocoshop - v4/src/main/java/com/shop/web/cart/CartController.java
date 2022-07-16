package com.shop.web.cart;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shop.dto.cart.CartDetailDto;
import com.shop.service.cart.CartService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;
	
	@GetMapping(value = "/cart")
	public String orderHist(Principal principal, Model model) {
		
		try {
			List<CartDetailDto> cartDetailDtoList = cartService.getCartList(principal.getName());
			
			model.addAttribute("cartItems", cartDetailDtoList);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "장바구니 상품 조회 중 에러가 발생하였습니다.");
			return "redirect:/";
		}
		
		return "cart/cartList";
	}
	
}
