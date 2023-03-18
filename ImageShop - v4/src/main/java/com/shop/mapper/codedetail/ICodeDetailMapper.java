package com.shop.mapper.codedetail;

import java.util.List;
import java.util.Optional;

import com.shop.domain.codedetail.CodeDetail;
import com.shop.domain.codelabel.CodeLabelValue;

public interface ICodeDetailMapper {
	
	public List<CodeLabelValue> getCodeLabelValueList();
	
	public Optional<CodeDetail> findByGroupCodeAndCodeValue(CodeDetail codeDetail);
	
	public int save(CodeDetail codeDetail);
	
	public Integer getMaxSortSeq(String groupCode);
	
	public List<CodeDetail> list();
	
	public int delete(CodeDetail codeDetail);
	
	public int update(CodeDetail codeDetail);
}
