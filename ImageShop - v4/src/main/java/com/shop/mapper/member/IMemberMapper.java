package com.shop.mapper.member;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.shop.domain.codelabel.CodeLabelValue;
import com.shop.domain.member.Member;
import com.shop.domain.member.MemberAuth;

@Mapper
public interface IMemberMapper {

	public int createAuth(MemberAuth memberAuth);
	
	public int deleteAuth(Long userNo);
	
	public List<CodeLabelValue> getCodeLabelValueList(String groupCode);
	
	public Optional<Member> findByUserId(String userId);
	
	public Optional<Member> findByUserNo(Long userNo);
	
	public int save(Member member);
	
	public List<Member> findAll();
	
	public int update(Member member);
	
	public Long findUserNoByUserId(String userId);
	
	public int delete(Long userNo);
	
	public int countAll();
	
	public List<MemberAuth> findMemberAuthByUserNo(Long userNo);
	
	public Optional<Member> findMemberByRefreshToken(String refreshToken);
	
	public int getCoin(Long userNo);

}

