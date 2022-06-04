package com.shop.web.cart;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shop.dto.cart.CartItemDetailDto;
import com.shop.service.cart.CartService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;
	
	// 2022-05-27 -> 345p까지 
	@GetMapping("/cart")
	public String cartHist(Principal principal, Model model) {
		
		List<CartItemDetailDto> cartDetailList = cartService.getCartList(principal.getName());
		
		model.addAttribute("cartItems", cartDetailList);
		
		return "cart/cartList";
	}
}
