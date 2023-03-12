package com.shop.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shop.domain.member.Member;
import com.shop.domain.member.MemberAuth;
import com.shop.mapper.member.IMemberMapper;

import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	public IMemberMapper mapper;
	
	private Member member;
	
	public PrincipalDetails(Member member) {
		this.member = member;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		
		MemberAuth memberAuth = new MemberAuth();
		
		member.getAuthList().forEach(role -> {
			memberAuth.setUserNo(member.getUserNo());
			memberAuth.setAuth(role.getAuth());
			
			mapper.createAuth(memberAuth);
			
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
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return member.getUserPw();
	}

	@Override
	public String getUsername() {
		return member.getUserName();
	}

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

}
