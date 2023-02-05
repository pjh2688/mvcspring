package com.shop.service.codegroup;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.domain.codegroup.CodeGroup;
import com.shop.handler.exception.CustomApiException;
import com.shop.mapper.codegroup.ICodeGroupMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class CodeGroupService {

	private final ICodeGroupMapper mapper;
	
	public int register(CodeGroup codeGroup) {
		
		Optional<CodeGroup> codeGroupOp = mapper.findByGroupCode(codeGroup.getGroupCode());
		
		if(!codeGroupOp.isPresent()) {
			int result = mapper.save(codeGroup);
			
			return result;
			
		} else {
			throw new CustomApiException("해당 코드그룹이 이미 존재합니다.");
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<CodeGroup> findAll(){
		
		List<CodeGroup> result = null;
	
		try {
			result = mapper.findAll();
			
		} catch (Exception e) {
			throw new CustomApiException("코드그룹 리스트를 불러오는데 실패하였습니다.");
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public CodeGroup findByGroupCode(String groupCode) {
		
		Optional<CodeGroup> codeGroupOp = mapper.findByGroupCode(groupCode);
		
		if(codeGroupOp.isPresent()) {
			return codeGroupOp.get();
			
		} else {
			throw new CustomApiException("해당 코드그룹이 존재하지 않습니다.");
		}
		
	}
	
	public int update(CodeGroup codeGroup){
		Optional<CodeGroup> codeGroupOp = mapper.findByGroupCode(codeGroup.getGroupCode());
		
		if(codeGroupOp.isPresent()) {
			return mapper.update(codeGroup);
		} else {
			throw new CustomApiException("해당 코드그룹이 존재하지 않습니다.");
		}
		
	}
	
	public int delete(String groupCode) {
		Optional<CodeGroup> codeGroupOp = mapper.findByGroupCode(groupCode);
		
		if(codeGroupOp.isPresent()) {
			return mapper.delete(groupCode);
		} else {
			throw new CustomApiException("해당 코드그룹이 존재하지 않습니다.");
		}
	}
	
}
