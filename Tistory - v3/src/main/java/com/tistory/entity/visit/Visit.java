package com.tistory.entity.visit;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

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
public class Visit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 1-1. PK
	
	@Column(nullable = true)
    private Long totalCount;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

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
    
    @Builder
    public Visit(Long totalCount, User user) {
        this.totalCount = totalCount;
        this.user = user;
    }
    

}
