package com.shop.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.item.ItemSellStatus;
import com.shop.entity.item.Item;
import com.shop.entity.item.ItemRepository;
import com.shop.entity.item.QItem;

@SpringBootTest
class ItemRepositoryTest {

	// 1.
	@Autowired
	ItemRepository itemRepository;
	
	// 2.
	@Test
	@DisplayName("상품 저장 테스트")
	public void createItemTest() {
		Item item = new Item();
		item.setItemNm("테스트 상품");
		item.setPrice(10000);
		item.setItemDetail("테스트 상품 상세 설명");
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		item.setRegTime(LocalDateTime.now());
		
		Item savedItem = itemRepository.save(item);
		System.out.println(savedItem.toString());
		
	}
	
	// 3.
	@Test
	@DisplayName("상품 리스트 저장 테스트 ")
	public void createItemList1() {
		for(int i = 1; i <= 10; i++) {
			Item item = new Item();
			item.setItemNm("테스트 상품" + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세 설명" + i);
			item.setItemSellStatus(ItemSellStatus.SELL);
			item.setStockNumber(100);
			item.setRegTime(LocalDateTime.now());
			
			itemRepository.save(item);
		}
	}
	
	// 4.
	@Test
	@DisplayName("상품 조회 테스트 ")
	public void findByItemNmTest() {
		this.createItemList1();
		
		List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	// 5.
	@Test
	@DisplayName("상품명, 상품상세설명 or 테스트")
	public void findByItemNmOrItemDetailTest() {
		this.createItemList1();
		
		List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	// 6.
	@Test
	@DisplayName("가격 LessThan(미만) 테스트")
	public void findByPriceLessThanTest() {
		this.createItemList1();
		
		List<Item> itemList = itemRepository.findByPriceLessThan(10005);
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}

	// 7. 
	@Test
	@DisplayName("가격 내림차순 테스트")
	public void findByPriceLessThanOrderByDescTest() {
		this.createItemList1();
		
		List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	// 8.
	@Test
	@DisplayName("@Query를 이용한 상품 조회 테스트")
	public void findByItemDetailTest() {
		this.createItemList1();
		
		List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	// 9.
	@Test
	@DisplayName("@Query에서 nativeQuery 속성을 이용한 상품 조회 테스트")
	public void findByItemDetailByNativeTest() {
		this.createItemList1();
		
		List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	
	// 10-1. 영속성 컨텍스트를 사용하기 위해 @PersistenceContext 어노테이션을 이용해 EntityManager 빈을 주입한다.
	@PersistenceContext
	EntityManager em;
	
	@Test
	@DisplayName("Querydsl 조회 테스트 1")
	public void queryDslTest1() {
		this.createItemList1();
		
		// 10-2. JPAQueryFactory를 이용하여 쿼리를 동적으로 생성한다. 생성자의 파리미터로는 10-1의 EntityManager em을 넣어준다.
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		
		// 10-3. Querydsl을 통해 쿼리를 생성하기 위해 플러그인을 통해 자동으로 생성된 QItem 객체를 이용
		QItem qItem = QItem.item;
		
		// 10-4. 자바 소스로 SQL문과 비슷하게 쿼리를 짤 수 있다.
		JPAQuery<Item> query = queryFactory.selectFrom(qItem)
										   .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
										   .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
										   .orderBy(qItem.price.desc());
		
		// 10-5. JPAQuery 메소드중 하나인 fetch를 이용해서 쿼리 결과를 리스트로 반환합니다. fetch() 메소드 실행 시점에 쿼리문이 실행됩니다.
		List<Item> itemList = query.fetch();
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
		
	
	}
	
	// 11-1. 상품 데이터를 만드는 새로운 메소드 생성, 1번부터 5번 상품은 상품의 판매 상태가 SELL(판매중), 6번부터 10번 상품은 판매 상태를 SOLD_OUT(품절)로 세팅해서 생성.
	public void createItemList2() {
		for(int i = 1; i <= 5; i++) {
			Item item = new Item();
			item.setItemNm("테스트 상품 " + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세 설명 " + i);
			item.setItemSellStatus(ItemSellStatus.SELL);
			item.setStockNumber(100);
			item.setRegTime(LocalDateTime.now());
			
			itemRepository.save(item);
		}
		
		for(int i = 6; i <= 10; i++) {
			Item item = new Item();
			item.setItemNm("테스트 상품 " + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세 설명 " + i);
			item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
			item.setStockNumber(0);
			item.setRegTime(LocalDateTime.now());
			
			itemRepository.save(item);
		}
	}
	
	@Test
	@DisplayName("상품 Querydsl 조회 테스트 2")
	public void queryDslTest2() {
		this.createItemList2();
		
		// 11-2. BooleanBuilder : 쿼리에 들어갈 조건을 만들어주는 빌더. Predicate를 구현하고 있으며 메소드 체인 형식으로 사용할 수 있다.
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		
		// 11-3. Querydsl을 통해 query를 생성하기 위해 QItem 객체를 가져온다.
		QItem qItem = QItem.item;
		
		String itemDetail = "테스트 상품 상세 설명";
		int price = 10003;
		String itemSellStat = "SELL";
		
		// 11-4. and조건 추가
		booleanBuilder.and(qItem.itemDetail.like("%" + itemDetail + "%"));
		booleanBuilder.and(qItem.price.gt(price));
		
		// 11-5. 아이템상태코드가 SELL일때
		if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
			booleanBuilder.and(qItem.itemSellStatus.eq(ItemSellStatus.SELL));
		}
		
		// 11-6. 데이터를 페이징해 조회하는 메소드 PageRequest.of() 메소드를 이용하여 pageable 객체를 생성.
		Pageable pageable = PageRequest.of(0, 5);
		
		// 11-7. QueryDslPredicateExecutor 인터페이스에서 정의해놓은 findAll 메소드를 이용해 조건에 맞는 데이터를 Page 객체에 담는다.
		Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
		
		System.out.println("total element : " + itemPagingResult.getTotalElements());
		
		List<Item> resultItemList = itemPagingResult.getContent();
		for(Item resultItem : resultItemList) {
			System.out.println(resultItem.toString());
		}
	}
	
	/*
	 * 주의 : 테스트시 오류날 경우 
	 *  1. project 우클릭 -> build path
	 *  2. source 탭에서 src/main/java에 설정된 output폴더 경로와 src/main/generated에 설정된 output폴더 경로가 다르다면 전자와 맞춰준다.
	 **/
}
