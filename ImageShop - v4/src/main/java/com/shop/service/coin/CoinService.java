package com.shop.service.coin;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.domain.coin.ChargeCoin;
import com.shop.domain.coin.PayCoin;
import com.shop.domain.member.Member;
import com.shop.handler.exception.CustomApiException;
import com.shop.mapper.coin.ICoinMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CoinService {

	private final ICoinMapper coinMapper;
	
	// 2023-03-08 -> 여기까지함
	public void chargeCoin(Integer amount, Member loginMember) {
		
		ChargeCoin chargeCoin = new ChargeCoin();
		chargeCoin.setUserNo(loginMember.getUserNo());
		chargeCoin.setAmount(amount);
		
		log.info("충전 금액 : " + chargeCoin.getAmount());
		
		try {
			// 1-2. 로그인 계정 정보에 있는 coin 항목 update
			coinMapper.charge(chargeCoin);
			
			// 1-1. 충전 내역 등록
			coinMapper.create(chargeCoin);
			
		} catch (Exception e) {
			throw new CustomApiException("게시글 저장 실패");
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<ChargeCoin> findChargeCoinByUserNo(Long userNo) {
		
		List<ChargeCoin> result = null;
		
		try {
			result = coinMapper.findChargeCoinByUserNo(userNo);
			
		} catch (Exception e) {
			throw new CustomApiException("코인 충전 내역이 존재하지 않습니다.");
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public List<PayCoin> findPayCoinByUserNo(Long userNo) {
		
		List<PayCoin> result = null;
		
		try {
			result = coinMapper.findPayCoinByUserNo(userNo);
		} catch (Exception e) {
			throw new CustomApiException("코인 사용 내역이 존재하지 않습니다.");
		}
		
		return result;
		
	}
}
