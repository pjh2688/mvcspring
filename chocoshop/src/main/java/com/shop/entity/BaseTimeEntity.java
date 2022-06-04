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

@EntityListeners(value = {AuditingEntityListener.class}) // 1-1. Auditing을 적용하기 위해서 @EntityListeners 어노테이션 추가 
@MappedSuperclass  // 1-2. 공통 매핑 정보가 필요할 때 사용하는 어노테이션으로 부모 클래스를 상속 받는 자식 클래스에게 매핑 정보만 제공한다.
@Getter @Setter
public abstract class BaseTimeEntity {

	@CreatedDate  // 1-3. 엔티티가 저장될때 시간을 자동으로 저장
	@Column(updatable = false)
	private LocalDateTime regTime;
	
	@LastModifiedDate  // 1-4. 엔티티의 값이 변경될떄 시간을 자동으로 저장
	private LocalDateTime updateTime;
}
