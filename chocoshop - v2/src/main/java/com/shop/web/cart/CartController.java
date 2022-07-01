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
		List<CartDetailDto> cartDetailDtoList = cartService.getCartList(principal.getName());
		
		model.addAttribute("cartItems", cartDetailDtoList);
		
		return "cart/cartList";
	}
	
}
