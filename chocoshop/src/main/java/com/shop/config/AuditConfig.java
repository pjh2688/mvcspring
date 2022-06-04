package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration  
@EnableJpaAuditing  // 1-1. JPA의 Auditing 기능을 활성화
public class AuditConfig {

	@Bean
	public AuditorAware<String> auditorProvider() {  // 1-2.사용자의 이름을 등록자와 수정자로 지정해주는 AuditorAware을 빈으로 등록.
		return new AuditorAwareImpl();
	}
	
}
