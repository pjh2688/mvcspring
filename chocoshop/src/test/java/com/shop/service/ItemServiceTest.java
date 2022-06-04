package com.shop.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shop.constant.item.ItemSellStatus;
import com.shop.dto.item.ItemFormDto;
import com.shop.entity.item.Item;
import com.shop.entity.item.ItemImg;
import com.shop.entity.item.ItemImgRepository;
import com.shop.entity.item.ItemRepository;
import com.shop.service.item.ItemService;

@SpringBootTest
@Transactional
class ItemServiceTest {

	@Autowired
	ItemService itemService;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	ItemImgRepository itemImgRepository;
	
	// 1-1. MockMultipartFile 클래스를 이용하여 가짜 MultipartFile 리스트를 만들어 반환해주는 메소드 생성
	List<MultipartFile> createMultipartFiles() throws Exception {
		
		List<MultipartFile> multipartFileList = new ArrayList<>();
		
		for(int i = 0; i < 5; i++) {
			String path = "C:/HANSSAK/SecureGate/Download/workspace(mvcspring)/upload/shop/item/";
			String imageName = "image" + i + ".jpg";
			MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
			multipartFileList.add(multipartFile);
		}
		
		return multipartFileList;
	}
	
	@Test
	@DisplayName("상품 등록 테스트")
	@WithMockUser(username = "admin", roles = "ADMIN")
	void saveItem() throws Exception {
		// 1-2. 상품 등록 화면에서 넘어오는 formData를 세팅
		ItemFormDto itemFormDto = new ItemFormDto();
		itemFormDto.setItemNm("테스트 상품");
		itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
		itemFormDto.setItemDetail("테스트 상품입니다.");
		itemFormDto.setPrice(1000);
		itemFormDto.setStockNumber(100);
		
		// 1-3. 
		List<MultipartFile> multipartFileList = createMultipartFiles();
		
		// 1-4. 상품 데이터와 이미지 경로를 파라미터로 넘겨서 저장한 후 상품의 아이디 값을 반환해준다.
		Long itemId = itemService.saveItem(itemFormDto, multipartFileList);
		
		// 1-5.
		List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
		
		// 1-6. 입력한 상품 데이터와 실제로 저장된 상품 데이터가 같은지 검증
		assertEquals(itemFormDto.getItemNm(), item.getItemNm());  
		
		// 1-7. 
		assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
		
		// 1-8. 
		assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
		
		// 1-9. 
		assertEquals(itemFormDto.getPrice(), item.getPrice());
		
		// 1-10. 
		assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
		
		// 1-11.
		assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());
		
		
	}

}
