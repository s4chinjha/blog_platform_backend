package com.sachin.blog.services;

import com.sachin.blog.domain.CreatePostRequest;
import com.sachin.blog.domain.UpdatePostRequest;
import com.sachin.blog.domain.entities.Post;
import com.sachin.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post getPost(UUID id);
    void deletePost(UUID id);
    List<Post> getAllposts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
}
