package com.shop.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import com.shop.entity.member.Member;
import com.shop.entity.member.MemberRepository;

@SpringBootTest
@Transactional
class MemberTest {

	@Autowired
	MemberRepository memberRepository;
	
	@PersistenceContext
	EntityManager em;
	
	@Test
	@DisplayName("Auditing 테스트")
	@WithMockUser(username = "gildong", roles = "USER")  // 1-1. gildong이란 유저가 로그인했다고 가정
	public void auditingTest() {
		Member newMember = new Member();
		memberRepository.save(newMember);
		
		em.flush();
		em.clear();
		
		Member member = memberRepository.findById(newMember.getId()).orElseThrow(EntityNotFoundException::new);
		
		System.out.println("register time : " + member.getRegTime());
		System.out.println("update time : " + member.getUpdateTime());
		System.out.println("create member : " + member.getCreatedBy());
		System.out.println("modify member : " + member.getModifiedBy());
	}

}
