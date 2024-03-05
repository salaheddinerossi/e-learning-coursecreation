package com.example.coursecreation.repository;

import com.example.coursecreation.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
