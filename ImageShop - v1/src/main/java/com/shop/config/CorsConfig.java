package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {  // 1-1. CorsFilter 관리
		
		// 1-2. UrlBasedCorsConfigurationSource 객체 생성
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		// 1-3. Cors Config 파일 생성
		CorsConfiguration config = new CorsConfiguration();
		
		// 1-4. 내 서버가 응답할 때 json 데이터를 자바스크립트 영역에서 처리할 수 있게(받을 수 있게) 할지를 설정하는 것.
		config.setAllowCredentials(true);
		
		// 1-5. 모든 origin 허용 -> 모든 ip 응답을 허용.
		config.addAllowedOrigin("*");
		
		// 1-6. 모든 header 응답 허용
		config.addAllowedHeader("*");
		
		// 1-7. 모든 방식 허용(GET, POST, PUT, DELETE, PATCH)
		config.addAllowedMethod("*");
		
		source.registerCorsConfiguration("/api/**", config);
		
		return new CorsFilter(source);
		
	}
}
