package com.shop.service.codedetail;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.domain.codedetail.CodeDetail;
import com.shop.domain.codelabel.CodeLabelValue;
import com.shop.handler.exception.CustomApiException;
import com.shop.mapper.codedetail.ICodeDetailMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class CodeDetailService {
	
	private final ICodeDetailMapper codeDetailMapper;
	
	@Transactional(readOnly = true)
	public List<CodeLabelValue> getCodeGroupList() {
		
		List<CodeLabelValue> result = null;
		
		try {
			result = codeDetailMapper.getCodeLabelValueList();
			
		} catch (Exception e) {
			throw new CustomApiException("코드 라벨과 코드 값 가져오기 실패");
		}
		
		return result;
	}
	
	public int register(CodeDetail codeDetail) {
		
		Optional<CodeDetail> codeDetailOp = codeDetailMapper.findByGroupCodeAndCodeValue(codeDetail);
		
		if(!codeDetailOp.isPresent()) {
			
			String groupCode = codeDetail.getGroupCode();
			
			int maxSortSeq = codeDetailMapper.getMaxSortSeq(groupCode);
			
			codeDetail.setSortSeq(maxSortSeq + 1); 
			
			int result = codeDetailMapper.save(codeDetail);
			
			return result;
			
		} else {
			
			throw new CustomApiException("해당 CodeDetail이 이미 존재합니다.");
		}
	}
	
	@Transactional(readOnly = true)
	public List<CodeDetail> findAll() {
		
		List<CodeDetail> result = null;
		
		try {
			result = codeDetailMapper.list();
			
		} catch (Exception e) {
			throw new CustomApiException("CodeDetail 정보가 존재하지 않습니다.");
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public CodeDetail findByGroypCodeAndCodeValue(CodeDetail codeDetail) {
		
		Optional<CodeDetail> codeDetailOp = codeDetailMapper.findByGroupCodeAndCodeValue(codeDetail);
		
		if(codeDetailOp.isPresent()) {
			return codeDetailOp.get();
			
		} else {
			throw new CustomApiException("해당 CodeDetail이 존재하지 않습니다.");
		}
		
	}
	
	public int delete(CodeDetail codeDetail) {
		
		Optional<CodeDetail> codeDetailOp = codeDetailMapper.findByGroupCodeAndCodeValue(codeDetail);
		
		if(codeDetailOp.isPresent()) {
			return codeDetailMapper.delete(codeDetail);
		} else {
			throw new CustomApiException("해당 CodeDetail이 존재하지 않습니다.");
		}
		
	}
	
	public int update(CodeDetail codeDetail){
		
		Optional<CodeDetail> codeDetailOp = codeDetailMapper.findByGroupCodeAndCodeValue(codeDetail);
		
		if(codeDetailOp.isPresent()) {
			return codeDetailMapper.update(codeDetail);
		} else {
			throw new CustomApiException("해당 CodeDetail이 존재하지 않습니다.");
		}
		
	}
}
