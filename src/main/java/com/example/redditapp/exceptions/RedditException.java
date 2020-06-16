package com.example.redditapp.exceptions;

public class RedditException extends RuntimeException {
  public RedditException(String exMessage, Exception exception) {
    super(exMessage, exception);
  }

  public RedditException(String message) {
    super(message);
  }
}
