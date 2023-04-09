package com.shop.mapper.pds;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.shop.domain.pds.Pds;
import com.shop.domain.pds.PdsFile;

@Mapper
public interface IPdsMapper {

	public void save(Pds pds);
	
	public void addAttach(PdsFile file);
	
	public void updateViewCnt(Long pdsItemId);
	
	public Optional<Pds> findByPdsItemId(Long pdsItemId);
	
	public List<PdsFile> findAllFileByPdsItemId(Long pdsItemId);
	
	public Optional<PdsFile> findByPdsFileId(Long pdsFileId);
	
	public List<PdsFile> findAll();
	
	public List<Pds> findAllByPdsItemId(Long pdsItemId);
	
	public int updatePds(Pds pdsInfo);
	
	public int deleteAllByPdsItemId(Long pdsItemId);
	
}
