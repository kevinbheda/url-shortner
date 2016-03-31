# url-shortner
A simple url shortning service 

# Setup requirements
1. Install java 8.
2. Install maven version 3.3.3 .
3. Clone the repository.
4. Open terminal and cd into the project folder.
5. To start the server run the command ' mvn spring-boot:run '. This will start the server http://localhost:8080
6  Try the shortening url service at http://localhost:8080/shorten-url, Method Type : POST, Content-type: application/json, sample json request ' { "url" : "http://www.google.com" }' and sample json response '{ "originalUrl": "http://www.google.com",  "shortUrl": "http://localhost:8080/4170157c" }'
7. Try short url redirection in the browser enter "http://localhost:8080/4170157c" and verify redirected "http://www.google.com/"
8. To run unit test in the terminal type ' mvn test '. 
