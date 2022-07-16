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
import com.shop.dto.item.ItemSearchDto;
import com.shop.dto.item.MainItemDto;
import com.shop.dto.item_img.ItemImgDto;
import com.shop.entity.item.Item;
import com.shop.entity.item.ItemRepository;
import com.shop.entity.item_img.ItemImg;
import com.shop.entity.item_img.ItemImgRepository;
import com.shop.service.item_img.ItemImgService;

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
		
		// 1-3. item DB에 저장 후 영속화
		itemRepository.save(item);
		
		// 1-4. itemImg에 item 세팅
		for(int i = 0; i < itemImgFileList.size(); i++) {
			ItemImg itemImg = new ItemImg();
			
			// 1-5. 1-3에서 영속화한 item과 itemImg 연결
			itemImg.setItem(item);
			
			// 1-6. 첫번째 이미지일 경우 대표 상품 이미지 여부 값을 "Y"로 세팅하고 나머지 상품 이미지는 "N"으로 설정
			if(i == 0) {
				itemImg.setRepImgYn("Y");
			} else {
				itemImg.setRepImgYn("N");
			}
			
			// 1-7. 이미지도 DB에 저장 후 영속화
			itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
		}
		
		return item.getId();
	}
	
	// 2-1. 상품 상세 보기
	@Transactional(readOnly = true) // 2-2. 상품 데이터를 읽어오는 트랜잭션에 읽기 전용을 설정합니다. 이럴 경우 JPA가 더티체킹(변경감지)를 수행하지 않기 때문에 성능을 향상 시킬 수 있습니다.
	public ItemFormDto getItemDtl(Long itemId) {
		// 2-3. 해당 상품의 이미지 엔티티를 조회합니다. 등록순으로 가지고 오기 위해서 상품 이미지 아이디를 오름차순으로 가져옵니다.
		List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
		
		// 2-4. 2-3에서 조회한 ItemImg 엔티티 List를 itemImgDto List 객체로 변환.
		List<ItemImgDto> itemImgDtoList = new ArrayList<>();
		
		for(ItemImg itemImg : itemImgList) {
			// 2-5. modelmapper를 이용하여 만든 메소드로 itemImg 엔티티를 itemImgDto 객체로 변환합니다.
			ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
			
			// 2-6. 2-5에서 변환한 itemImgDto 객체를 2-4에서 만든 itemImgDtoList에다가 추가 (화면에 보내기 위해 dto로 만드는 것)
			itemImgDtoList.add(itemImgDto);
		}
		
		// 2-7. 상품의 아이디로 상품 엔티티를 조회
		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
		
		// 2-8. 2-7에서 조회한 item 엔티티를 뷰로 가지고 가기 위해 modelmapper를 이용하여 만든 메소드로 item 엔티티를 itemFormDto로 변환
		ItemFormDto itemFormDto = ItemFormDto.of(item);
		
		// 2-9. 변환된 itemFormDto에 ItemImgDtoList도 추가
		itemFormDto.setItemImgDtoList(itemImgDtoList);
		
		return itemFormDto;
	}
	
	// 3-1. 상품 수정
	public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
		// 3-2. 상품 수정을 위해 전달 받은 itemFormDto에서 id값을 꺼내 item 엔티티 조회
		Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		
		// 3-3. 상품 수정(변경감지)
		item.updateItem(itemFormDto);
		
		// 3-4. 상품 이미지 수정을 위해 등록된 이미지들의 id값을 가져온다.
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
	
	// 6. 전체 아이템 수 구하기
	public int count() {
		return itemRepository.findAll().size();
	}
	
	// 7-1.
	@Transactional(readOnly = true)
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		return itemRepository.getMainItemPage(itemSearchDto, pageable);
	}
	
		
}
