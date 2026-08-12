package com.sachin.blog.services.impl;

import com.sachin.blog.domain.PostStatus;
import com.sachin.blog.domain.entities.Category;
import com.sachin.blog.domain.entities.Post;
import com.sachin.blog.domain.entities.Tag;
import com.sachin.blog.domain.entities.User;
import com.sachin.blog.repositories.PostRepository;
import com.sachin.blog.services.CategoryService;
import com.sachin.blog.services.PostService;
import com.sachin.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllposts(UUID categoryId, UUID tagId) {
        if(categoryId!=null && tagId!=null){
            Category category = categoryService.getCategoryByID(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
            );

        }

        if(categoryId!=null){
            Category category = categoryService.getCategoryByID(categoryId);
            return postRepository.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
            );
        }

        if(tagId != null){
            Tag tag = tagService.getTagById(tagId);
           return postRepository.findAllByStatusAndTagsContaining(
                   PostStatus.PUBLISHED,
                   tag
           );
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(
                user,
                PostStatus.DRAFT
        );
    }
}
