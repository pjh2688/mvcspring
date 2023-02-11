package com.shop.config.jwt.filter;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.shop.config.auth.PrincipalDetails;
import com.shop.config.jwt.JwtProperties;
import com.shop.config.jwt.service.JwtService;
import com.shop.domain.member.Member;
import com.shop.domain.member.MemberAuth;
import com.shop.mapper.member.IMemberMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

/*  
 *  -> 스프링 시큐리티가 filter를 여러개 가지고 있는데 그 필터중에 BasicAuthenticationFilter라는 필터가 있다.
 *  -> SecurityConfig에서 설정해놓은 권한이나 인증이 필요한 특정 주소를 요청했을 때에는 BasicAuthenticationFilter를 무조건 타게 되어 있다.
 *  -> 만약에 SecurityConfig에 권한이나 인증이 필요한 주소로 설정이 안되어 있다면 이 필터를 타지 않는다.    
*/
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	
	// 주의 : secretKey 값에 특수문자가 들어가면 안된다.
	private static String secretKey = JwtProperties.SECRET_KEY;
	
	private IMemberMapper memberMapper;
	
	private JwtService jwtService;
	
	byte[] secretKeyBytes = secretKey.getBytes();

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, IMemberMapper memberMapper, JwtService jwtService) {
		super(authenticationManager);
		this.memberMapper = memberMapper;
		this.jwtService = jwtService;
	}
	
	// 1-1. 인증이나 권한이 필요한 주소(request) 요청이 있을 때 타는 필터.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			
			System.out.println("인증이나 권한이 필요한 주소 요청이 왔음.");
			
			// 1-2. Authorization header 값 확인(Authorization 필드에는 한글 X)
			String jwtHeader = request.getHeader("Authorization");
			
