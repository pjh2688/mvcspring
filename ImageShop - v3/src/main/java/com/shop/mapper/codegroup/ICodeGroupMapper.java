package com.shop.mapper.codegroup;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.shop.domain.codegroup.CodeGroup;

@Mapper
public interface ICodeGroupMapper {

	/* 1-1. 코드그룹 저장 */
	public int save(CodeGroup codeGroup);
	
	/* 1-2. 코드그룹 리스트 불러오기 */
	public List<CodeGroup> findAll();
	
	/* 1-3. 그룹코드로 해당 코드그룹 가져오기 */
	public Optional<CodeGroup> findByGroupCode(String groupCode);

	/* 1-4. 그룹코드로 해당 코드그룹 수정 */
	public int update(CodeGroup codeGroup);
	
	/* 1-5. 그룹코드로 해당 코드그룹 삭제 */
	public int delete(String groupCode);
}
