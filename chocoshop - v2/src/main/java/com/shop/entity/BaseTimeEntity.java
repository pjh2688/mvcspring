package com.shop.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@EntityListeners(value = {AuditingEntityListener.class})  // 1-4. Auditing을 적용하기 위해서 명시
@MappedSuperclass  // 1-5. 공통 매핑 정보가 필요할 때 상용하는 어노테이션으로 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공합니다.
@Getter @Setter
public abstract class BaseTimeEntity {  // 1-3. 추상 클래스로 선언, 추상 클래스를 사용 하는 이유 : 공통된 내용(필드나 메소드)들을 추출하여 통일된 내용으로 작성하도록 규격화시키기 위해서

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime regTime;  // 1-1. 엔티티가 생성되어 저장될 때 시간을 자동으로 저장.
	
	@LastModifiedDate
	private LocalDateTime updateTime;  // 1-2. 엔티티의 값이 변경될 때 시간을 자동으로 저장.
}
