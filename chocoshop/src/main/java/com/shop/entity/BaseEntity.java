package com.shop.entity;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@EntityListeners(value = {AuditingEntityListener.class}) // 1-1. Auditing을 적용하기 위해서 @EntityListeners 어노테이션 추가 
@MappedSuperclass  // 1-2. 공통 매핑 정보가 필요할 때 사용하는 어노테이션으로 부모 클래스를 상속 받는 자식 클래스에게 매핑 정보만 제공한다.
@Getter
public abstract class BaseEntity extends BaseTimeEntity {

	@CreatedBy
	@Column(updatable = false)
	private String createdBy;
	
	@LastModifiedBy
	private String modifiedBy;
}
