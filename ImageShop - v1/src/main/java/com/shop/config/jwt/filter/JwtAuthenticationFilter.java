package com.shop.config.jwt.filter;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

// import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.config.auth.PrincipalDetails;
import com.shop.config.jwt.JwtProperties;
import com.shop.config.jwt.service.JwtService;
import com.shop.web.dto.SignInDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 1-1. 스프링 시큐리티에 있는 UsernamePasswordAuthenticationFilter를 상속받는다. 이 필터는 Post방식으로 /login이라는 요청이 왔을때 동작한다.
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	// 주의 : secretKey 값에 특수문자가 들어가면 안된다.
	private static String secretKey = JwtProperties.SECRET_KEY;
	
	byte[] secretKeyBytes = secretKey.getBytes();
	
	// 1-2. 롬복으로 빈주입
	private final AuthenticationManager authenticationManager;
	
	private final JwtService jwtService;

	// 1-3. 기본적으로 /login 요청이 post로 왔을때 실행되는 함수. 
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		System.out.println("JwtAuthenticationFilter : 로그인 시도중");
		
		// 1-4. username, password를 전달 받아 수행
		try {
		/*	
		   // 1-5. 원시적인 방법 
			BufferedReader br = request.getReader();			
			String input = null;
			String jsonString = "";
			while((input = br.readLine()) != null) {
				System.out.println(input);
				jsonString += input;
			}
			
			System.out.println(jsonString);
		*/
			// 1-6. ObjectMapper 이용 -> JSON 데이터 파싱해주는 객체
			ObjectMapper om = new ObjectMapper();
			
			// 1-7. 먼저 username과 password 값을 받을 JoinReqDto를 생성해서 inputStream에서 SignInDto.class 형태로 받는다.
			SignInDto dto = om.readValue(request.getInputStream(), SignInDto.class);
			
			System.out.println(dto.getUsername() + ", " + dto.getPassword());
			
			// 1-8. 토큰 만들기
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
			
			// 1-9. 로그인 시도를 하면 authenticationManager를 통해 1-8에서 만든 토큰으로 authenticate(인증)을 수행하며 완료된 후  PrincipalDetailsService.loadUserByUsername 메소드가 호출이된다.
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			
			// 1-10. 1-5가 정상적으로 수행되면 PrincipalDetails를 세션에 저장된다(세션을 사용하는 이유는 권한관리 때문)
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
			
			// 1-11. 출력이 되면 로그인이 되었다는 뜻
			System.out.println("로그인 완료됨 : "  + principalDetails.getMember().getUserName());
		
			System.out.println("==================================================");
			
			// 1-12. 권한 관리를 위해 세션이 담긴 authentication 객체 return(인증 완료)
			return authentication;
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("====================================================");
		
		return null;
	}
	
	// 1-13. 인증이 정상적으로 완료되었으면 successfulAuthentication 메소드가 실행된다.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// 1-14. 인증이 완료되면 JWT 토큰을 만들어서 request를 요청한 사용자에게 JWT토큰을 return해주면 된다.
		System.out.println("successfulAuthentication 실행됨 => 이게 실행된다면 인증이 완료되었다는 의미.");
		
		// 1-15. 인증이 완료된 Authentication형 authResult 가져온다.
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		
		// 1-16. JWT 토큰 만들기 1 : Header값 생성
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");
		
		// 1-17. JWT 토큰 만들기 2 : claims 부분 설정(토큰 안에 담을 내용)
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", principalDetails.getMember().getUserId());
		claims.put("userName", principalDetails.getMember().getUserName());
		
		// 1-18. JWT 토큰 만들기 3 : 만료 시간 설정(Access token)
//		Long expiredTime = 1000 * 60L * 60L * 1;
		Long expiredTime = 30 * 60L * 60L * 1;
		
		Date date = new Date();
		date.setTime(date.getTime() + expiredTime);
		
		System.out.println("access_token 만료일자 : " + date);
		
		// 1-19. JWT 토큰 만들기 4 : hmacSha 형식 key 만들기 
		Key key = Keys.hmacShaKeyFor(secretKeyBytes);
	
		// 1-20. JWT 토큰 Builder : access_token
		String access_token = Jwts.builder()
				.setHeader(headers) 
				.setClaims(claims)
				.setSubject("access_token by jhpark")
				.setExpiration(date)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
		
		System.out.println("access_token = " + access_token);
		
		// 1-21. JWT 토큰 Builder : refresh token -> expiredTime을 24시간보다 약간 크게 설정함.
		expiredTime *= 23;
		expiredTime += 100000;
		date.setTime(System.currentTimeMillis() + expiredTime);
		
		String refresh_token = Jwts.builder()
				.setHeader(headers) 
				.setSubject("refresh_token by jhpark")
				.setExpiration(date)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
		
		System.out.println("refresh_token = " + refresh_token);
		
		// 1-22. refresh token 저장.
		jwtService.setRefreshToken(principalDetails.getMember().getUserId(), refresh_token);
		
		// 1-23. JWT 토큰 response header에 담음(주의 : Bearer 다음에 한칸 띄우고 저장해야함)
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + access_token);
		
		// 1-24. access_token 쿠키에 저장.
		Cookie cookie = new Cookie("access_token", access_token);
		cookie.setSecure(true);
	
		response.addCookie(cookie);
	
		log.info("successfulAuthentication 종료");
		
		// 1-25. 다 만들었으면 이부분 주석.
//		super.successfulAuthentication(request, response, chain, authResult);
	}
	
	// 2023-01-22 -> https://ws-pace.tistory.com/254?category=1025277

}
