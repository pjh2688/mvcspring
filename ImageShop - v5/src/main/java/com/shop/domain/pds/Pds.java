package com.shop.domain.pds;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class Pds {

	private Long pdsItemId;
	
	private String pdsItemName;
	
	private String description;
	
	@JsonIgnore
	private MultipartFile[] files;
	
	private Integer viewCount;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime modifiedDate;
}
