package com.infobip.shortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class ShortenerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShortenerApplication.class, args);
  }
}
