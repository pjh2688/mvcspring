package com.shop.service.item;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.item.ItemFormDto;
import com.shop.dto.item.ItemImgDto;
import com.shop.dto.item.ItemSearchDto;
import com.shop.dto.item.MainItemDto;
import com.shop.entity.item.Item;
import com.shop.entity.item.ItemImg;
import com.shop.entity.item.ItemImgRepository;
import com.shop.entity.item.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

	private final ItemRepository itemRepository;
	private final ItemImgRepository itemImgRepository;
	private final ItemImgService itemImgService;
	
	// 1-1. 상품 등록
	public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
		
		// 1-2. itemFormDto를 item entity로 변환
		Item item = itemFormDto.createItem();
		
		// 1-3. 영속화
		itemRepository.save(item);
		
		// 1-4. 이미지 등록
		for(int i=0; i < itemImgFileList.size(); i++) {
			ItemImg itemImg = new ItemImg();
			itemImg.setItem(item);

			// 1-5. 첫번째 이미지일 경우 대표 상품 이미지 여부 값을 "Y"로 세팅하고 나머지 상품 이미지는 "N"으로 설정
			if(i == 0) {  
				itemImg.setRepimgYn("Y");
			} else {
				itemImg.setRepimgYn("N");
			}
			
			itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
		}
		
		return item.getId();
	}
	
	// 2-1. 상품 단건 조회
	@Transactional(readOnly = true)  // 2-2. 전체 서비스에 트랜잭션을 걸어두고 해당 서비스에 트랜잭션을 또 걸고 readonly를 true로 해주면 JPA가 더티체킹을 수행하지 않아 성능이 향상된다.
	public ItemFormDto getItemDetail(Long itemId) {
		
		// 2-3. 매개변수로 전달된 itemId로 등록된 이미지 리스트 정보 가져오기.
		List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
		
		// 2-4. 2-3에서 엔티티로 가져왔으니 Dto로 변환해해서
		List<ItemImgDto> itemImgDtoList = new ArrayList<>();
		
		// 2.5 itemImgDtoList에 추가.
		for(ItemImg itemImg : itemImgList) {
			ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
			itemImgDtoList.add(itemImgDto);
		}
		
		// 2-6. 
		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
		
		// 2-7.
		ItemFormDto itemFormDto = ItemFormDto.of(item);
		itemFormDto.setItemImgDtoList(itemImgDtoList);
		
		return itemFormDto;
	}
	
	// 3-1. 상품 업데이트
	public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
		
		// 3-2. 상품 수정을 위해 전달 받은 itemFormDto에서 id값을 꺼내 item 엔티티 조회
		Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		
		// 3-3. 상품 수정(변경감지)
		item.updateItem(itemFormDto);
		
		// 3-4. 등록된 이미지들의 id값을 가져온다.
		List<Long> itemImgIds = itemFormDto.getItemImgIds();
		
		// 3-5. 이미지 업데이트(등록)
		for(int i = 0; i < itemImgFileList.size(); i++) {
			itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
		}
		
		return item.getId();
	}
	
	// 4-1. 페이지별 상품 관리 화면 select 
	@Transactional(readOnly = true)
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		return itemRepository.getAdminItemPage(itemSearchDto, pageable);
	}
	
	// 5-1. 
	@Transactional(readOnly = true)
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		return itemRepository.getMainItemPage(itemSearchDto, pageable);
	}
	
	// 6. 전체 아이템 수 구하기
	public int count() {
		return itemRepository.findAll().size();
	}
	
}
