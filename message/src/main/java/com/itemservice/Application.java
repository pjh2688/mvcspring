package com.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.MessageSource;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	// 1. 스프링부트에서는 자동으로 등록해준다.(부트에서는 굳이 명시 안해줘도 된다는 말)
	/*
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("messages", "errors");  // 2. messages.properties , errors.properties 파일을 읽어온다.
		messageSource.setDefaultEncoding("utf-8");
		return messageSource;
	}
	*/

}
