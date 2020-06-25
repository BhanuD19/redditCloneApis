package com.example.redditapp.mapper;

import com.example.redditapp.dto.SubRedditDto;
import com.example.redditapp.dto.SubRedditDto.SubRedditDtoBuilder;
import com.example.redditapp.model.Subreddit;
import com.example.redditapp.model.Subreddit.SubredditBuilder;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-21T01:18:59+0530",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.5 (Amazon.com Inc.)"
)
@Component
public class SubredditMapperImpl implements SubredditMapper {

    @Override
    public SubRedditDto mapSubredditToDto(Subreddit subreddit) {
        if ( subreddit == null ) {
            return null;
        }

        SubRedditDtoBuilder subRedditDto = SubRedditDto.builder();

        subRedditDto.id( subreddit.getId() );
        subRedditDto.name( subreddit.getName() );
        subRedditDto.description( subreddit.getDescription() );

        subRedditDto.numberOfPosts( mapPosts(subreddit.getPosts()) );

        return subRedditDto.build();
    }

    @Override
    public Subreddit mapDtoToSubreddit(SubRedditDto subRedditDto) {
        if ( subRedditDto == null ) {
            return null;
        }

        SubredditBuilder subreddit = Subreddit.builder();

        subreddit.id( subRedditDto.getId() );
        subreddit.name( subRedditDto.getName() );
        subreddit.description( subRedditDto.getDescription() );

        return subreddit.build();
    }
}
