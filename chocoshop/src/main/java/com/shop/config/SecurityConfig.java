package com.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.shop.service.member.MemberService;

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
	
	@Autowired
	MemberService memberService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin()
				.loginPage("/members/login")
				.defaultSuccessUrl("/")
				.usernameParameter("email")
				.failureUrl("/members/login/error")
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
				.logoutSuccessUrl("/");
		
		// 1-1. 시큐리티 처리에 HttpServletRequest를 이용한다는 것을 의미.
		http.authorizeRequests()
				.mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()  // 1-2. permitAll : 인증 없이 모든 사용자가 접근 가능
				.mvcMatchers("/admin/**").hasRole("ADMIN")  // 1-3. /admin으로 시작하는 url은 ADMIN Role만 접근 가능.
				.anyRequest().authenticated();  // 1-4. 1-1, 1-2, 1-3을 제외한 모든 요청에 인증을 요구하겠다는 의미.
		
		http.exceptionHandling()  
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint());  // 1-5. 인증되지 않은 사용자가 리소스에 접근하였을때 실행되는 핸들러를 등록.
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// 1-6. static 하위에 있는 파일들은 인증을 무시.
		web.ignoring().antMatchers("/assets/css/**", "/assets/js/**", "/assets/img/**", "/assets/fonts/**", "/assets/plugin/**", "/assets/scripts/**");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
	}
	
	
}

/**
 *  참고 : SecurityConfig 파일을 만들고  WebSecurityConfigurerAdapter를 상속받아서 
 *        configure 메소드를 오버라이드하고 어떤 설정도 추가하지 않으면 요청에 인증을 요구하지 않는다.
 */
 