package com.shop.service.item;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.shop.entity.item.ItemImg;
import com.shop.entity.item.ItemImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

	// 1-1. @Value 어노테이션(org.springframework.beans.factory.annotation.Value) -> application.properties에 설정한 "itemImgLocation" 프로퍼티 값을 읽어옵니다.
	@Value("${itemImgLocation}")
	private String itemImgLocation;
	
	// 1-2.
	private final ItemImgRepository itemImgRepository;
	
	// 1-3.
	private final FileService fileService;
	
	// 1-4. 상품 이미지 저장
	public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
		String oriImgName = itemImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";
		
		// 1-5. 실제 파일 업로드
		if(!StringUtils.isEmpty(oriImgName)) {
			// 1-6. itemName : 실제 로컬에 저장된 상품 이미지 파일의 이름, oriImgName : 업로드 했던 상품 이미지 파일의 원래 이름
			imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			
			// 1-7. imgUrl : 업로드 결과 로컬에 저장된 상품이미지 파일을 불러오는 요청 경로
			imgUrl = "/images/item/" + imgName;
		}
		
		// 1-6. 상품 이미지 정보 저장
		itemImg.updateItemImg(oriImgName, imgName, imgUrl);
		itemImgRepository.save(itemImg);
	}
	
	// 2-1. 상품 이미지 수정
	public void updateItemImg(Long itemId, MultipartFile itemImgFile) throws Exception {
		
		// 2-2. 상품 이미지를 수정하는 경우 상품 이미지를 업데이트 한다.
		if(!itemImgFile.isEmpty()) {
			// 2-3. 저장된 이미지를 가져온다.
			ItemImg savedItemImg = itemImgRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
			
			// 2-4. 저장된 이미지 파일 명이 저장된 곳에 이미 있다면 기존 이미지 파일을 삭제한다.
			if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
				fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName()); 
			}
			
			// 2-5. 2-4가 아니라면 전달 받은 MultipartFile itemImgFile의 원본 파일명을 가지고온다.
			String oriImgName = itemImgFile.getOriginalFilename();
			
			// 2-6. 새로운 이미지를 업데이트 해준다.
			String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			
			// 2-7. 변경된 상품이미지 경로를 만들어 준다.
			String imgUrl = "/images/item/" + imgName;
		
			// 2-8. 엔티티에 만들어 놓은 updateItemImg 메소드를 이용해 변경감지(더티체킹)를 일으켜 update 쿼리를 실행시킨다. 
			savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
		}
		
	}
}
