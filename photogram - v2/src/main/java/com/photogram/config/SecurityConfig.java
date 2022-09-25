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
@EnableWebSecurity  // 1-3. 해당 파일로 Security를 활성한다는 의미.
@Configuration  // 1-2. IoC에등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {  // 1-1. Spring Security를 구현하기 위해 WebSecurityConfigurerAdapter 상속

	private final OAuth2DetailsService oAuth2DetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		super.configure(http);  // 1-4. super.configure(http); -> 이게 default로 실행되고 있기 때문에 계속 Spring Security가 가지고 있는 기본 로그인 페이지가 뜨게 된다. -> 주석 처리, 기존 시큐리티 기능이 비활성화 된다.
		
		http.authorizeRequests()
			.antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**").authenticated()  // 1-5. 해당 URL로 매핑되는건 인증이 필요하고
			.anyRequest().permitAll()  // 1-6. 그외의 모든 요청은 허용하겠다. 
			.and()  
			.formLogin()
			.loginPage("/auth/signin")  // 1-7. 사용자가 1-5의 URL로 요청하면(GET 방식) /auth/signin으로 리다이렉트.
			.loginProcessingUrl("/auth/signin")  // 1-8. URL로 요청(POST 방식) -> 스프링 시큐리티가 구현한 UserDetailsService가 낚아채서 로그인 프로세스 진행
			.defaultSuccessUrl("/")  // 1-9. 1-8이 정상적으로 처리가 되었으면 /로 이동.
			.and()
			.oauth2Login()  // 1-11. oauth2 로그인도 허용.
			.userInfoEndpoint()  // 1-2. oauth2 로그인을 하면 최종 응답으로 회원정보를 바로 받을 수 있다.
			.userService(oAuth2DetailsService);
		
		
		http.csrf().disable();  // 1-10. CSRF[Cross Site Request Forgery] 토큰 비활성화
								// -> 여기다 안적고 form태그 안에다가  넣어줘도 된다. 
								// -> <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"> 
		
	}
	
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
}
