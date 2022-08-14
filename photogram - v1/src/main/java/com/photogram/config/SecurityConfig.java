package com.photogram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.photogram.config.oauth.OAuth2DetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity  // 3. 해당 파일로 시큐리티를 활성화
@Configuration  // 2. IoC 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {  // 1. WebSecurityConfigurerAdapter 상속

	private final OAuth2DetailsService oAuth2DetailsService;
	
	// 7. 패스워드 인코딩
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 4. super.configure(http); -> 삭제.
		
		// 5. 
		http.authorizeHttpRequests()
			.antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**").authenticated()  // 6. antMatchers에 등록된 url은 인증이 필요하다는 의미.
			.anyRequest().permitAll()  // 7. 그외의 요청은 모두 허용하겠다는 의미.
			.and()  // 8. 인증이 필요한 페이지로 요청이오면
			.formLogin() // 9.
			.loginPage("/auth/signin") // 10. GET : /auth/signin으로 리다이렉트.
			.loginProcessingUrl("/auth/signin")  // 12. POST -> 스프링 시큐리티기 로그인 프로세스 진행.
			.defaultSuccessUrl("/") // 11. 로그인에 성공하면 /로 이동.
			.and()
			.oauth2Login()  // 12. 폼로그인 말고도 oauth2로그인도 허용한다.
			.userInfoEndpoint()
			.userService(oAuth2DetailsService);
			
		// 6. CSRF 비활성화
		http.csrf().disable();
	}
}

/*
 *	
 	*CSRF(Cross Site Request Forgery)
 	 -> 웹 어플리케이션 취약점 중 하나로 인터넷 사용자(희생자)가 자신의 의지와는 무관하게 
 	         공격자가 의도한 행위(수정, 삭제, 등록 등)를 특정 웹사이트에 요청하게 만드는 공격입니다.
	
	 -> 스프링시큐리티 의존성을 추가하면 CSRF를 막는 기능이 기본으로 활성화된다.

 **/
