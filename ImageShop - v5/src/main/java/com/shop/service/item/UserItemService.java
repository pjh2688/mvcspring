package com.shop.service.item;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.domain.coin.PayCoin;
import com.shop.domain.item.Item;
import com.shop.domain.item.UserItem;
import com.shop.domain.member.Member;
import com.shop.handler.exception.CustomApiException;
import com.shop.mapper.coin.ICoinMapper;
import com.shop.mapper.item.IUserItemMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserItemService {

	private final IUserItemMapper userItemMapper;
	
	private final ICoinMapper coinMapper;
	
	public int register(Member member, Item item) {
		log.info("---------- 구매하기 ------------");
		
		Long userNo = member.getUserNo();
		
		Long itemId = item.getItemId();
		Integer price = item.getPrice();
		
		UserItem userItem = new UserItem();
		userItem.setUserNo(userNo);
		userItem.setItemId(itemId);
		userItem.setPrice(price);
		
		PayCoin payCoin = new PayCoin();
		payCoin.setUserNo(userNo);
		payCoin.setItemId(itemId);
		payCoin.setAmount(price);
		
		coinMapper.pay(payCoin);
		coinMapper.createPayHistory(payCoin);
		
		int result = userItemMapper.save(userItem);
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public List<UserItem> findAllByUserNo(Long userNo) {
		List<UserItem> result = null;
		
		try {
			result = userItemMapper.findAllByUserNo(userNo);
			
		} catch (Exception e) {
			throw new CustomApiException("구매 내역 리스트 정보가 존재하지 않습니다.");
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public UserItem findByUserItemNo(Long userItemNo) {
		Optional<UserItem> userItemOp = userItemMapper.findByUserItemNo(userItemNo);
		
		if(userItemOp.isPresent()) {
			return userItemOp.get();
			
		} else {
			throw new CustomApiException("해당 구매 내역이 존재하지 않습니다.");
		}
	}
}
