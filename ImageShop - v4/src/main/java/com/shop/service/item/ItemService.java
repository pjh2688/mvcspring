package com.shop.service.item;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.domain.item.Item;
import com.shop.domain.member.Member;
import com.shop.handler.exception.CustomApiException;
import com.shop.mapper.item.IItemMapper;
import com.shop.mapper.member.IMemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ItemService {
	
	private final IItemMapper itemMapper;
	private final IMemberMapper memberMapper; 
	private final UserItemService userItemService;
	private final FileService fileService;
	
	@Value("${uploadPath}")
	private String uploadPath;
	
	public int itemSave(String itemString, MultipartFile picture_file, MultipartFile preview_file) throws Exception {
		// 1-1. itemString값 찍어보기
		log.info("itemString:" + itemString);
				
		// 1-2. itemString을 ObjectMapper를 사용해 Item 객체로 형 변환하여 저장.
		Item item = new ObjectMapper().readValue(itemString, Item.class);
		
		String itemName = item.getItemName();
		String description = item.getDescription();

		if(itemName != null) {
			log.info("item.getItemName() : " + itemName);
			item.setItemName(itemName);
		}
		
		if(description != null) {
			log.info("item.getDescription() : " + description);
			item.setDescription(description);
		}
					
		// 1-3. 아이템은 Item 객체로 변환을 했지만 파일 들은 따로 셋팅해줘야 한다.
		item.setPicture(picture_file);
		item.setPictureName(picture_file.getOriginalFilename());
		item.setPreview(preview_file);
		item.setPreviewName(preview_file.getOriginalFilename());
					
		// 1-4. item에 셋팅해놓은 파일 불러오기.
		MultipartFile pictureFile = item.getPicture();
		MultipartFile previewFile = item.getPreview();
		
		// 1-5. 파일 업로드 후 저장 파일 명을 반환 받아온다. -> 주의 : 파일 사이즈가 : 4800KB 밑이여야 된다. 아니 더 아래일 수도 있다.
		String createdPictureFilename = fileService.uploadFile(pictureFile.getOriginalFilename(), pictureFile.getBytes());
		String createdPreviewFilename = fileService.uploadFile(previewFile.getOriginalFilename(), previewFile.getBytes());
					
		// 1-6. item 이미지 저장 경로 저장
		item.setPictureUrl(createdPictureFilename);
		item.setPreviewUrl(createdPreviewFilename);
		
		// 1-7. item 저장.
		int result = itemMapper.save(item);
	
		return result;
	}
	
	// 2023-03-07 -> 아이템 수정 성공
	public int itemUpdate(Long itemId, String itemString, MultipartFile picture_file, MultipartFile preview_file) throws Exception {
		
		// 2-1. itemString을 ObjectMapper를 사용해 Item 객체로 형 변환하여 저장.
		Item item = new ObjectMapper().readValue(itemString, Item.class);
		
		// 2-2. 수정할 itemId를 파라미터로 받아와 2-1에 셋팅
		item.setItemId(itemId);
		
		String itemName = item.getItemName();
		String description = item.getDescription();
		
		// 2-3. 넘겨 받은 itemString에 담긴 값을 2-1에 셋팅 1
		if(itemName != null) {
			log.info("item.getItemName() : " + itemName);
			item.setItemName(itemName);
		} 
		
		// 2-4. 넘겨 받은 itemString에 담긴 값을 2-1에 셋팅 2
		if(description != null) {
			log.info("item.getDescription() : " + description);
			item.setDescription(description);
		}
		
		// 2-5. update 폼에서 넘어오는 폼에 파일이 있다면 그 파일도 2-1에 세팅
		if(picture_file != null && picture_file.getSize() > 0) {
			item.setPicture(picture_file);
			item.setPictureName(picture_file.getOriginalFilename());
			
			// 2-6. 만약 null이 아니고 파일이 존재하면 itemString에 같이 담겨온 item에서 이미지 파일정보를 가져와 MultipartFile 형태로 저장
			MultipartFile pictureFile = item.getPicture();
			
			// 2-7. picturefile을 업로드 시키고 저장이름을 반환받아서
			String createdPictureFilename = fileService.uploadFile(pictureFile.getOriginalFilename(), pictureFile.getBytes());
		
			// 2-8. 저장명을 item 객체에 셋팅
			item.setPictureUrl(createdPictureFilename);
		} else { // 2-9. 기존에 파일이 있다면 updateForm에는 picture_file이 없는 상태로 넘어올 것이므로 기존에 저장되어 있는 파일로 업데이트한다.
			// 2-10. 업데이트시 전달 받은 itemId로 해당 item정보 를 optional로 일단 가지고 온다.
			Optional<Item> itemOp = itemMapper.findByItemId(itemId);
			
			// 2-11. item이 존재하면
			if(itemOp.isPresent()) {
				// 2-12. 기존에 등록되어 있는 itemOp 객체를 get
				Item oldItem = itemOp.get();
				
				// 2-13. db에 저장되어 있는 그림파일 정보를 셋팅한다.
				item.setPicture(oldItem.getPicture());
				item.setPictureName(oldItem.getPictureName());
				item.setPictureUrl(oldItem.getPictureUrl());
			}
		}
		
		// 2-14. preview_file도 picture_file과 동일하다.
		if(preview_file != null && preview_file.getSize() > 0) {
			item.setPreview(preview_file);
			item.setPreviewName(preview_file.getOriginalFilename());
			
			MultipartFile previewFile = item.getPreview();
			String createdPreviewFilename = fileService.uploadFile(previewFile.getOriginalFilename(), previewFile.getBytes());
			
			item.setPreviewUrl(createdPreviewFilename);	
		} else {
			Optional<Item> itemOp = itemMapper.findByItemId(itemId);
			
			if(itemOp.isPresent()) {
				Item oldItem = itemOp.get();
				
				item.setPreview(oldItem.getPreview());
				item.setPreviewName(oldItem.getPreviewName());
				item.setPreviewUrl(oldItem.getPreviewUrl());
			}
		}	
		
		// 2-15. 2-5~2-13 작업과 2-14작업이 끝났다면 셋팅된 item을 업데이트한다.
		int result = itemMapper.update(item);
		
		return result;
	}
	
	// 2023-03-08 -> 여기까지
	public void deleteItem(Long itemId) throws Exception { 
		// 3-1. 전달 받은 itemId로 삭제할 item 검색
		Optional<Item> itemOp = itemMapper.findByItemId(itemId);
		
		if(itemOp.isPresent()) {  // 3-2. item이 DB에 존재하면
			// 3-3. item을 get 해서 셋팅
			Item findItem = itemOp.get();
			
			// 3-4. 찾아온 item에 첨부파일이 있다면 해당 경로에 있는 파일을 삭제하고
			fileService.deleteFile(findItem.getPictureUrl(), findItem.getPreviewUrl());
			
			// 3-5. db에서 해당 id의 item을 삭제.
			itemMapper.delete(findItem.getItemId());
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<Item> findAll() {
		
		List<Item> result = null;
		
		try {
			result = itemMapper.findAll();
			
		} catch (Exception e) {
			throw new CustomApiException("아이템 리스트 정보가 존재하지 않습니다.");
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public Item findByItemId(Long itemId) {
		
		Optional<Item> itemOp = itemMapper.findByItemId(itemId);
		
		if(itemOp.isPresent()) {
			return itemOp.get();
			
		} else {
			throw new CustomApiException("해당 아이템이 존재하지 않습니다.");
		}
		
	}
	
	public int itemBuy(Member member, Long itemId) {
		
		Optional<Member> memberOp = memberMapper.findByUserNo(member.getUserNo());
		
		if(memberOp.isPresent()) {
			Member buyMember = memberOp.get();
			
			Integer coin = memberMapper.getCoin(member.getUserNo());
			
			System.out.println("가지고 있는 코인 : " + coin);
			
			if(coin <= 0) {
				throw new CustomApiException("코인이 없습니다. 코인을 충전해주세요.");
			}
			
			Optional<Item> itemOp = itemMapper.findByItemId(itemId);
			
			if(itemOp.isPresent()) {
				Item buyItem = itemOp.get();
				
				if(coin < buyItem.getPrice()) {
					throw new CustomApiException("가지고 있는 코인이 부족합니다. 코인을 충전해주세요.");
				}
				
				buyMember.setCoin(memberMapper.getCoin(member.getUserNo()));
				
				int result = userItemService.register(buyMember, buyItem);
				
				return result;
				
			} else {
				throw new CustomApiException("해당 상품이 존재하지 않습니다.");
			}
			
		} else {
			throw new CustomApiException("해당 유저가 존재하지 않습니다.");
		}
		
	}

}
