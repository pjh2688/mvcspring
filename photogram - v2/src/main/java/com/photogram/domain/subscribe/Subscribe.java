package com.photogram.domain.subscribe;

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
@Table(  // 1-4. 유니크 제약조건 컬럼 두개 이상 거는 방법(중복이 안되는 유일성을 가지기 위해)
	uniqueConstraints = {
		@UniqueConstraint(
			name = "subscribe_uk",
			columnNames = {"fromUserId", "toUserId"}
		)
	}
)
public class Subscribe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 1-1. PK
	
	@ManyToOne
	@JoinColumn(name = "fromUserId")
	private User fromUser;  // 1-2. 구독하는 유저
	
	@ManyToOne
	@JoinColumn(name = "toUserId")
	private User toUser; // 1-3. 구독받는 유저
	
	private LocalDateTime createDate;  // 1-4. 생성일자
	
	@PrePersist  // 1-5. DB에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
