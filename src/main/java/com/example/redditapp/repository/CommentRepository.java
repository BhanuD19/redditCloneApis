package com.example.redditapp.repository;

import com.example.redditapp.model.Comment;
import com.example.redditapp.model.Post;
import com.example.redditapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByPost(Post post);
  List<Comment> findAllByUser(User user);
}
