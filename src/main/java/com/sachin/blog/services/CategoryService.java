package com.sachin.blog.services;

import com.sachin.blog.domain.entities.Category;

import java.util.List;

public interface CategoryService{

    List<Category> listCategories();
    Category createCategory(Category category);
}