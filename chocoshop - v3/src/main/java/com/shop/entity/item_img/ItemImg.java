package com.shop.entity.item_img;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.shop.entity.BaseEntity;
import com.shop.entity.item.Item;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item_img")
@Getter @Setter
public class ItemImg extends BaseEntity {

	// 1-1. PK
	@Id
	@Column(name = "item_img_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 1-2. 이미지 파일명
	private String imgName;
	
	// 1-3. 원본 이미지 파일명
	private String oriImgName;
	
	// 1-4. 이미지 경로
	private String imgUrl;
	
	// 1-5. 대표 이미지 여부
	private String repImgYn;
	
	// 1-6. 단방향 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	// 1-7.
	public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}
}