//			System.out.println("jwtHeader : " + jwtHeader);
			
			// 1-3. JWT 토큰을 검증해서 header 값이  있는지 확인하고 있다면 Bearer로 시작하는지까지 체킹한다.
			if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
				// 1-4. 1-3이라면 필터를 계속 타게 한다.
				chain.doFilter(request, response);
				return;
			}
			
			String access_token = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
			
			System.out.println("doFilter access_token : " + access_token);
		
			System.out.println("access_token : " + access_token);
			
			String userId = Jwts.parser().setSigningKey(secretKeyBytes).parseClaimsJws(access_token).getBody().get("userId", String.class);
			
			// 1-6. 서명이 정상적으로 되었다면 userId가 null이 아님
			if(userId != null) {
				// 1-7. userId가 null이 아니라면 DB에서 찾아본다.(Optional)
				Optional<Member> memberOp = memberMapper.findByUserId(userId);
				
				System.out.println("================= 서명 정상적으로 됨 ==================");
				
				if(memberOp.isPresent()) {  // 1-8. 해당 멤버가 존재하면
					Member findMember = memberOp.get();  // 1-9. get
					
					// 1-10. 찾아온 Member객체를 PrincipalDetails로 감싼다.
					PrincipalDetails principalDetails = new PrincipalDetails(findMember);
					
					System.out.println("username : " + principalDetails.getUsername());
					
					// 1-14. 현재 권한을 저장하고 있는 테이블이 따로 있는 관계로 권한정보 목록을 userId로 조회한 회원정보에 들어 있는 userNo으로 가져온다.
					List<MemberAuth> authList = memberMapper.findMemberAuthByUserNo(findMember.getUserNo());
					
					// 1-15. 스프링 시큐리티에서 받아서 사용할 수 있게 Collection<GrantedAuthority> 형인 authorities 선언
					Collection<GrantedAuthority> authorities = new ArrayList<>();
					
					// 1-16. 우리가 저장한 권한 정보 List를 forEach문으로 반복해서
					authList.forEach(role -> {
						// 1-17. authorities에 GrantedAuthority 형태로 권한 이름을 저장한다.
						authorities.add(new GrantedAuthority() {
							
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							@Override
							public String getAuthority() {
								
								return role.getAuth();	
							}
						});
					});
					
					System.out.println("=============== 저장된 권한 출력해보기 =================");

					// 1-18. 저장된 권한 출력해 보기.
					authorities.forEach(role -> {
						System.out.println(role.getAuthority());
					});
					
					// 1-19. 토큰이 null인지 유효한지 한번 검증
					if(access_token != null && jwtService.validationToken(access_token)) {
						// 1-20. JWT 토큰 서명을 통해서 서명이 정상일때 만들어지는 가짜 인증(Authentication) 객체  -> 두번째 인자 값(패스워드)는 null => username값이 null이 아니기 때문에 가능.
						Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, authorities);
					
						// 1-21. SecurityContextHolder.getContext() -> 세션 공간, 세션 공간에 1-11에서 가져온 가짜 인증 객체를 넣어준다.
						SecurityContextHolder.getContext().setAuthentication(authentication);
						
					}
					
					String db_refresh_token = findMember.getRefreshToken();
					
					System.out.println("DB에서 가져온 refresh_token : " + db_refresh_token);
					
					// 1-23. access_token이 만료되었거나 만료되기 1일전이라면 -> (거의 모든 요청마다 access_token은 재발급) 
					if(jwtService.isNeedToUpdateAccessToken(access_token)) {
						log.info("엑세스 토큰 재발급하기");
						// 1-24. access 토큰 재발급
						System.out.println("엑세스 토큰 재발급 해야된다.");
							
						// 1-25. JWT 토큰 만들기 1 : Header값 생성
						Map<String, Object> headers = new HashMap<>();
						headers.put("typ", "JWT");
						headers.put("alg", "HS256");
							
						// 1-26. JWT 토큰 만들기 2 : claims 부분 설정(토큰 안에 담을 내용)
						Map<String, Object> claims = new HashMap<>();
						claims.put("userId", principalDetails.getMember().getUserId());
						claims.put("userName", principalDetails.getMember().getUserName());
							
						// 1-27. JWT 토큰 만들기 3 : 만료 시간 설정(Access token)
						Long expiredTime = 1000 * 60L * 60L * 1;

						Date date = new Date();
						date.setTime(date.getTime() + expiredTime);
							
						System.out.println("access_token 만료일자 : " + date);
							
						// 1-28. JWT 토큰 만들기 4 : hmacSha 형식 key 만들기 
						Key key = Keys.hmacShaKeyFor(secretKeyBytes);
						
						// 1-29. JWT 토큰 Builder : access_token
						access_token = Jwts.builder()
								.setHeader(headers) 
								.setClaims(claims)
								.setSubject("access_token by jhpark")
								.setExpiration(date)
								.signWith(key, SignatureAlgorithm.HS256)
								.compact();
							
						System.out.println("access_token = " + access_token);
							
						// 1-30. header에 새로운 access_token 장착
						response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + access_token);
							
						// 1-31. 1-29에서 만든 새로운 access_token로 쿠키 생성 하고 편집 못하게 암호화.
						Cookie cookie = new Cookie("access_token", access_token);
						cookie.setSecure(true);
							
						// 1-32. 응답 객체에 새로 생성된 쿠키 장착.
						response.addCookie(cookie);
					}
						
					// 1-22. 다시 체인을 타게한다.
					chain.doFilter(request, response);
				}
					
				// 2023-01-31 -> JWT 인증 구현 어느정도 완성됨
				// 참고 : https://tansfil.tistory.com/59
					
			}
		// 1-33. 전체를 try-catch로 감싼다음 예외처리
		} catch (SignatureException | MalformedJwtException e) {
            log.error("잘못된 Jwt 서명입니다.");
            jwtService.sendErrorResponse(response, "잘못된 jwt 서명입니다.");
          
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다. 로그인을 다시 해주세요.");
            jwtService.sendErrorResponse(response, "만료된 토큰입니다. 로그인을 다시 해주세요.");

        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰입니다.");
            jwtService.sendErrorResponse(response, "지원하지 않는 토큰입니다.");
           
        } catch (IllegalArgumentException e) {
            log.error("잘못된 토큰입니다.");
            jwtService.sendErrorResponse(response, "잘못된 토큰입니다.");
          
        }
		
	} 
		
//	super.doFilterInternal(request, response, chain);
}
	
// https://ws-pace.tistory.com/254 -> 2023-01-24