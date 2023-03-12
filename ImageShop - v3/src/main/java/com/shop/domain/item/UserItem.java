package com.shop.domain.item;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UserItem {

	private Long userItemNo;
	private Long userNo;
	
	private Long itemId;
	private String itemName;
	private Integer price;
	private String description;
	private String pictureUrl;;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdDate;
	
}
