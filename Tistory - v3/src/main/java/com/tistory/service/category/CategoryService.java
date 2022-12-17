package com.tistory.service.category;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tistory.entity.category.Category;
import com.tistory.entity.category.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	
	public void categoryRegister(Category category) {
		categoryRepository.save(category);
	}

	@Transactional(readOnly = true)
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}
}
