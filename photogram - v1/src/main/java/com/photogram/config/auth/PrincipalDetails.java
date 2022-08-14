package com.photogram.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.photogram.domain.user.User;

import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private User user;
	
	private Map<String, Object> attributes;
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
	}

	// 2-1. Collection 타입인 이유 : 권한이 한개가 아닐 수 있음.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collector = new ArrayList<>();
		
	/*  // 2-2. 람다식 X
		collector.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				
				return user.getRole();
			}
		});
	*/
		
		// 2-3. 람다식 O
		collector.add(() -> {
			return user.getRole();
		});
		
		return collector;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// ======================================
	// 1. 여기는 일단 true로 바꿔준다. false면 로그인이 안됨.
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	// ======================================
	
	@Override
	public Map<String, Object> getAttributes() {
		// {id:129319391249, name: 박종희, email:pjh2688@naver.com}
		return attributes;
	}

	@Override
	public String getName() {
		return (String) attributes.get("name");
	}
}
