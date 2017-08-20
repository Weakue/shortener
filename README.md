# shortener

Run with ./mvnw spring-boot:run

Some non-standart (regarding to spring boot with WebMVC) attributes can be overriden:
* app.password.length - integer - specifies size of generated passwords. Default is 8.
* app.service.baseUrl - string - specifies url of service. Short url will be added to it. Default is localhost
* app.password.hash.algorithm - string - algorithm with which password hashing will be performed. Default is SHA-256. Possible values are MD5, SHA-1, SHA-256. More info is on https://docs.oracle.com/javase/7/docs/api/java/security/MessageDigest.html.
