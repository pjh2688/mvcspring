package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.shop.config.jwt.filter.JwtAuthenticationFilter;
import com.shop.config.jwt.filter.JwtAuthorizationFilter;
import com.shop.config.jwt.service.JwtService;
import com.shop.handler.CustomLogoutHandler;
// import com.shop.filter.MyFilter3;
import com.shop.mapper.member.IMemberMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity  // 1-1. 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CorsConfig corsConfig;
	
	private AuthenticationConfiguration configuration; 
	
	private final IMemberMapper memberMapper;
	
	private final JwtService jwtService;
	
	// 1-2. WebSecurityConfigurerAdapter를 상속해서 AuthenticationManager를 bean으로 등록했던걸 직접 등록.
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		this.configuration = configuration;  // 1-22. authenticationManager로 전달되는 AuthenticationConfiguration을 셋팅.
		return configuration.getAuthenticationManager();
	}
	
	// 1-3. 기존 SecurityConfig에서 configure 메소드 기능을 한다. -> 기본 시큐리티 config파일을 작동시키지 않고 내가 만든 configuration을 적용한다는 말.(기본 security 로그인페이지 안뜸)
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf().disable()  // 1-6. CSRF[Cross Site Request Forgery] 토큰 비활성화 -> 이걸 안꺼주면 회원가입(form submit)이 안된다. 403 fobiden 뜸
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 1-7. jSessionId를 관리하지 않기 때문에, 세션 응답이 종료되면 사라진다는 뜻.(무상태성 설정)
				.and()
				.formLogin().disable()  // 1-8. 폼 로그인 방식을 사용하지 않는다고 선언
				.httpBasic().disable()  // 1-9. httpSecurity가 제공하는 기본인증 기능 disable
				.authorizeRequests()  // 1-10. 인증 Request를 정의
				.antMatchers("/api/user/**")  // 1-11. /api/user/** 형식으로 주소가 들어오면
				.access("hasRole('ROLE_USER') or hasRole('ROLE_MEMBER') or hasRole('ROLE_ADMIN')") // 1-12. ROLE_USER, ROLE_MEMBER, ROLE_ADMIN 허용
				.antMatchers("/api/manage/**")  // 1-13. /api/manage/** 형식으로 주소가 들어오면
				.access("hasRole('ROLE_MEMBER') or hasRole('ROLE_ADMIN')")  // 1-14.ROLE_MEMBER, ROLE_ADMIN 허용
				.antMatchers("/api/admin/**", "/api/codegroups/**")  // 1-15. /api/admin/**, /api/codegroups/** 형식으로 주소가 들어오면
				.access("hasRole('ROLE_ADMIN')")  // 1-16. ROLE_ADMIN만 허용
				.antMatchers("/api/users/**")
				.access("hasRole('ROLE_MEMBER') or hasRole('ROLE_ADMIN')")
				.anyRequest() // 1-17. 그 외의 요청들은
				.permitAll()  // 1-18. 모두 허용.
				.and()
				.addFilter(corsConfig.corsFilter())  // 1-19. @CrossOrign 정책을 안쓰고 커스텀 cors 설정해서 사용할거고 선언
//				.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class)  // 1-20. SecurityFilter가 아니라 같이 등록이 안된다. 그래서 SecurityFiltler 실행 Before or After에 걸어줘야 한다.(참고:SecurityFilterChain이 다른 필터보다 대부분 우선한다.)
				.addFilterAt(new JwtAuthenticationFilter(authenticationManager(configuration), jwtService), UsernamePasswordAuthenticationFilter.class) // 1-21. 폼로그인을 사용하지 않기 때문에 PricipalDetailsService를 따로 처리하는 Filter를 하나 만들어서 등록한다. 매개변수로는 AuthenticationManager를 넘겨준다.
				.addFilterBefore(new JwtAuthorizationFilter(authenticationManager(configuration), memberMapper, jwtService), UsernamePasswordAuthenticationFilter.class)  // 1-22. 권한 관리 필터 등록. -> SecurityFilterChain 앞에 addFilterBefore로 필터를 등록.
				// 2023-02-02 -> jwt 로그아웃 구현은 되었는데 로그인이 안되어 있을때 로그아웃 예외 처리도 해줘야한다.
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.addLogoutHandler(new CustomLogoutHandler(jwtService))
				.logoutSuccessUrl("/home")
				.and()
				.build();
	} 
	
	// 1-4. 비밀번호 해시  -> 충돌 방지를 위해 SecurityConfig에서 위치 변경(일단 해보고 변경)
	@Bean
	public BCryptPasswordEncoder encode() throws Exception {
		return new BCryptPasswordEncoder();
	}
	
	// 1-5. 정적 파일 인증 무시.(2.7.0이상 부터는 이런 방식으로 설정. -> 임시로 bean으로 등록은 안해놓음)
//  @Bean	
	public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
		WebSecurityCustomizer web = new WebSecurityCustomizer() {
				
			@Override
			public void customize(WebSecurity web) {
				web.ignoring().antMatchers("/tistory/css/**", "/tistory/image/**", "/tistory/js/**");
			}
						
		};
						
		return web;
	}
}


/*
 	* RSA(Rivest Shamir Adleman) 암호화
 	 (1) RSA방식에서 사용되는 키의 종류
 	 - PUBLIC KEY(공개 키)
 	 - PRIVATE KEY(개인 키)
 	 
 	 (2) 키 암호화 방식에는 2가지 방식이 있다.
 	 - 키 하나로 암호화 : 시멘트릭 키(Symmetric key)
 	 - 공개 키와 개인 키로 암호화 : 키페어(Key pair)
 	 
 	 (3) A의 공개키로 잠구면 -> A의 개인키로만 열어서 수정할 수 있다.(암호화)
 	 
 	 (4) A의 개인키로 잠구면 -> A의 공개키로 열어 볼  수 있다.(전자서명)
 	 
 	 (5) A와 B사이에 데이터를 보낼 때 암호화를 하려면 B의 공개키로 잠궈서 보내야 된다.
 	 
 	 (6) 중간에 해커가 A가 보낸 데이터를 가로채서 위조해 보낼 수 있으므로 B의 개인키로 한 번 더 잠군다.
 	 
 	 (7) B는 문서를 받으면 A의 공개키로 한 번 열어본다. 열리면 -> 인증(o), 안열리면 -> 인증(x)
 	 
 	 (8) 열리면 A의 개인키로 한번 더 열어서 데이터를 확인.
 	 
 	* RFC 1945 란?
 	 - 약속된 규칙이 정리된 문서(프로토콜)의 하나. -> 인터넷이란 이 RFC문서들의 모임. = http(HyperText Transfer Protocol) 프로토콜.
 	 
 	*JWT(Json Web Token) : 기존의 쿠키 세션 방식과 다르게 http headers 영역에 Authorization이라는 Key 값에 인증정보를 담아서 가는 방식이 있다.
 	(1) HTTP basic 방식 : http headers 영역에 Authorization이라는 키값에다가 ID, PW 정보를 담아서 인증을 하는 방법.
 	(2) HTTP bearer 방식 : http header 영역에 Authorization이라는 키 값에 ID, PW값을 토큰으로 암호화하여 바꿔서 보내서 인증을 하는 방법.
 	
 */
