package com.photogram.domain.image;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.photogram.domain.comment.Comment;
import com.photogram.domain.likes.Likes;
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
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 1-1. PK
	
	private String caption;  // 1-2. 상세 설명.
	
	private String postImageUrl;  // 1-3. 실제 사진이 저장된 pc내 폴더 경로
	
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name = "userId")
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;  // 1-4. 유저 정보
	
	// 1-5. 이미지 좋아요.
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image")
	private List<Likes> likes; 
	
	// 1-6. 
	@Transient  // DB에 컬럼을 만들지 않는다.
	private Boolean likeState;
	
	// 1-7. 
	@Transient  // DB에 컬럼을 만들지 않는다.
	private int likeCount;
	
	// 1-8. 댓글
	@OrderBy("id desc") // 1-11. 내림 차순으로 정렬해서 가져오기
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image")
	private List<Comment> comments;
	
	private LocalDateTime createDate;  // 1-9. 생성일자
	
	@PrePersist  // 1-10. DB에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}