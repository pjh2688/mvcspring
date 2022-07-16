package com.shop.entity.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.shop.constant.member.Role;
import com.shop.dto.member.MemberFormDto;
import com.shop.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member")
@Getter @Setter
@ToString
public class Member extends BaseEntity {

	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(unique = true)  // 1-1. 회원은 이메일을 통해 유일하게 구분되어야 하기 때문에, 동일한 값이 데이터베이스에 들어올 수 없도록 유니크 제약조건을 걸어준다.
	private String email;
	
	private String password;
	
	private String address;
	
	@Enumerated(EnumType.STRING)  // 1-2. EnumType.STRING
	private Role role;
	
	// 1-3. 회원가입 창에서 넘어온 MemberFormDto 데이터를 Member Entity로 변환해 return(이때 비밀번호 암호화)
	public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
		
		Member member = new Member();
		member.setName(memberFormDto.getName());
		member.setEmail(memberFormDto.getEmail());
		member.setAddress(memberFormDto.getAddress());
		
		String password = passwordEncoder.encode(memberFormDto.getPassword());
		member.setPassword(password);
		member.setRole(Role.USER);
		
		return member;
	}
}
