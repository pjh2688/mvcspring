package com.shop.repository.custom;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.item.ItemSellStatus;
import com.shop.dto.item.ItemSearchDto;
import com.shop.dto.item.MainItemDto;
import com.shop.dto.item.QMainItemDto;
import com.shop.entity.item.Item;
import com.shop.entity.item.QItem;
import com.shop.entity.item.QItemImg;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {  // 1-1. ItemRepositoryCustom 인터페이스 상속

	// 1-2. 동적으로 쿼리를 생성하기 위해 JPAQueryFactory 클래스 사용
	private JPAQueryFactory queryFactory;
	
	// 1-3. 생성자로 JPAQueryFactory를 이용해 EntityManager 주입
	public ItemRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	/* 1-4. 상품 판매 상태 조건(ItemSellStatus)이 전체(null)일 경우 null을 return하고 
	 *		결과값이 null이면 where절에서 해당 조건은 무시됩니다. 
	 *		상품 판매 조건이 전체(null)이 아니라 SELL(판매중) 이거나(or) SOLD_OUT(품절) 상태라면 해당 조건에 맞는 상품만 조회합니다.
	 */
	private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
		return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
	}
	
	/* 
	 * 1-5. ItemSearchDto.searchDataType의 값에 따라서 dateTime의 값을 이전 시간의 값으로 세팅 후 해당 시간 이후로 등록된 상품만 조회합니다.
     *		ex) searchDataType 값이 "1m"인 경우 dateTime의 시간을 한달 전으로 세팅 후 최근 한달 동ㅇ안 등록된 상품만 조회하도록 조건값을 반환합니다.
	 */
	private BooleanExpression regDtsAfter(String searchDataType) {
		LocalDateTime dateTime = LocalDateTime.now();
		
		if(StringUtils.equals("all", searchDataType) || searchDataType == null) {
			return null;
		} else if(StringUtils.equals("1d", searchDataType)) {
			dateTime = dateTime.minusDays(1);
		} else if(StringUtils.equals("1w", searchDataType)) {
			dateTime = dateTime.minusWeeks(1);
		} else if(StringUtils.equals("1m", searchDataType)) {
			dateTime = dateTime.minusMonths(1);
		} else if(StringUtils.equals("6m", searchDataType)) {
			dateTime = dateTime.minusMonths(6);
		}
		
		return QItem.item.regTime.after(dateTime);
	}
	
	/*
	 *  1-6. searchBy의 값에 따라서 상품명에 검색어를 포함하고 있는 상품 또는 상품 생성자의 아이디에 검색어를 포함하고 있는 상품을 조회하도록 조건값을 반환합니다.
	 */
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		
		if(StringUtils.equals("itemNm", searchBy)) {
			return QItem.item.itemNm.like("%" + searchQuery + "%");
		} else if(StringUtils.equals("createdBy", searchBy)) {
			return QItem.item.createdBy.like("%" + searchQuery + "%");
		}
		
		return null;
	}
	
	/*
	 *  1-7. 조회한 데이터를 Page 클래스의 구현체인 PageImpl 객체로 반환
	 */
	@Override
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		
		List<Item> results = queryFactory.selectFrom(QItem.item)
												 .where(regDtsAfter(itemSearchDto.getSearchDateType()), 
														searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
														searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
												 .orderBy(QItem.item.id.desc())
												 .offset(pageable.getOffset())
												 .limit(pageable.getPageSize())
												 .fetch();
		
		long total = results.size();
												 
		return new PageImpl<>(results, pageable, total);
	}
	
	/*
	 * 1-8. 검색어가 null이 아니면 상품명에 해당 검색어가 포함되ㅣ는 상품을 조회하는 조건을 반환합니다. 
	 */
	private BooleanExpression itemNmLike(String searchQuery) {
		return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
	}

	/*
	 * 1-9. 
	 */
	@Override
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		
		QItem item = QItem.item;
		QItemImg itemImg = QItemImg.itemImg;
		
		List<MainItemDto> results = (List<MainItemDto>) queryFactory.select(
																			new QMainItemDto(
																					item.id,
																					item.itemNm,
																					item.itemDetail, 
																					itemImg.imgUrl, 
																					item.price)
																			).from(itemImg)
																			 .join(itemImg.item, item)
																			 .where(itemImg.repimgYn.eq("Y"))
																			 .where(itemNmLike(itemSearchDto.getSearchQuery()))
																			 .orderBy(item.id.desc())
																			 .offset(pageable.getOffset())
																			 .limit(pageable.getPageSize())
																			 .fetch();
		
		long total = results.size();
		
		return new PageImpl<MainItemDto>(results, pageable, total);
	}
	
	
}
