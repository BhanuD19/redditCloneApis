package com.example.redditapp.repository;

import com.example.redditapp.model.Post;
import com.example.redditapp.model.User;
import com.example.redditapp.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
  Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
