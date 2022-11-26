package com.tistory.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

	// 1-1. JPA Query Method
	Optional<User> findByUsername(String username);
	
	// 1-2. JPA Native Query Method
	@Query(value = "SELECT * FROM user WHERE username = :username AND email = :email", nativeQuery = true)
	Optional<User> findByUsernameAndEmail(@Param("username") String username, @Param("email") String email);
}
