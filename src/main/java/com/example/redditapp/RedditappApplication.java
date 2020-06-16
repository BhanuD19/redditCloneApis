package com.example.redditapp;

import com.example.redditapp.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class RedditappApplication {

  public static void main(String[] args) {
    SpringApplication.run(RedditappApplication.class, args);
  }
}
