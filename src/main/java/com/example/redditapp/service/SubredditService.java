package com.example.redditapp.service;

import com.example.redditapp.dto.SubRedditDto;
import com.example.redditapp.exceptions.SubredditNotFoundException;
import com.example.redditapp.mapper.SubredditMapper;
import com.example.redditapp.model.Subreddit;
import com.example.redditapp.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

  private final SubredditRepository subredditRepository;
  private final SubredditMapper subredditMapper;

  @Transactional
  public SubRedditDto save(SubRedditDto subRedditDto) {
    Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subRedditDto));
    subRedditDto.setId(save.getId());
    return subRedditDto;
  }

  @Transactional(readOnly = true)
  public List<SubRedditDto> getAll() {
    return subredditRepository.findAll()
      .stream()
      .map(subredditMapper::mapSubredditToDto)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public SubRedditDto getSubreddit(Long id) {
    Subreddit subreddit = subredditRepository.findById(id)
      .orElseThrow(() -> new SubredditNotFoundException("Subreddit with id "+ id+ " not found"));
    return subredditMapper.mapSubredditToDto(subreddit);
  }

}
