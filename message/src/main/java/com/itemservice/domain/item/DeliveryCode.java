package com.itemservice.domain.item;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * FAST : 빠른 배송 
 * NORMAL : 일반 배송
 * SLOW : 느린 배송
 */

@Data
@AllArgsConstructor  
public class DeliveryCode {

	private String code;  // 1. 시스템에서 전달하는  값.
	private String displayName;  // 2. 외부에 보여주는 값.
}
