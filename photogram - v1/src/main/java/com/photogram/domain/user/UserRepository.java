package com.photogram.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

// 1. 어노테이션이 없어도 JpaRepository를 상속하면 IoC 등록이 자동으로 된다.
public interface UserRepository extends JpaRepository<User, Long> {

	// 2. JPA Query Method
	User findByUsername(String username);
}
