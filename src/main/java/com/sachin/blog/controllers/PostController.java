package com.sachin.blog.controllers;

import com.sachin.blog.domain.CreatePostRequest;
import com.sachin.blog.domain.dtos.CreatePostRequestDto;
import com.sachin.blog.domain.dtos.PostDto;
import com.sachin.blog.domain.entities.Post;
import com.sachin.blog.domain.entities.User;
import com.sachin.blog.mappers.PostMapper;
import com.sachin.blog.services.PostService;
import com.sachin.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final PostMapper postMapper;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllposts(
            @RequestParam(required = false)UUID categoryId,
            @RequestParam(required = false)UUID tagId){
        List<Post> posts = postService.getAllposts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID id){
        User loggedInUser = userService.getUserById(id);
        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
        List<PostDto> postDtos = draftPosts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute UUID userId
            ){
        User loggedInUser = userService.getUserById(userId);
        CreatePostRequest cretaePostRequest = postMapper.toCretaePostRequest(createPostRequestDto);

        Post createdPost = postService.createPost(loggedInUser, cretaePostRequest);
        PostDto createdPostDto = postMapper.toDto(createdPost);

        return new ResponseEntity<>(
                createdPostDto,
                HttpStatus.CREATED
        );
    }
}
