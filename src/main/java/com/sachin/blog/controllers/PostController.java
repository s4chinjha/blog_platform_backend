package com.sachin.blog.controllers;

import com.sachin.blog.domain.dtos.PostDto;
import com.sachin.blog.domain.entities.Post;
import com.sachin.blog.mappers.PostMapper;
import com.sachin.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    private final PostMapper postMapper;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllposts(
            @RequestParam(required = false)UUID categoryId,
            @RequestParam(required = false)UUID tagId){
        List<Post> posts = postService.getAllposts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }
}
