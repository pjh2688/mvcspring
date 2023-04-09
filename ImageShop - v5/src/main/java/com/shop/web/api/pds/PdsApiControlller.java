package com.shop.web.api.pds;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.shop.domain.pds.Pds;
import com.shop.domain.pds.PdsFile;
import com.shop.mapper.pds.IPdsMapper;
import com.shop.service.pds.PdsService;
import com.shop.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PdsApiControlller {
	
	@Value("${uploadFilePath}")
	private String uploadFilePath;

	private final PdsService pdsService;
	private final IPdsMapper pdsMapper;
	
	@PostMapping("/pds")
	public ResponseEntity<?> register(@RequestPart("item") String itemString, MultipartFile[] files) throws Exception { // 1-1. @RequestPart -> javascript에서 FormData에 담아서 보내는 데이터를 하나하나 매핑 해주는 어노테이션. 
		log.info("자료 등록하기 api(다중 파일 업로드)");
		
		pdsService.registerPds(itemString, files);
			
		return new ResponseEntity<>(new CMRespDto<>(1, "자료 등록하기 성공", null), HttpStatus.OK);
	}
	
	@GetMapping("/pds")
	public ResponseEntity<?> readAll() throws Exception {
		log.info("readAll()");
		
		List<PdsFile> result = pdsService.findAll();
		
		return new ResponseEntity<>(new CMRespDto<>(1, "파일 정보 List 불러오기 성공", result), HttpStatus.OK);
	}
	
	@GetMapping("/pds/{pdsItemId}")
	public ResponseEntity<?> readByPdsItemId(@PathVariable("pdsItemId") Long pdsItemId) throws Exception {
		log.info("readByPdsItemId");
		
		Pds pds = pdsService.findByPdsItemId(pdsItemId);		
		
		return new ResponseEntity<>(new CMRespDto<>(1, pdsItemId + "번 아이템 불러오기 성공", pds), HttpStatus.OK);
	}
	
	@PutMapping("/pds/{pdsItemId}")
	public ResponseEntity<?> updateByPdsItemId(@PathVariable("pdsItemId") Long pdsItemId, @RequestPart("item") String itemString, MultipartFile[] files) throws Exception {
		log.info("readByPdsItemId");
		
		int result = pdsService.updatePds(pdsItemId, itemString, files);
		
		return new ResponseEntity<>(new CMRespDto<>(1, pdsItemId + "번 pds정보 업데이트", result), HttpStatus.OK);
	}
	
	@DeleteMapping("/pds/{pdsFileId}/file")
	public ResponseEntity<?> deleteByPdsFileId(@PathVariable("pdsFileId") Long pdsFileId) throws Exception {
		log.info("deleteByPdsFileId");
		
		int result = pdsService.deleteFileByPdsFileId(pdsFileId);
		
		return new ResponseEntity<>(new CMRespDto<>(1, pdsFileId + "번 파일 삭제 성공", result), HttpStatus.OK);
	}
	
	@GetMapping("/pds/{pdsItemId}/files")
	public ResponseEntity<?> readAllFileByPdsItemId(@PathVariable("pdsItemId") Long pdsItemId) throws Exception {
		log.info("readAllFileByPdsItemId");
		
		List<PdsFile> pdsFiles = pdsService.findAllByPdsItemId(pdsItemId);
		
		return new ResponseEntity<>(new CMRespDto<>(1, pdsItemId + "번 아이템 파일 리스트 불러오기 성공", pdsFiles), HttpStatus.OK);
	}
	
	@GetMapping("/pds/download/{pdsFileId}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable("pdsFileId") Long pdsFileId) throws Exception {
		
		ResponseEntity<byte[]> fileData = null;
		
		PdsFile fileInfo = pdsService.findFileByPdsFileId(pdsFileId);
		
		String savedFileName =  fileInfo.getFileUrl();
		String fileName = fileInfo.getFileName();
		
		try {
			HttpHeaders headers = new HttpHeaders();
			
			File file = new File(uploadFilePath + "/" + File.separator + savedFileName);
			
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			String encodedUploadFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");

			String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
		
			// 주의 : 키 값을 직접 스트링으로 적어놓으면 제대로 동작 안함.
			headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
			
			byte[] result = FileCopyUtils.copyToByteArray(file);
			
			fileData = new ResponseEntity<byte[]>(result, headers, HttpStatus.CREATED);
			
		} catch (Exception e) {
			e.printStackTrace();
			fileData = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}
		
		return fileData;
	}
	
	
//	@GetMapping("/pds/download/{pdsFileId}")
	public ResponseEntity<Resource> downloadFile02(@PathVariable("pdsFileId") Long pdsFileId) throws Exception {
		
		PdsFile fileInfo = pdsMapper.findByPdsFileId(pdsFileId).get();
		
		String savedFileName =  fileInfo.getFileUrl();
		String fileName = fileInfo.getFileName();
		
		UrlResource resource = new UrlResource("file:" + uploadFilePath + "\\" + savedFileName);
		
		log.info("uploadFileName = {}", fileName);
		
		String encodedUploadFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		
		String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
		
		return ResponseEntity.ok()
							.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
							.body(resource);
	}
	
}
// 2023-03-23 -> 파일 다운로드 내방식으로 성공