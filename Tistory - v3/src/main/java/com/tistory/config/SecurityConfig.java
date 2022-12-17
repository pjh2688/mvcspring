package com.tistory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.tistory.handler.LoginSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity  // 1-1. 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig {
	// 1-2. WebSecurityConfigurerAdapter를 상속해서 AuthenticationManager를 bean으로 등록했던걸 직접 등록.
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	// 1-3. 기존 SecurityConfig에서 configure 메소드 기능을 한다. -> 기본 시큐리티 config파일을 작동시키지 않고 내가 만든 configuration을 적용한다는 말.(기본 security 로그인페이지 안뜸)
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.httpBasic().disable()  // 1-6. httpSecurity가 제공하는 기본인증 기능 disable
				.csrf().disable()  // 1-7. CSRF[Cross Site Request Forgery] 토큰 비활성화 -> 이걸 안꺼주면 회원가입이 안된다. 403 fobiden 뜸
				.cors()  // 1-8. cors[Cross Origin Resource Sharing] 허용 : 서로 다른 Origin끼리 요청을 주고 받을 수 있게 허용.
				.and()
				.headers().frameOptions().disable()  // 1-9. spring-security에서는 자체적으로 X-Frame-Options를 deny해놓기 때문에 이것을 disable 해놓는다.
				.and()
				.authorizeRequests()  // 1-10. 인증이 필요한 Request 정보
				.antMatchers("/admin/**", "/user/**").authenticated()  // 1-11. 해당 url은 인증이 필요하다.
                .anyRequest().permitAll()  // 1-12. 1-11가 아닌 url은 모두 허용한다.
                .and()
                .formLogin()  // 1-13. 
    			.loginPage("/login-form")  // 1-14. 사용자가 1-9의 URL로 요청하면(GET 방식) /auth/signin으로 리다이렉트.
    			.loginProcessingUrl("/login")  // 1-15. URL로 요청(POST 방식) -> 스프링 시큐리티가 구현한 UserDetailsService가 낚아채서 로그인 프로세스 진행
    			.successHandler(new LoginSuccessHandler()) // 16. AuthenticationSuccessHandler를 implements한 LoginSuccessHandler를 만들어 대신 처리하게 위임(함수 재정의(오버라이딩))
    			.and()
    			.build();
	}
	
	// 1-4. 비밀번호 해시 
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	// 1-5. 정적 파일 인증 무시.(2.7.0이상 부터는 이런 방식으로 설정. -> 임시로 bean으로 등록은 안해놓음)
//  @Bean	
	public WebSecurityCustomizer webSecurityCustomizer() {
		WebSecurityCustomizer web = new WebSecurityCustomizer() {
				
			@Override
			public void customize(WebSecurity web) {
				web.ignoring().antMatchers("/tistory/css/**", "/tistory/image/**", "/tistory/js/**");
			}
						
		};
						
		return web;
	}
}