package com.example.redditapp.Controller;

import com.example.redditapp.dto.CommentDto;
import com.example.redditapp.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
@Slf4j
public class CommentsController {

  private final CommentService commentService;

  @PostMapping
  public ResponseEntity<Void> createComment(@RequestBody CommentDto commentDto) {
    log.info("comment body: " + commentDto);
    commentService.save(commentDto);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("by-post/{postId}")
  public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@PathVariable Long postId) {
     return ResponseEntity.status(HttpStatus.OK)
       .body(commentService.getAllCommentsForPost(postId));
  }

  @GetMapping("by-user/{username}")
  public ResponseEntity<List<CommentDto>> getAllCommentsForUser(@PathVariable String username) {
    return ResponseEntity.status(HttpStatus.OK)
      .body(commentService.getAllCommentsForUser(username));
  }
}
