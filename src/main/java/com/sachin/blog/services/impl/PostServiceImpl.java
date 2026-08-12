package com.sachin.blog.services.impl;

import com.sachin.blog.domain.CreatePostRequest;
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
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    private static final int WORDS_PER_MINUTE = 200;

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

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

        Category category = categoryService.getCategoryByID(createPostRequest.getCategoryId());
        newPost.setCategory(category);

        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagByIds(tagIds);
        newPost.setTags( new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    private Integer calculateReadingTime(String content){
        if(content == null || content.isEmpty()){
            return 0;
        }

        int wordCount = content.trim().split("\\s+").length;
        return  (int)Math.ceil((double)(wordCount / WORDS_PER_MINUTE));
    }
}
