package com.example.redditapp.Controller;

import com.example.redditapp.dto.VoteDto;
import com.example.redditapp.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes/")
@AllArgsConstructor
public class VoteController {

  private final VoteService voteService;
  @PostMapping
  public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto) {
    voteService.vote(voteDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
