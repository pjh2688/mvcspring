package com.shop.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String userId = "";
		
		if(authentication != null) {
			userId = authentication.getName();  // 1-1. 스프링 시큐리티에서 관리되고 있는 현재 로그인한 사용자의 정보를 조회하여 사용자의 이름을 등록자와 수정자로 지정합니다.
		}
		
		return Optional.of(userId);
	}

}

/*
 *   - Spring data JPA Auditing
 *   Audit는 감독하다, 검사하다라는 뜻으로, 해당 데이터를 보고 있다가 생성(Create) 또는 수정(Update)가 발생하면 자동으로 값을 넣어주는 편리한 기능. 
 * 
 */
 