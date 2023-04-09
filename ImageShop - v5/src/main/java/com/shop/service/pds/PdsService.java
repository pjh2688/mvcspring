package com.shop.service.pds;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.domain.pds.Pds;
import com.shop.domain.pds.PdsFile;
import com.shop.handler.exception.CustomApiException;
import com.shop.mapper.pds.IPdsMapper;
import com.shop.service.item.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class PdsService {

	private final IPdsMapper pdsMapper;
	
	private final FileService fileService;
	
	public void registerPds(String itemString, MultipartFile[] files) throws Exception {
		
		// 1-1. itemString값 찍어보기
		log.info("itemString:" + itemString);
						
		// 1-2. itemString을 ObjectMapper를 사용해 Pds 객체로 형 변환하여 저장.
		Pds pds = new ObjectMapper().readValue(itemString, Pds.class);
				
		// 1-3. 매개변수로 넘어온 file이 있을때만 동작
		if(files != null) {			
			// 1-4.
			PdsFile pdsFile = new PdsFile();
			
			// 1-5. 파일이 넘어온 만큼 for문을 돌린다음 해당 file에 대한 정보를 db에 저장.
			for(MultipartFile file : files) {
				String createdFilename = fileService.uploadFileByPds(file.getOriginalFilename(), file.getBytes());
				
				pdsFile.setPdsItemId(pds.getPdsItemId());
				pdsFile.setFileName(file.getOriginalFilename());
				pdsFile.setFileUrl(createdFilename);
				pdsFile.setDownCnt(0);
				
				try {
					
					pdsMapper.addAttach(pdsFile);
				} catch (Exception e) {
					throw new CustomApiException("첨부파일 데이터 DB 저장 실패");
				}
			}
		}
		
		// 1-6. 파일 저장이 끝났으면 pds객체도 저장.
		try {
			pdsMapper.save(pds);
		} catch (Exception e) {
			throw new CustomApiException("자료 저장 실패");
		}
		
	}
	
	public int updatePds(Long pdsItemId, String itemString, MultipartFile[] files) throws Exception {
	
		// 2-1. itemString을 ObjectMapper를 사용해 Item 객체로 형 변환하여 저장.
		Pds pds = new ObjectMapper().readValue(itemString, Pds.class);
				
		// 2-2. 수정할 itemId를 파라미터로 받아와 2-1에 셋팅
		pds.setPdsItemId(pdsItemId);
		
		String pdsItemName = pds.getPdsItemName();
		String description = pds.getDescription();
		
		// 2-3. 넘겨 받은 itemString에 담긴 값을 2-1에 셋팅 1
		if(pdsItemName != null) {
			log.info("pds.getPdsItemName() : " + pdsItemName);
			pds.setPdsItemName(pdsItemName);
		} 
				
		// 2-4. 넘겨 받은 itemString에 담긴 값을 2-1에 셋팅 2
		if(description != null) {
			log.info("item.getDescription() : " + description);
			pds.setDescription(description);
		}
		
		// 2-5. update 폼에서 넘어오는 폼에 파일이 있다면 기존 파일을 날리고 DB에서 정보도 삭제하고 새로 입력.
		if(files != null) {			
			// 2-6. 
			PdsFile pdsFile = new PdsFile();
			
			// 2-7. pdsItemId로 저장된 기존 파일 정보를 가져온다.
			List<PdsFile> result = pdsMapper.findAllFileByPdsItemId(pds.getPdsItemId());
			
			// 2-8. 저장된 파일 명을 가져와서
			for(PdsFile savedFile : result) {
				// 2-9. 기존 파일 삭제 
				fileService.deletePdsFile(savedFile.getFileUrl());
				
			}
			
			// 2-9. 수정하기 전 pdsItemId로 등록된 기존 파일 정보 DB에서 모두 삭제
			pdsMapper.deleteAllByPdsItemId(pds.getPdsItemId());
			
			// 2-10. 파일이 넘어온 만큼 for문을 돌린다음 해당 file에 대한 정보를 db에 저장.
			for(MultipartFile file : files) {
				
				String createdFilename = fileService.uploadFileByPds(file.getOriginalFilename(), file.getBytes());
						
				pdsFile.setPdsItemId(pds.getPdsItemId());
				pdsFile.setFileName(file.getOriginalFilename());
				pdsFile.setFileUrl(createdFilename);
				pdsFile.setDownCnt(0);
						
				try {
					pdsMapper.addAttach(pdsFile);
				} catch (Exception e) {
					throw new CustomApiException("첨부파일 데이터 DB 업데이트 실패");
				}
			}
		} 
		
		// 2-11. 위에서 셋팅한 pds 정보 업데이트 
		int result = 0;
		
		try {
			result = pdsMapper.updatePds(pds);
		} catch (Exception e) {
			throw new CustomApiException("pds정보 db 업데이트 실패");
		}
		
		return result;
	}
	
	public int deleteFileByPdsFileId(Long pdsFileId) {
		int result = 1;
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public Pds findByPdsItemId(Long pdsItemId) {
		
		Optional<Pds> pdsOp = pdsMapper.findByPdsItemId(pdsItemId);
		
		if(pdsOp.isPresent()) {
			return pdsOp.get();
			
		} else {
			throw new CustomApiException("해당 pds정보가 존재하지 않습니다.");
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<PdsFile> findAllByPdsItemId(Long pdsItemId) {
		List<PdsFile> result = null;
		
		try {
			result = pdsMapper.findAllFileByPdsItemId(pdsItemId);
			
		} catch (Exception e) {
			throw new CustomApiException("pds파일정보 가져오기 실패");
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public PdsFile findFileByPdsFileId(Long pdsFileId) {
		
		Optional<PdsFile> pdsFileOp = pdsMapper.findByPdsFileId(pdsFileId);
		
		if(pdsFileOp.isPresent()) {
			return pdsFileOp.get();
			
		} else {
			throw new CustomApiException("해당 pds정보가 존재하지 않습니다.");
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<PdsFile> findAll() {
		List<PdsFile> result = null;
		
		try {
			result = pdsMapper.findAll();
			
		} catch (Exception e) {
			throw new CustomApiException("pds파일정보 가져오기 실패");
		}
		
		return result;
	}
}
