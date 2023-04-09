package com.shop.domain.item;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class Item {

	private Long itemId;
	
	private String itemName;
	
	private Integer price;
	
	private String description;
	
	@JsonIgnore
	private MultipartFile picture;
	
	private String pictureUrl;
	
	private String pictureName;
	
	@JsonIgnore
	private MultipartFile preview;
	
	private String previewUrl;
	
	private String previewName;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime modifiedDate;
}
