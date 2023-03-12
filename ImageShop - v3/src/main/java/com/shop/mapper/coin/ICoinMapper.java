package com.shop.mapper.coin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shop.domain.coin.ChargeCoin;
import com.shop.domain.coin.PayCoin;

@Mapper
public interface ICoinMapper {

	// 1-1. 충전 내역 저장.
	public int create(ChargeCoin chargeCoin);
	
	// 1-2. 멤버 코인 충전
	public int charge(ChargeCoin chargeCoin);
	
	// 1-3. 멤버 코인 충전 내역 리스트
	public List<ChargeCoin> findByUserNo(Long userNo);
	 
	// 1-4. 코인 지불(결제)
	public int pay(PayCoin payCoin);
	
	// 1-5. 구매 내역 등록
	public int createPayHistory(PayCoin payCoin);
}
