package com.example.redditapp.Controller;

import com.example.redditapp.dto.SubRedditDto;
import com.example.redditapp.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

  private final SubredditService subredditService;

  @PostMapping
  public ResponseEntity<SubRedditDto> createSubreddit(@RequestBody SubRedditDto subRedditDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(subredditService.save(subRedditDto));
  }

  @GetMapping
  public ResponseEntity<List<SubRedditDto>> getAllSubreddits() {
    return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<SubRedditDto> getSubreddit (@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(subredditService.getSubreddit(id));
  }
}
