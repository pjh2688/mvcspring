package com.shop.web;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shop.dto.item.ItemSearchDto;
import com.shop.dto.item.MainItemDto;
import com.shop.service.item.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

	private final ItemService itemService;
	
	@GetMapping(value = "/")
	public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
		
		// 1-2. 페이징을 위해 PageRequest.of 메소드를 통해 Pageable 객체를 생성합니다. URL 경로에 페이지 번호가 있으면 해당 페이지를 조회하도록 세팅하고, 페이지 번호가 없으면 0페이지로 조회하도록 합니다.
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		
		try {
			// 1-3. 조회 조건(itemSearchDto)과 페이징 정보(pageable)를 파라미터로 넘겨서 Page<Item> 객체를 반환 받는다.
			Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
					
			// 1-4. 조회한 상품 데이터 및 페이징 정보를 가지고 있는 items 객체를 model에 담는다.
			model.addAttribute("items", items);
					
			// 1-5. 페이지 전환 시 기존 검색 조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달한다.
			model.addAttribute("itemSearchDto", itemSearchDto);
					
			// 1-6. 상품 관리 메뉴 하단에 보여줄 페이지 번호의 최대 갯수입니다. 5로 설정했으므로 최대 5개의 이동할 페이지 번호만 보여줍니다.
			model.addAttribute("maxPage", 5);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "메인 화면 조회 중 에러가 발생하였습니다.");
		}
			
		return "main";
	}
}
