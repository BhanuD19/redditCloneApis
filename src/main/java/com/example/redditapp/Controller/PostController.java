package com.example.redditapp.Controller;

import com.example.redditapp.dto.PostRequest;
import com.example.redditapp.dto.PostResponse;
import com.example.redditapp.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts/")
@AllArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping
  public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
    postService.save(postRequest);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<PostResponse>> getAllPosts() {
    return status(HttpStatus.OK).body(postService.getAllPosts());
  }

  @GetMapping("{id}")
  public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
    return status(HttpStatus.OK).body(postService.getPost(id));
  }

  @GetMapping("by-subreddit/{id}")
  public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id) {
    return status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));
  }

  @GetMapping("by-user/{name}")
  public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String name) {
    return status(HttpStatus.OK).body(postService.getPostsByUsername(name));
  }
}
