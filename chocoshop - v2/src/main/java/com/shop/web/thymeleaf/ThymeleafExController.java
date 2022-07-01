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
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {

	@GetMapping(value = "/ex01")
	public String thymeleafExample01(Model model) {
		model.addAttribute("data", "타임리프 예제입니다.");
		return "thymeleaf/thymeleafEx01";
	}
	
	@GetMapping(value = "/ex02")
	public String thymeleafExample02(Model model) {
		
		ItemDto itemDto = new ItemDto();
		itemDto.setItemDetail("상품 상세 내용");
		itemDto.setItemNm("테스트 상품1");
		itemDto.setPrice(10000);
		itemDto.setRegTime(LocalDateTime.now());
		
		model.addAttribute("itemDto", itemDto);
		
		return "thymeleaf/thymeleafEx02";
	}
	
	@GetMapping(value = "/ex03")
	public String thymeleafExample03(Model model) {
		
		List<ItemDto> itemDtoList = new ArrayList<>();
		
		for(int i = 1; i <= 10; i++) {
			
			ItemDto itemDto = new ItemDto();
			itemDto.setItemDetail("상품 상세 설명 " + i);
			itemDto.setItemNm("테스트 상품 " + i);
			itemDto.setPrice(1000 * i);
			itemDto.setRegTime(LocalDateTime.now());
			
			itemDtoList.add(itemDto);
		}
		
		model.addAttribute("itemDtoList", itemDtoList);
		
		return "thymeleaf/thymeleafEx03";
	}
	
	@GetMapping(value = "/ex04")
	public String thymeleafExample04(Model model) {
		
		List<ItemDto> itemDtoList = new ArrayList<>();
		
		for(int i = 1; i <= 10; i++) {
			
			ItemDto itemDto = new ItemDto();
			itemDto.setItemDetail("상품 상세 설명 " + i);
			itemDto.setItemNm("테스트 상품 " + i);
			itemDto.setPrice(1000 * i);
			itemDto.setRegTime(LocalDateTime.now());
			
			itemDtoList.add(itemDto);
		}
		
		model.addAttribute("itemDtoList", itemDtoList);
		
		return "thymeleaf/thymeleafEx04";
	}
	
	@GetMapping(value = "/ex05")
	public String thymeleafExample05() {
		return "thymeleaf/thymeleafEx05";
	}
	
	@GetMapping(value = "/ex06")
	public String thymeleafExample06(@RequestParam("param1") String param1, @RequestParam("param2") String param2, Model model) {
		model.addAttribute("param1", param1);
		model.addAttribute("param2", param2);
		return "thymeleaf/thymeleafEx06";
	}
	
	@GetMapping(value = "/ex07")
	public String thymeleafExample07() {
		return "thymeleaf/thymeleafEx07";
	}
}
