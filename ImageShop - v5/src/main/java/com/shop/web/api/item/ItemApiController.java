package com.shop.web.api.item;

import java.io.File;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shop.config.auth.PrincipalDetails;
import com.shop.domain.item.Item;
import com.shop.domain.member.Member;
import com.shop.service.item.ItemService;
import com.shop.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ItemApiController {
	
	@Value("${uploadPath}")
	private String uploadPath;
	
	private final ItemService itemService;
	private final MessageSource messageSource;

	// 주의 : 파일처리 중에는 Excetpion을 그냥 전체에 걸어준다. -> 그래야 jwt 관련 exception이 정상동작.
	@PostMapping("/items")
	public ResponseEntity<?> register(@RequestPart("item") String itemString, // 1-1. @RequestPart -> javascript에서 FormData에 담아서 보내는 데이터를 하나하나 매핑 해주는 어노테이션. 
									  @RequestPart(name = "picture_file", required = false) MultipartFile picture_file, 
									  @RequestPart(name = "preview_file", required = false) MultipartFile preview_file) throws Exception {
		
		log.info("item : " + itemString);
		
		itemService.itemSave(itemString, picture_file, preview_file);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "아이템 등록하기 성공", null), HttpStatus.OK);
	}
	
	@GetMapping("/items")
	public ResponseEntity<?> list(Authentication authentication) throws Exception {
		log.info("list");
		
		List<Item> result = itemService.findAll();
		
		log.info("list result = " + result);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "아이템 리스트 불러오기 성공", result), HttpStatus.OK);
	}
	
	@GetMapping("/items/{itemId}")
	public ResponseEntity<?> readByItemId(@PathVariable("itemId") Long itemId) throws Exception {
		log.info("readByitemId");
		
		Item findItem = itemService.findByItemId(itemId);
		
		log.info("readByitemId = " + findItem);
		
		return new ResponseEntity<>(new CMRespDto<>(1, itemId + "번 아이템 불러오기 성공", findItem), HttpStatus.OK);
	}
	
	@GetMapping("/items/download/{itemId}/{type}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable("itemId") Long itemId, @PathVariable("type") String type) throws Exception {
		ResponseEntity<byte[]> fileData = null;
		
		Item findItem = itemService.findByItemId(itemId);
		
		String savedFileName = "";
		String fileName = "";
		
		if(type.equals("preview")) {
			savedFileName = findItem.getPreviewUrl();
			fileName = findItem.getPreviewName();
			
			System.out.println(savedFileName);
			System.out.println(fileName);
		} else if(type.equals("profile")) {
			savedFileName = findItem.getPictureUrl();
			fileName = findItem.getPictureName();
			
			System.out.println(savedFileName);
			System.out.println(fileName);
		}
		
		try {
			HttpHeaders headers = new HttpHeaders();
			
			File file = new File(uploadPath + "/" + File.separator + savedFileName);
			
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.add("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
			
			fileData = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.CREATED);
			
		} catch (Exception e) {
			e.printStackTrace();
			fileData = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}
		
		return fileData;
	}
	
	@PutMapping("/items/{itemId}")
	public ResponseEntity<?> updateByItemId(@PathVariable("itemId") Long itemId, 
											@RequestPart("item") String itemString, // 1-1. @RequestPart -> javascript에서 FormData에 담아서 보내는 데이터를 하나하나 매핑 해주는 어노테이션. 
											@RequestPart(name = "picture_file", required = false) MultipartFile picture_file, 
											@RequestPart(name = "preview_file", required = false) MultipartFile preview_file) throws Exception {
		
		log.info("updateByItemId");
		
		itemService.itemUpdate(itemId, itemString, picture_file, preview_file);
		
		return new ResponseEntity<>(new CMRespDto<>(1, itemId + "번 아이템 정보 수정 성공", null), HttpStatus.OK);
	}
	
	@DeleteMapping("/items/{itemId}")
	public ResponseEntity<?> deleteByItemId(@PathVariable("itemId") Long itemId) throws Exception {
		
		log.info("deleteByItemId");
		
		itemService.deleteItem(itemId);
		
		return new ResponseEntity<>(new CMRespDto<>(1, itemId + "번 아이템 삭제 성공", null), HttpStatus.OK);
	}
	
	// 2023-03-10 -> 아이템 구매까지 완료.
	@GetMapping("/items/buy/{itemId}")
	public ResponseEntity<?> itemBuyByItemId(@PathVariable("itemId") Long itemId, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
		Member loginMember = principalDetails.getMember();
		
		itemService.itemBuy(loginMember, itemId);
		
		String message = messageSource.getMessage("item.purchaseComplete", null, Locale.KOREAN);
		
		return new ResponseEntity<>(new CMRespDto<>(1, itemId + message, null), HttpStatus.OK);
	}	
}
