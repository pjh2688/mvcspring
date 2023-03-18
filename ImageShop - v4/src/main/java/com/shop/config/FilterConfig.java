package com.shop.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shop.filter.MyFilter1;
import com.shop.filter.MyFilter2;

@Configuration  // 1-1. IOC 등록
public class FilterConfig {  // 1-2. 필터를 SecurityConfig에서 addFilter에 걸지 않고 걸 수 있는 다른 방식.

//	@Bean
	public FilterRegistrationBean<MyFilter1> filter01() {  // 1-2. 필터는 Request가 발생할 때마다 실행된다. 	
		// 1-3. 내가 만든 MyFilter1 을 FilterRegistrationBean 형태로 만든다.
		FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<MyFilter1>(new MyFilter1());
		bean.addUrlPatterns("*");  // 1-4. 모든 URL 패턴 허용.
		bean.setOrder(1);  // 1-5. 순서는 숫자가 낮을 수록 제일 먼저 실행.
		
		return bean;
	}
	
//	@Bean
	public FilterRegistrationBean<MyFilter2> filter02() {  // 1-2. 	
		// 1-3. 내가 만든 MyFilter1 을 FilterRegistrationBean 형태로 만든다.
		FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<MyFilter2>(new MyFilter2());
		bean.addUrlPatterns("*");  // 1-4. 모든 URL 패턴 허용.
		bean.setOrder(0);  // 1-5. 순서는 숫자가 낮을 수록 제일 먼저 실행.
		
		return bean;
	}
}
