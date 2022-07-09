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
 
	@Autowired
	ItemRepository itemRepository;
	
	// 1-1. 영속성 컨텍스트를 사용하기 위해 @PersistenceContext 어노테이션을 이용해 EntityManager 빈을 주입.
	@PersistenceContext
	EntityManager em;
	
	@Test
	@DisplayName("상품 저장 테스트")
	public void createItemTest() {
		Item item = new Item();
		item.setItemNm("테스트 상품");
		item.setPrice(10000);
		item.setItemDetail("테스트 상품 상세내용");
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		item.setRegTime(LocalDateTime.now());
		item.setUpdateTime(LocalDateTime.now());
		
		Item savedItem = itemRepository.save(item);
		System.out.println(savedItem.toString());
	}
	
	public void createItemList1() {
		for(int i = 1; i <= 10; i++) {
			Item item = new Item();
			item.setItemNm("테스트 상품 " + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세내용 " + i);
			item.setItemSellStatus(ItemSellStatus.SELL);
			item.setStockNumber(100);
			item.setRegTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			
			Item savedItem = itemRepository.save(item);
			System.out.println(savedItem.toString());
		}
	}
	
	@Test
	@DisplayName("상품명 조회 테스트")
	public void findByItemNmTest() {
		this.createItemList1();
		
		List<Item> itemList = itemRepository.findByItemNm("테스트 상품 1");
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("상품명, 상품상세 설명 OR 테스트")
	public void findByItemNmOrItemDetailTest() {
		this.createItemList1();
		
		List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품 1", "테스트 상품 상세내용 5");
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	
	}
	
	@Test
	@DisplayName("가격 LessThan(미만) 테스트")
	public void findByPriceLessThanTest() {
		this.createItemList1();
		
		List<Item> itemList = itemRepository.findByPriceLessThan(10005);
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("가격 내림차순 조회 테스트")
	public void findByPriceLessThanOrderByPriceDesc() {
		this.createItemList1();
		
		List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("@Query를 이용한 상품 조회 테스트")
	public void findByItemDetailTest() {
		this.createItemList1();
		
		List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세내용");
				
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
	public void findByItemDetailByNative() {
		this.createItemList1();
		
		List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세내용");
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("상품 Querydsl 조회 테스트 1")
	public void queryDslTest1() {
		this.createItemList1();
		
		// 1-2. JPAQueryFactory를 이용하여 쿼리를 동적으로 생성합니다. 생성자 파라미터에는 1-1에서 주입받은 EntityManager 객체를 넣어줍니다.
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		
		// 1-3. Querydsl을 통해 쿼리를 생성하기 위해 플러그인을 통해 자동으로 생성된 QItem 객체를 이용합니다.
		QItem qItem = QItem.item;
		
		// 1-4. 자바 소스코드지만 SQL문과 비슷하게 소스를 작성할 수 있습니다.
		JPAQuery<Item> query = queryFactory.selectFrom(qItem)
										   .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
										   .where(qItem.itemDetail.like("%" + "테스트 상품 상세내용" + "%"))
										   .orderBy(qItem.price.desc());
		// 1-5. JPAQuery 메소드중 하나인 fetch를 이용해서 쿼리 결과를 리스트로 반환합니다. fetch() 메소드 실행 시점에 쿼리문이 실행됩니다.
		List<Item> itemList = query.fetch();
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	public void createItemList2() {
		for(int i = 0; i <= 5; i++) {
			Item item = new Item();
			item.setItemNm("테스트 상품" + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세내용" + i);
			item.setItemSellStatus(ItemSellStatus.SELL);
			item.setStockNumber(100);
			item.setRegTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			
			itemRepository.save(item);
		}
		
		for(int i = 6; i <= 10; i++) {
			Item item = new Item();
			item.setItemNm("테스트 상품" + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세내용" + i);
			item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
			item.setStockNumber(0);
			item.setRegTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			
			itemRepository.save(item);
		}
	}
	
	@Test
	@DisplayName("상품 Querydsl 조회 테스트 2")
	public void queryDslTest2() {
		
		this.createItemList2();
		
		// 2-1. BooleanBuilder는 쿼리에 조건을 만들어 주는 빌더이다. Predicate를 구현하고 있으며 메소드 체인 형식으로 사용할 수 있습니다.
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		
		QItem item = QItem.item;
		
		String itemDetail = "테스트 상품 상세내용";
		int price = 10003;
		String itemSellStatus = "SELL";
		
		// 2-2. AND 조건 추가 -> itemDetail의 문자가 포함된 값 
		booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
		
		// 2-3. AND 조건 추가 -> price보다 초과된 값.
		booleanBuilder.and(item.price.gt(price));
		
		// 2-4. 판매 상태 조건이 SELL일 때만 booleanBuilder의 판매 상태 조건을 동적으로 추가.
		if(StringUtils.equals(itemSellStatus, ItemSellStatus.SELL)) {
			booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
		}
		
		// 2-5. 첫번째 인자는 조회할 페이지의 번호 , 두번째 인자는 한 페이지당 조회할 데이터 개수
		Pageable pageable = PageRequest.of(0, 5);
		
		// 2-6. QueryDslPredicateExecutor 인터페이스에서 정의 해놓은 findAll() 메소드를 이용해 조건에 맞는 데이터를 Page 객체로 받아옵니다.
		Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
		
		System.out.println("Total Elements : " + itemPagingResult.getTotalElements());
		
		List<Item> resultItemList = itemPagingResult.getContent();
		
		for(Item resultItem : resultItemList) {
			System.out.println(resultItem.toString());
		}
		
	}

}
