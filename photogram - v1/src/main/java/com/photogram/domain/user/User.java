package com.photogram.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.photogram.domain.image.Image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// JPA - Java Persistence API(자바로 데이터를 영구적으로 저장(DB)할 수 있는 API를 제공)

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 100, unique = true)  //length = 20 -> length = 100 => oAuth2 로그인을 위해 변경
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String name;
	
	private String website;
	private String bio;
	
	@Column(nullable = false)
	private String email;
	private String phone;
	private String gender;
	
	private String profileImageUrl;
	private String role;
	
	private LocalDateTime createDate;
	
	// 1-1. 양방향 매핑시 한쪽에 mappedby를 사용했다는건 여기는 연관관계의 주인이 아니다라는 뜻이며 데이터베이스에 컬럼을 만들지 않는다.
	// 1-2. User를 Select할 때 해당 User id로 등록된 image도 같이 가져온다.
	// 1-3. 양방향 매핑은 무조건 Lazy loading으로 설정 -> 지연로딩이란 실제로 사용한 시점(getImages)에 가지고오고 처음엔 가지고 오지 않는다.
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)  
	@JsonIgnoreProperties({"user"})  // 1-4. mappedBy 된 Image 내부에 있는 user는 rest통신시 무시한다는 말.(getter를 호출하지 못하게 한다는 말, 이걸 안해놓으면 JPA 무한참조가 일어난다.)
	private List<Image> images;
	
	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

	// 1-4. image 제외한 toString
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role + ", createDate=" + createDate + "]";
	}
	
	
}
