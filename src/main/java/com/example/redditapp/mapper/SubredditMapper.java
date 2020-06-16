package com.example.redditapp.mapper;

import com.example.redditapp.dto.SubRedditDto;
import com.example.redditapp.model.Post;
import com.example.redditapp.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {
  @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
  SubRedditDto mapSubredditToDto(Subreddit subreddit);

  default Integer mapPosts(List<Post> numberOfPosts) {return numberOfPosts.size();}

  @InheritInverseConfiguration
  @Mapping(target = "posts", ignore = true)
  Subreddit mapDtoToSubreddit(SubRedditDto subRedditDto);
}
