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
@Transactional
@RequiredArgsConstructor
public class SubscribeService {

	private final SubscribeRepository subscribeRepository;
	private final EntityManager em;
	
	public void mSubscribe(Long fromUserId, Long toUserId) {
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);  // 1-1. 메소드 앞에 m을 붙은건 my의 약어
		} catch (Exception e) {
			throw new CustomApiException("없는 회원이거나 이미 구독한 회원입니다.");
		}
	}
	
	public void mUnSubscribe(Long fromUserId, Long toUserId) {
		subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> subscribeList(Long principalId, Long pageUserId) {
		
		StringBuffer sb = new StringBuffer();
	
		// 1-1. 쿼리 준비(주의 : 세미콜론 들어가면 안된다.) 
		sb.append("SELECT ");
		sb.append(	"u.id, u.username, u.profileImageUrl, ");
		sb.append(	"if((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id), 1, 0) AS subscribeState, ");
		sb.append(	"if((?=u.id), 1, 0) as equalUserState ");
		sb.append("FROM ");
		sb.append(	"user u INNER JOIN subscribe s ON u.id = s.toUserId ");
		sb.append("WHERE ");
		sb.append(	"s.fromUserId = ?");
		
		// 1-5. 첫번째 물음표 : principalId 
		// 1-6. 두번째 물음표 : principalId
		// 1-7. 마지막 물음표 : pageUserId
		
		// 1-2. 쿼리 완성
		Query query = em.createNativeQuery(sb.toString())
						.setParameter(1, principalId)
						.setParameter(2, principalId)
						.setParameter(3, pageUserId);
		
		// 1-3. 쿼리 실행(qlrm 라이브러리 이용, dto에 DB 결과값을 매핑시키기 위해서)
		JpaResultMapper result = new JpaResultMapper();
		
		// 1-4. list로 추출
		List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class);
		
		return subscribeDtos;
	}
}
