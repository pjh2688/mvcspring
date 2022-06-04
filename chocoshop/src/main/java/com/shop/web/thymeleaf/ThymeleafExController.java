package com.shop.web.thymeleaf;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shop.dto.item.ItemDto;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafExController {

	@GetMapping("/ex01")
	public String thymeleafExample01(Model model) {
		model.addAttribute("data", "타임리프 예제입니다.");
		return "thymeleaf/thymeleafEx01";
	}
	
	@GetMapping("/ex02")
	public String thymeleafExample02(Model model) {
		
		ItemDto item = new ItemDto();
		item.setItemDetail("상품 상세 설명");
		item.setItemNm("테스트 상품1");
		item.setPrice(10000);
		item.setRegTime(LocalDateTime.now());

		model.addAttribute("item", item);
		
		return "thymeleaf/thymeleafEx02";
	}
	
	@GetMapping("/ex03")
	public String thymeleafExample03(Model model) {
		
		List<ItemDto> itemList = new ArrayList<>();
		
		for(int i = 1; i <= 10; i++) {
			ItemDto itemDto = new ItemDto();
			itemDto.setItemDetail("상품상세설명" + i);
			itemDto.setItemNm("테스트 상품" + i);
			itemDto.setPrice(1000*i);
			itemDto.setRegTime(LocalDateTime.now());
			
			itemList.add(itemDto);
		}
		
		model.addAttribute("itemList", itemList);
		
		return "thymeleaf/thymeleafEx03";
	}
	
	@GetMapping("/ex04")
	public String thymeleafExample04(Model model) {
		
		List<ItemDto> itemList = new ArrayList<>();
		
		for(int i = 1; i <= 10; i++) {
			ItemDto itemDto = new ItemDto();
			itemDto.setItemDetail("상품상세설명" + i);
			itemDto.setItemNm("테스트 상품" + i);
			itemDto.setPrice(1000*i);
			itemDto.setRegTime(LocalDateTime.now());
			
			itemList.add(itemDto);
		}
		
		model.addAttribute("itemList", itemList);
		return "thymeleaf/thymeleafEx04";
	}
	
	@GetMapping("/ex05")
	public String thymeleafExample05(Model model) {
		return "thymeleaf/thymeleafEx05";
	}
	
	@GetMapping("/ex06")
	public String thymeleafExample06(@RequestParam(value = "param1", required = false) String param1, @RequestParam(value = "param2", required = false) String param2, Model model) {
		
		model.addAttribute("param1", param1);
		model.addAttribute("param2", param2);
		
		return "thymeleaf/thymeleafEx06";
	}
	
	// 2022-04-27 -> 타임리프 thymeleaf-layout-dialect 성공
	@GetMapping("/ex07")
	public String thymeleafExample07(Model model) {
		return "thymeleaf/thymeleafEx07";
	}
}
