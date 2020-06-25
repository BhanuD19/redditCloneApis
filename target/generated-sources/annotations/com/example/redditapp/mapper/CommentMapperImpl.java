package com.example.redditapp.mapper;

import com.example.redditapp.dto.CommentDto;
import com.example.redditapp.model.Comment;
import com.example.redditapp.model.Post;
import com.example.redditapp.model.User;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-25T15:40:20+0530",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.5 (Amazon.com Inc.)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment map(CommentDto commentDto, Post post, User user) {
        if ( commentDto == null && post == null && user == null ) {
            return null;
        }

        Comment comment = new Comment();

        if ( commentDto != null ) {
            comment.setText( commentDto.getText() );
        }
        if ( post != null ) {
            comment.setPost( post );
        }
        if ( user != null ) {
            comment.setUser( user );
        }
        comment.setCreatedDate( java.time.Instant.now() );

        return comment;
    }

    @Override
    public CommentDto mapToDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDto commentDto = new CommentDto();

        commentDto.setId( comment.getId() );
        commentDto.setCreatedDate( comment.getCreatedDate() );
        commentDto.setText( comment.getText() );

        commentDto.setPostId( comment.getPost().getPostId() );
        commentDto.setUsername( comment.getUser().getUsername() );

        return commentDto;
    }
}
