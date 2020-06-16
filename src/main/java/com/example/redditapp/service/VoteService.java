package com.example.redditapp.service;

import com.example.redditapp.dto.VoteDto;
import com.example.redditapp.exceptions.PostNotFoundException;
import com.example.redditapp.exceptions.RedditException;
import com.example.redditapp.model.Post;
import com.example.redditapp.model.Vote;
import com.example.redditapp.repository.PostRepository;
import com.example.redditapp.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.redditapp.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class VoteService {

  private final PostRepository postRepository;
  private final VoteRepository voteRepository;
  private final AuthService authService;

  public void vote(VoteDto voteDto) {
    Post post = postRepository.findById(voteDto.getPostId())
      .orElseThrow(() -> new PostNotFoundException("Post with id: "+ voteDto.getPostId()+ " not found"));
    Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
    if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
      throw new RedditException("You have already "+ voteDto.getVoteType() + "'d for this post");
    }
    if(UPVOTE.equals(voteDto.getVoteType())) {
      post.setVoteCount(post.getVoteCount() +1);
    } else {
      post.setVoteCount(post.getVoteCount() -1);
    }
    voteRepository.save(mapToVote(voteDto, post));
    postRepository.save(post);
  }

  private Vote mapToVote(VoteDto voteDto, Post post) {
    return Vote.builder()
      .voteType(voteDto.getVoteType())
      .post(post)
      .user(authService.getCurrentUser())
      .build();
  }
}
