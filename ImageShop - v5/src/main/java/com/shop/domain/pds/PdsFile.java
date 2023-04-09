package com.shop.domain.pds;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class PdsFile {

	private Long pdsFileId;
	
	private String fileName;
	
	private String fileUrl;
	
	private Long pdsItemId;
	
	private Integer downCnt;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime modifiedDate;
}
