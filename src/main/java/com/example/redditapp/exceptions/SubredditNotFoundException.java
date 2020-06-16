package com.example.redditapp.exceptions;

public class SubredditNotFoundException extends RuntimeException {
  public SubredditNotFoundException(String s) {
    super(s);
  }
}
