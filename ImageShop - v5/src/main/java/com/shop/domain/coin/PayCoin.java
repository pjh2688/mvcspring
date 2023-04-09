package com.shop.domain.coin;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class PayCoin {

	private Long historyNo;
	private Long userNo;
	
	private Long itemId;
	private Integer amount;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime modifiedDate;
}
