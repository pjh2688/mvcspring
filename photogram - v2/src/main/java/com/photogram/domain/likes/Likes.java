package com.photogram.domain.likes;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.photogram.domain.image.Image;
import com.photogram.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(
	// 1-2. 유니크 제약조건 컬럼 두개 이상 거는 방법(중복이 안되는 유일성을 가지기 위해)
	uniqueConstraints = {
		@UniqueConstraint(
			name = "likes_uk",
			columnNames = {"imageId", "userId"}
		)
	}
)
public class Likes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JoinColumn(name = "imageId")
	@ManyToOne
	private Image image;
	
	@JsonIgnoreProperties({"images"})  // 1-1. 무한 참조 해결(좋아요할때 발생)
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user;
	
	private LocalDateTime createDate;
	
	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
