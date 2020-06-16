package com.example.redditapp.exceptions;

public class PostNotFoundException extends RuntimeException {
  public PostNotFoundException(String s) {
    super(s);
  }
}
