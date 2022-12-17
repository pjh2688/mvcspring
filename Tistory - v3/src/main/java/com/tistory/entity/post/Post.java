package com.tistory.entity.post;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.tistory.entity.category.Category;
import com.tistory.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 1-1. PK

	@Column(length = 60, nullable = false)
    private String title;

    @Lob
    @Column(nullable = true)
    private String content;

    @Column(length = 200, nullable = true)
    private String thumnail;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    @JoinColumn(name = "categoryId")
    @ManyToOne
    private Category category;

    private LocalDateTime createDate;
    
    private LocalDateTime updateDate;

    @PrePersist 
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

    @PreUpdate 
	public void updateDate() {
		this.updateDate = LocalDateTime.now();
	}
}
