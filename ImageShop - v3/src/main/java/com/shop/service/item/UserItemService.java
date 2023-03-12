package com.shop.service.item;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.domain.coin.PayCoin;
import com.shop.domain.item.Item;
import com.shop.domain.item.UserItem;
import com.shop.domain.member.Member;
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
}
