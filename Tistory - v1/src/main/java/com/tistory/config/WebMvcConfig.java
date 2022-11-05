package com.tistory.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Value("${thumnail.path}")
	private String thumnailFolder;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry
			.addResourceHandler("/upload/**")  // 1-1. /upload/** 패턴으로 요청이 오면
			.addResourceLocations("file:///" + uploadFolder)  // 1-2. 1-1 경로로 오면 application.properties에 등록된 file.path 경로로 바꿔준다.
			.setCachePeriod(60*10*6)   // 1-3. 60초 * 10 = 600초 = 10분, * 6을 하면 60분 = 1시간 
			.resourceChain(true)  // 1-4. true = 발동
			.addResolver(new PathResourceResolver()); // 1-5. 등록	
		
		registry
			.addResourceHandler("/thumnail/**")  // 1-1. /thumnail/** 패턴으로 요청이 오면
			.addResourceLocations("file:///" + thumnailFolder)  // 1-2. 1-1 경로로 오면 application.properties에 등록된 file.path 경로로 바꿔준다.
			.setCachePeriod(60*10*6)   // 1-3. 60초 * 10 = 600초 = 10분, * 6을 하면 60분 = 1시간 
			.resourceChain(true)  // 1-4. true = 발동
			.addResolver(new PathResourceResolver()); // 1-5. 등록
		
		WebMvcConfigurer.super.addResourceHandlers(registry);
	
	}
}
/* 
 * 참고 : 파일 프로토콜은 file:/// -> /// => 짝대기 3개 사용
 */