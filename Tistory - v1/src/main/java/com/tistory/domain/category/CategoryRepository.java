package com.tistory.domain.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	@Query(value = "SELECT * FROM category WHERE userId = :userId", nativeQuery = true)
    List<Category> findByUserId(@Param("userId") Long userId);
}
