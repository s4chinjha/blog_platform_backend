package com.sachin.blog.controllers;

import com.sachin.blog.domain.dtos.CategoryDto;
import com.sachin.blog.domain.dtos.CreateCategoryRequest;
import com.sachin.blog.domain.entities.Category;
import com.sachin.blog.mappers.CategoryMapper;
import com.sachin.blog.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/categories")
@RequiredArgsConstructor
public class CategoryController{

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    @GetMapping
    private ResponseEntity<List<CategoryDto>> listCategories(){
        List<CategoryDto> categories = categoryService.listCategories()
                .stream().map(categoryMapper::toDto)
                .toList();

        return ResponseEntity.ok(categories);
    }

    @PostMapping
    private ResponseEntity<CategoryDto> createCategory( @Valid  @RequestBody CreateCategoryRequest createCategoryRequest){

        Category categoryToCreate = categoryMapper.toEntity(createCategoryRequest);
        Category savedCategory = categoryService.createCategory(categoryToCreate);
        return new ResponseEntity<>(
                categoryMapper.toDto(savedCategory),
                HttpStatus.CREATED
        );
    }
}