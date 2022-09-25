package com.photogram.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("${file.path}")
	private String uploadFolder;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		registry
			.addResourceHandler("/upload/**")  // 1-1. /upload/** 패턴으로 요청이 오면
			.addResourceLocations("file:///" + uploadFolder)  // 1-2. 1-1 경로로 오면 application.properties에 등록된 file.path 경로로 바꿔준다.
			.setCachePeriod(60*10*6)   // 1-3. 60초 * 10 = 600초 = 10분, * 6을 하면 60분 = 1시간 
			.resourceChain(true)  // 1-4. true = 발동
			.addResolver(new PathResourceResolver()); // 1-5. 등록	
	}
}