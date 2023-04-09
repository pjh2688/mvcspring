package com.shop.service.member;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.domain.codelabel.CodeLabelValue;
import com.shop.domain.member.Member;
import com.shop.domain.member.MemberAuth;
import com.shop.handler.exception.CustomApiException;
import com.shop.mapper.member.IMemberMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
	
	private final IMemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	private final MessageSource messageSource;
	
	public int setupAdmin(Member member) {
		
		if(memberMapper.countAll() == 0) {
			String raw_password = member.getUserPw();
			
			member.setUserPw(passwordEncoder.encode(raw_password));

			int result = memberMapper.save(member);
			
			Member findMember = memberMapper.findByUserId(member.getUserId()).get();
			
			MemberAuth memberAuth = new MemberAuth();
			
			memberAuth.setUserNo(findMember.getUserNo());
			memberAuth.setAuth("ROLE_ADMIN");
			
			memberMapper.createAuth(memberAuth);
			
			memberAuth.setUserNo(findMember.getUserNo());
			memberAuth.setAuth("ROLE_MEMBER");
						
			memberMapper.createAuth(memberAuth);
			
			memberAuth.setUserNo(findMember.getUserNo());
			memberAuth.setAuth("ROLE_USER");
			
			memberMapper.createAuth(memberAuth);
			
			return result;
			
		} else {
			
			String message = messageSource.getMessage("common.cannotSetupAdmin", null, Locale.KOREAN);
			
			throw new CustomApiException(message);
		}
	}
	
	@Transactional(readOnly = true)
	public List<CodeLabelValue> getCodeGroupList(String groupCode) {
		
		List<CodeLabelValue> result = null;
		
		try {
			result = memberMapper.getCodeLabelValueList(groupCode);
			
		} catch (Exception e) {
			throw new CustomApiException("코드 라벨과 코드 값 가져오기 실패");
		}
		
		return result;
	}
	
	// 2023-01-03 -> 2340페이지
	public int register(Member member) {
		Optional<Member> memberOp = memberMapper.findByUserId(member.getUserId());
		
		if(!memberOp.isPresent()) {
			String raw_password = member.getUserPw();
			
			member.setUserPw(passwordEncoder.encode(raw_password));
			
			int result = memberMapper.save(member);
			
			Member findMember = memberMapper.findByUserId(member.getUserId()).get();
			
			// 1-1. 가입시에는 기본적으로 ROLE_MEMBER와 ROLE_USER 권한을 부여.
			MemberAuth memberAuth = new MemberAuth();
			
			memberAuth.setUserNo(findMember.getUserNo());
			memberAuth.setAuth("ROLE_MEMBER");
						
			memberMapper.createAuth(memberAuth);
			
			memberAuth.setUserNo(findMember.getUserNo());
			memberAuth.setAuth("ROLE_USER");
			
			memberMapper.createAuth(memberAuth);
			
			return result;
			
		} else {
			throw new CustomApiException("아이디가 이미 존재합니다.");
		}
	}
	
	@Transactional(readOnly = true)
	public List<Member> findAll() {
		
		List<Member> result = null;
		
		try {
			result = memberMapper.findAll();
			
		} catch (Exception e) {
			throw new CustomApiException("멤버 리스트 정보가 존재하지 않습니다.");
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public Member findByUserNo(Long userNo) {
		
		Optional<Member> memberOp = memberMapper.findByUserNo(userNo);
		
		if(memberOp.isPresent()) {
			return memberOp.get();
			
		} else {
			throw new CustomApiException("해당 유저가 존재하지 않습니다.");
		}
		
	}
	
	public int update(Member member) {
		Optional<Member> memberOp = memberMapper.findByUserId(member.getUserId());
		
		if(memberOp.isPresent()) {
			int result = memberMapper.update(member);
			
			updateAuth(member);
		
			return result;
			
		} else {
			throw new CustomApiException("해당 유저가 존재하지 않습니다.");
		}	
	}
	
	private void updateAuth(Member member) {
		
		Long userNo = member.getUserNo();
		
		memberMapper.deleteAuth(userNo);
		
		List<MemberAuth> authList = member.getAuthList();
		
		for(int i = 0; i < authList.size(); i++) {
			MemberAuth memberAuth = authList.get(i);
			
			String auth = memberAuth.getAuth();
			
			if(auth == null) {
				continue;
			}
			
			if(auth.trim().length() == 0) {
				continue;
			}
			
			memberAuth.setUserNo(userNo);
			
			memberMapper.createAuth(memberAuth);
		}
	}
	
	// 2023-01-04 -> MEMBER CRUD 완료.
	public int delete(Long userNo) {
		Optional<Member> memberOp = memberMapper.findByUserNo(userNo);
		
		if(memberOp.isPresent()) {
			
			memberMapper.deleteAuth(userNo);
			
			int result = memberMapper.delete(userNo);
			
			return result;
			
		} else {
			throw new CustomApiException("해당 유저가 존재하지 않습니다.");
		}	
	}
	
	public int getCoin(Long userNo) {
		Optional<Member> memberOp = memberMapper.findByUserNo(userNo);
		
		if(memberOp.isPresent()) {
			Member findMember = memberOp.get();
			
			return findMember.getCoin();
			
		} else {
			throw new CustomApiException("해당 유저가 존재하지 않습니다.");
		}	
	}
}
