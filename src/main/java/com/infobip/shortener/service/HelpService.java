package com.infobip.shortener.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelpService {

  @RequestMapping(method = RequestMethod.GET, path = "/help")
  public String getHelp(){
    return "# URL shortener\n"
        + "\n"
        + "Run with ./mvnw spring-boot:run\n"
        + "\n"
        + "Some non-standart (regarding to spring boot with WebMVC) attributes can be overriden:\n"
        + "* app.password.length - integer - specifies size of generated passwords. Default is 8.\n"
        + "* app.service.baseUrl - string - specifies url of service. Short url will be added to it. Default is localhost\n"
        + "* app.password.hash.algorithm - string - algorithm with which password hashing will be performed. Default is SHA-256.\n"
        + "Possible values are MD5, SHA-1, SHA-256. More info is on https://docs.oracle.com/javase/7/docs/api/java/security/MessageDigest.html.";
  }
}
