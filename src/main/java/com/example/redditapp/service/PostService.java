package com.example.redditapp.service;

import com.example.redditapp.dto.PostRequest;
import com.example.redditapp.dto.PostResponse;
import com.example.redditapp.exceptions.PostNotFoundException;
import com.example.redditapp.exceptions.SubredditNotFoundException;
import com.example.redditapp.mapper.PostMapper;
import com.example.redditapp.model.Post;
import com.example.redditapp.model.Subreddit;
import com.example.redditapp.model.User;
import com.example.redditapp.repository.PostRepository;
import com.example.redditapp.repository.SubredditRepository;
import com.example.redditapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

  private final AuthService authService;
  private final SubredditRepository subredditRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostMapper postMapper;

  public void save(PostRequest postRequest) {
    Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
      .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()+ "Subreddit not found"));

    postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
  }

  @Transactional(readOnly = true)
  public List<PostResponse> getAllPosts() {
    return postRepository.findAll()
      .stream()
      .map(postMapper::mapToDto)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public PostResponse getPost(Long id) {
    Post post = postRepository.findById(id)
      .orElseThrow(() -> new PostNotFoundException(id.toString()));
    return postMapper.mapToDto(post);
  }

  @Transactional(readOnly = true)
  public List<PostResponse> getPostsBySubreddit(Long subredditId) {
    Subreddit subreddit = subredditRepository.findById(subredditId)
      .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString() + " subreddit not found"));
    List<Post> posts = postRepository.findAllBySubreddit(subreddit);
    return posts
      .stream()
      .map(postMapper::mapToDto)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<PostResponse> getPostsByUsername(String username) {
    User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException(username));
    return postRepository.findByUser(user)
      .stream()
      .map(postMapper::mapToDto)
      .collect(Collectors.toList());
  }
}
