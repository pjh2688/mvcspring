package com.photogram.domain.image;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
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
	private Long id;  // 1-1. 기본 키
	private String name; // 1-2. 이미지 이름
	private String caption; // 1-3. 사진 상세 설명.
	private String postImageUrl; // 1-4. 사진을 전송받아서 그 사진을 서버에 특정 폴더에 저장 - DB에 그 저장된 경로를 insert
	
	@JsonIgnoreProperties({"images"})  // 1-9. 이미지정보는 이미 있어서 필요가 없으므로 어노테이션 붙여준다.
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user; // 1-5. 이미지를 업로드한 유저 정보.
	
	private LocalDateTime createDate;  // 1-6. insert 시간.
	
	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now(); 
	}
	
	// 1-7. 이미지 좋아요
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image")
	private List<Likes> likes;
	
	@Transient  // 1-8. DB에 컬럼을 만들지 않는다.
	private Boolean likeState;
	
	// 1-9. 좋아요 카운트
	@Transient
	private int likeCount;
	
	// 1-10. 이미지 댓글
	@OrderBy("id DESC")
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image")
	private List<Comment> comments;

}
