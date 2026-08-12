package com.sachin.blog.services;

import com.sachin.blog.domain.entities.Post;
import com.sachin.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllposts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
}
