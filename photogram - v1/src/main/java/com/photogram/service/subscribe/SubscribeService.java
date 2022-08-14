package com.photogram.service.subscribe;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.photogram.domain.subscribe.SubscribeRepository;
import com.photogram.dto.subscribe.SubscribeDto;
import com.photogram.handler.exception.CustomApiException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscribeService {
	
	private final SubscribeRepository subscribeRepository;
	
	// 1-1. 직접 구현을 위해 entityManager 객체 생성
	private final EntityManager em;
	
	@Transactional(readOnly = true)
	public int subscribe(Long fromUserId, Long toUserId) {
		int result = 0;
		
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);
		} catch (Exception e) {
			throw new CustomApiException("이미 구독하고 있는 유저입니다.");
		}
		
		return result;
	}
	
	@Transactional
	public int unSubscribe(Long fromUserId, Long toUserId) {
		int result = subscribeRepository.mUnSubscribe(fromUserId, toUserId);
		return result;
	}
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> getSubscribeList(Long principalId, Long pageUserId) {
		// 1-2. 쿼리를 만들기 위해 StringBuffer 객체 생성.
		StringBuffer sb = new StringBuffer();
		
		// 1-3. 쿼리 준비하기
		sb.append("SELECT u.id, u.username, u.profileImageUrl, ");
		sb.append("if((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id), 1, 0) AS subscribeState, ");
		sb.append("if((?=u.id), 1, 0) as equalUserState ");
		sb.append("FROM user u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.toUserId ");
		sb.append("WHERE s.fromUserId = ?"); // 1-4. 세미콜론은 빼야된다.
		
		// 1-4. 두번째 줄 물음표 : principalId(로그인한 아이디) 
		// 1-5. 세번째 줄 물음표 : principalId(로그인한 아이디) 
		// 1-6. 마지막 물음표 : pageUserId(페이지의 주인 아이디)
		
		// 1-7. 쿼리 바인딩해서 완성하기
		Query query = em.createNativeQuery(sb.toString())
						.setParameter(1, principalId)
						.setParameter(2, principalId)
						.setParameter(3, pageUserId);
		
		// 1-8. 쿼리 실행하기(QLRM 라이브러리 필요) - 단 건 : uniqueResult, 여러 건 : list
		JpaResultMapper result = new JpaResultMapper();
		List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class);
		
		return subscribeDtos;
	}

}
