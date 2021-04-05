package com.example.redditapp.service;

import com.example.redditapp.dto.CommentDto;
import com.example.redditapp.exceptions.PostNotFoundException;
import com.example.redditapp.mapper.CommentMapper;
import com.example.redditapp.model.Comment;
import com.example.redditapp.model.Post;
import com.example.redditapp.model.User;
import com.example.redditapp.repository.CommentRepository;
import com.example.redditapp.repository.PostRepository;
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
@Transactional
@Slf4j
public class CommentService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final CommentMapper commentMapper;
  private final AuthService authService;
  private final CommentRepository commentRepository;

  public void save(CommentDto commentDto) {
    Post post= postRepository.findById(commentDto.getPostId())
      .orElseThrow(() -> new PostNotFoundException("Post with id "+ commentDto.getPostId()+ " not found"));
    Comment comment = commentMapper.map(commentDto, post, authService.getCurrentUser());
    log.info("comment to save:" + comment);
    commentRepository.save(comment);
  }

  @Transactional(readOnly = true)
  public List<CommentDto> getAllCommentsForPost(Long postId) {
    Post post = postRepository.findById(postId)
      .orElseThrow(() -> new PostNotFoundException("Post with id" + postId + " not found"));
    return commentRepository.findByPost(post)
      .stream()
      .map(commentMapper::mapToDto)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<CommentDto> getAllCommentsForUser(String username) {
    User user = userRepository.findByUsername(username)
      .orElseThrow(()-> new UsernameNotFoundException(username));
    return commentRepository.findAllByUser(user)
      .stream()
      .map(commentMapper::mapToDto)
      .collect(Collectors.toList());
  }
}
