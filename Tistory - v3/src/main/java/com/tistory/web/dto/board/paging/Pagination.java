package com.tistory.web.dto.board.paging;

import lombok.Getter;

@Getter
public class Pagination {

	// 1. 전체 데이터 갯수
	private int totalRecordCount;
	
	// 2. 전체 페이지 수
	private int totalPageCount;
	
	// 3. 첫번째 페이지 번호
	private int startPage;
	
	// 4. 끝 페이지 번호
	private int endPage;
	
	// 5. LIMIT 시작 위치
	private int limitStart;
	
	// 6. 이전 페이지 존재 여부
	private boolean existPrevPage;
	
	// 7. 다음 페이지 존재 여부
	private boolean existNextPage;
	
	// 8. 생성자
	public Pagination(int totalRecordCount, CommonParams params) {
		if(totalRecordCount > 0) {
			this.totalRecordCount = totalRecordCount;
			this.calculation(params);
		}
	}
	
	// 9-1. 전체 페이지 수 계산해주는 메소드
	private void calculation(CommonParams params) {
		// 9-2. 전체 페이지 수 계산
		totalPageCount = ((totalRecordCount - 1) / params.getRecordPerPage()) + 1;
		
		// 9-3. 현재 페이지 번호가 전체 페이지 수보다 큰 경우, 현재 페이지 번호에 전체 페이지 수 저장
		if(params.getPage() > totalPageCount) {
			params.setPage(totalPageCount);
		}
		
		// 9-4. 첫 페이지 번호 계산
		startPage = ((params.getPage() - 1) / params.getPageSize()) * params.getPageSize() + 1;
		
		// 9-5. 끝 페이지 번호 계산
		endPage = startPage + params.getPageSize() - 1;
		
		// 9-6. 끝 페이지가 전체 페이지 수보다 큰 경우, 끝 페이지 번호에 전체 페이지 수 저장.
		if(endPage > totalPageCount) {
			endPage = totalPageCount;
		}
		
		// 9-7. LIMIT 시작 위치 계산
		limitStart = (params.getPage() - 1) * params.getRecordPerPage();
		
		// 9-8. 이전 페이지 존재 여부 확인
		existPrevPage = startPage != 1;
		
		// 9-9. 다음 페이지 존재 여부 확인
		existNextPage = (endPage * params.getRecordPerPage()) < totalRecordCount;
	}
}
