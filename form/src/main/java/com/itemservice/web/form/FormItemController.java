package com.itemservice.web.form;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itemservice.domain.item.Item;
import com.itemservice.repository.item.ItemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// *참고 1 : restcontroller가 아니면 @GetMapping이랑 @PostMapping만 사용할 것.
// *참고 2 : HTML Form 전송은 PUT, PATCH를 지원하지 않는다. GET, POST만 사용 가능.
@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

	private final ItemRepository itemRepository;
	
	// 4. @ModelAttribute의 특별한 사용법 : 이렇게 하면 해당 컨트롤러 안에서는 모든 요청에 해당 model이 담겨서 넘어간다.
	@ModelAttribute("regions")
	public Map<String, String> regions() {
		Map<String, String> regions = new LinkedHashMap<>();
		regions.put("SEOUL", "서울");
		regions.put("BUSAN", "부산");
		regions.put("CHEONGJU", "청주");
		
		return regions;
	}
	
	@GetMapping("")
	public String items(HttpServletRequest request, Model model) {
		List<Item> items = itemRepository.findAll();
		
		model.addAttribute("item", new Item());
		model.addAttribute("items", items);
	
		return "form/items";
	}
	
	@GetMapping("/{itemId}")
	public String item(@PathVariable Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		
		model.addAttribute("item", item);
		
		return "form/item";
	}
	
	@GetMapping("/add")
	public String addForm(Model model) {
		// 3. 빈 껍데기 item 객체를 폼으로 넘겨준다.(validation에서 유용)
		model.addAttribute("item", new Item());
		
		return "form/addForm";
	}
	
	// 1. @ModelAttribute를 사용하고 이름을 지정하면 model에 담긴 객체의 이름을 지정해주는 것. 지정안하면 클래스명에서 첫글자만 소문자로 바꾼 값이 넘어간다.
	@PostMapping("/add")
	public String addItem(@ModelAttribute("item") Item addItem, RedirectAttributes redirectAttributes) {  // 3. RedirectAttributes -> 리다이렉트시 attribute 실을 수 있다.
		
		log.info("item.open={}", addItem.getOpen());
		log.info("item.regions={}", addItem.getRegions());
	
		Item savedItem = itemRepository.save(addItem);
	    redirectAttributes.addAttribute("itemId", savedItem.getId());
	    redirectAttributes.addAttribute("status", true);
	     
		return "redirect:/form/items";
	}
	
	@GetMapping("/{itemId}/edit")
	public String editForm(@PathVariable Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		
		model.addAttribute("item", item);
		
		return "form/editForm";
	}
	
	// 2. PRG 방식 -> Post Redirect Get
	@PostMapping("/{itemId}/edit")
	public String editItem(@PathVariable Long itemId, @ModelAttribute("item") Item editItem) {
		
		itemRepository.update(itemId, editItem);
		
		return "redirect:/form/items/{itemId}";
	}
	
	@PostMapping("/{itemId}/delete")
	public String deleteItem(@PathVariable Long itemId) {
		
		itemRepository.delete(itemId);
		
		return "redirect:/form/items";
	}
}
