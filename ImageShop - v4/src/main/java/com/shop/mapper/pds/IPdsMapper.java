package com.shop.mapper.pds;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IPdsMapper {

	public void addAttach(@Param("fullName") String fullName, @Param("itemId") Long itemId);
}
