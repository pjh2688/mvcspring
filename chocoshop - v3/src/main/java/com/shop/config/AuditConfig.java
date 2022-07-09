package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration 
@EnableJpaAuditing  // 1-1. JPA에서 Auditing기능을 활성화시킵니다.
public class AuditConfig {
	
	@Bean  // 1-2. 등록자와 수정자를 처리해주는 AuditorAwareImpl을 빈으로 등록합니다.
	public AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}
}
