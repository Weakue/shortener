# shortener

Run with ./mvnw spring-boot:run

Some attributes can be overriden:
* app.password.length - integer - specifies size of generated passwords
* app.service.baseUrl - string - specifies url to with shortage will be added
* app.password.hash.algorithm - string - algorithm with which password hashing will be performed. Possible values are MD5, SHA-1, SHA-256. More info is on https://docs.oracle.com/javase/7/docs/api/java/security/MessageDigest.html
