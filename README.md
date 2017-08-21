# shortener

Run with ./mvnw spring-boot:run

Some non-standart (regarding to spring boot with WebMVC) attributes can be overriden:
* app.password.length - integer - specifies size of generated passwords. Default is 8.
* app.service.baseUrl - string - specifies url of service. Short url will be added to it. Default is localhost
* app.password.hash.algorithm - string - algorithm with which password hashing will be performed. Default is SHA-256. Possible values are MD5, SHA-1, SHA-256. More info is on https://docs.oracle.com/javase/7/docs/api/java/security/MessageDigest.html.


URL shortener
Assignment: Make an HTTP service that serves to shorten URLs, with the following functionalities:
 * Registration Web address (API)  
 * Redirect client in accordance with the shortened URL  
 * Usage Statistics (API)
 
* Assignment description:
1. Basic architecture
The service should have two parts: configuration and user.
1.1. Configuration part
The configuration part is invoked using REST calls with JSON payload and is used for: a) Opening of accounts
b) Registration of URLs in the 'Shortener' service c) Displaying stats
a) Opening of accounts

HTTP method - POST
URI - /account
Request type - application/json
Request Body - JSON object with the following parameters:  AccountId (String, mandatory)
Example: { AccountId : 'myAccountId'}
Reponse Type - application/json
Response - We distinguish the successful from the unsuccessful registration. Unsuccessful registration occurs only if the concerned account ID already exists. The parameters are as follows:
* success: true | false
* description: Description of status, for example: account with that ID already exists
* password: Returns only if the account was successfully created.Automatically generated password length of 8 alphanumeric
characters
Example {success: 'true', description: 'Your account is opened', password: 'xC345Fc0'}

 b) Registration of URLs
We power your mobile world
HTTP method -POST
URI - /register
Request type - application/json
Request Headers - Authorization header with Basic authentication token
Request Body - JSON object with the following parameters:
* url (mandatory, url that needs shortening)
* redirectType : 301 | 302 (not mandatory, default 302)
Example: {
url: 'http://stackoverflow.com/questions/1567929/website-safe-data-
access-architecture-question?rq=1', redirectType : 301
}

Reponse Type - application/json
Response - Response parameters in case of successful registration are as follows: 
* shortUrl(shortenedURL)
Example: { shortUrl: 'http://short.com/xYswlE'}


c) Retrieval of statistics
HTTP method - GET
URI - /statistic/{AccountId}
 Request Headers - Set Authorization header and authenticate user
 Response Type - application/json
Response - The server responds with a JSON object, key:value map, where the key is the registered URL, and the value is the number of this URL redirects...
Example:
{
'http://myweb.com/someverylongurl/thensomedirectory/: 10, 'http://myweb.com/someverylongurl2/thensomedirectory2/: 4, 'http://myweb.com/someverylongurl3/thensomedirectory3/: 91,
}
 
1.2. Redirecting
Redirecting the client on the configured address with the configured http status.
Binded on /
