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

//JPA - Java Persistence API(자바로 데이터를 영구적으로 저장(DB)할 수 있는 API를 제공)

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 1-1. PK
	
	@Column(length = 100, unique = true, nullable = false)  // 1-16. OAuth2 로그인을 위해 20 -> 100자로 늘린다.
	private String username;  // 1-2. 계정 명
	
	@Column(nullable = false)
	private String password; // 1-3. 계정 비밀번호
	
	@Column(nullable = false)
	private String name;  // 1-4. 사용자 이름
	
	private String website; // 1-5. 웹사이트
	
	private String bio;  // 1-6. 자기소개
	
	@Column(nullable = false)
	private String email;  // 1-7. 이메일
	
	private String phone;  // 1-8. 휴대폰 번호
	
	private String gender;  // 1-9. 성별
	
	private String profileImageUrl;  // 1-10. 프로필 이미지 경로
	
	private String role;  // 1-11. 권한
	
	private LocalDateTime createDate;  // 1-12. 생성일자
	
	@PrePersist  // 1-13. DB에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	
	// 1-14. image 엔티티 양방향 매핑 mappedBy 속성의 의미는 나는 연관관계의 주인이 아니므로 DB에 테이블을 만들지 말라는 뜻.
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)  // 1-15. Lazy 전략은 image를 get할때만 같이 select 하라는 의미.
	@JsonIgnoreProperties({"user"})  // 1-16. image 엔티티에 있는 user는 또 불러오지 않는다.(무한참조)
	private List<Image> images;

	// 1-15. AOP 처리시 User 객체 따로 sysout 해보고 싶을 때 toString 커스터마이징(images 제거)
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role + ", createDate=" + createDate + "]";
	} 
	
}