package com.shop.web.item;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shop.constant.item.ItemSellStatus;
import com.shop.dto.item.ItemFormDto;
import com.shop.dto.item.ItemSearchDto;
import com.shop.dto.item.MainItemDto;
import com.shop.entity.item.Item;
import com.shop.service.item.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;
	
	@GetMapping(value = "/")
	public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
		
		model.addAttribute("items", items);
		model.addAttribute("itemSearchDto", itemSearchDto);
		model.addAttribute("maxPage", 5);
		
		return "main";
	}
	
	@GetMapping(value = "/admin/item/new")
	public String itemForm(Model model) {
		model.addAttribute("itemFormDto", new ItemFormDto());
		return "/item/itemForm";
	}
	
	@PostMapping(value = "/admin/item/new")
	public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
		
		// 1-1. 상품 등록 시 필수 값이 없다면 다시 상품 등록페이지로 리다이렉트
		if(bindingResult.hasErrors()) {
			return "item/itemForm";
		}
		
		// 1-2. 상품 등록 시 첫 번째 이미지가 없다면 에러 메시지와 함께 상품 등록 페이지로 리다이렉트(상품의 첫 번째 이미지는 메인 페이지에서 보여줄 상품 이미지로 사용하기 위해 필수 값으로 지정)
		if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
			model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
			return "item/itemForm";
		}
		
		// 1-3. 상품 등록 
		try {
			itemService.saveItem(itemFormDto, itemImgFileList);
		} catch (Exception e) {
			// 1-4. 상품 등록 실패시 errorMessage를 model에 담아서 itemForm으로 이동
			model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
			return "item/itemForm";
		}
		
		return "redirect:/";
	}
	
	@GetMapping(value = "/admin/item/{itemId}")
	public String adminItemDtl(@PathVariable("itemId") Long itemId, Model model) {
		
		try {
			// 2-1. url 파라미터로 전달된 itemId로 조회해서 저장
			ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
			
			// 2-2. 2-1에서 조회한 itemFormDto를 model에 담는다. 
			model.addAttribute("itemFormDto", itemFormDto);
			model.addAttribute("itemId", itemId);
		} catch (EntityNotFoundException e) {
			// 2-3. 에러메시지를 담아서 빈 itemFormDto를 가지고 itemForm 페이지로 이동
			model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
			model.addAttribute("itemFormDto", new ItemFormDto());
			
			return "item/itemForm";
		}
		
		// 2-4. 
		return "item/itemForm";
	}
	
	@PostMapping(value = "/admin/item/{itemId}")
	public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
		
		// 3-1. 상품 수정 시 필수 값이 없다면 다시 상품 수정페이지로 리다이렉트
		if(bindingResult.hasErrors()) {
			return "item/itemForm";
		}
				
		// 3-2. 상품 수정 시 첫 번째 이미지가 없다면 에러 메시지와 함께 상품 등록 페이지로 리다이렉트(상품의 첫 번째 이미지는 메인 페이지에서 보여줄 상품 이미지로 사용하기 위해 필수 값으로 지정)
		if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
			model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
			return "item/itemForm";
		}
				
		// 1-3. 상품 수정
		try {
			itemService.updateItem(itemFormDto, itemImgFileList);
		} catch (Exception e) {
			// 1-4. 상품 등록 실패시 errorMessage를 model에 담아서 itemForm으로 이동
			model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
			return "item/itemForm";
		}
				
		return "redirect:/";
	}
	
	@GetMapping(value = {"/admin/items", "/admin/items/{page}"})  // 4-1. 상품 관리 화면 진입시 URL에 페이지 번호가 없는 경우와 페이지 번호가 있는 경우 2가지를 모두 매핑시킵니다.
	public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
		// 4-2. 페이징을 위해 PageRequest.of 메소드를 통해 Pagealbe 객체를 생성합니다. URL 경로에 페이지 번호가 있으면 해당 페이지를 조회하도록 세팅하고, 페이지 번호가 없으면 0페이지로 조회하도록 합니다.
		Pageable pageable = PageRequest.of(page.isPresent() ? (page.get() - 1) : 0, 5);
		
		// 4-3. 조회 조건(itemSearchDto)와 페이징 정보(pageable)을 파라미터로 넘겨서 Page<Item> 객체를 반환 받는다.
		Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
		
		// 4-4. 조회한 상품 데이터 및 페이징 정보를 가지고 있는 items 객체를 model에 담는다.
		model.addAttribute("items", items);
		
		// 4-5. 페이지 전환 시 기존 검색 조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달한다.
		model.addAttribute("itemSearchDto", itemSearchDto);
		
		// 4-6. 상품 관리 메늎 하단에 보여줄 페이지 번호의 최대 갯수입니다. 5로 설정했으므로 최대 5개의 이동할 페이지 번호만 보여줍니다.
		model.addAttribute("maxPage", 5);
		
		// 4-7. ENUM 타입 전송
		model.addAttribute("itemSellStatus", ItemSellStatus.values());
		
		// 4-8. 
		model.addAttribute("page", pageable.getPageNumber());
		
		// 4-9.
		model.addAttribute("pageCnt", Math.round(itemService.count() / 5) + 1);
		
		// 4-10. 현제 페이지 가져오는데 존재하지 않으면 1로 가져온다. -> 2022-05-25 페이징 해결 완료.
		model.addAttribute("curPage", (page.isPresent() ? page.get() : 1));
		
		return "item/itemMng";
	}
	
	@GetMapping(value = "/item/{itemId}") // 5-1. 상품 상세 페이지(사용자)
	public String userItemDtl(@PathVariable("itemId") Long itemId, Model model) {
		// 5-2. url 파라미터로 전달된 itemId로 조회해서 저장
		ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
		
		// 5-3. 5-2로 조회한 itemFormDto객체를 item이란 이름으로 model에 담는다.
		model.addAttribute("item", itemFormDto);
		return "item/itemDtl";
	}
	
}
