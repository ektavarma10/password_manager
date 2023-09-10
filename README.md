# Password Manager

This is a Java/Spring based microservice to generate/manage passwords. This has following functionalities : 

* `POST /user/sign-up` - Registers a user using a password and username. 
* `POST /user/sign-in` - Generates a jwtToken for a given user with claims.

* `POST /password/generate` - Generates a 10 character password with alphanumerics and special characters using Secure Random.
* `POST /password/` - Creates a vault entry with given username, website and password.
* `POST /password/show/` - Fetches username and password for a given website.
* `POST /password/show/` - Fetches all username and passwords.
* `POST /password/preview` - Takes a valid jwtToken and previews all website and username.

Postman Collection - [Link](https://www.postman.com/avionics-pilot-71349591/workspace/naruto-4/collection/21463046-48de42e5-b6e4-4627-ba78-24560b58761a)


## Technical Requirements 
* Java11 
* Spring Boot 2.7 (managed via gradle)
* SQL

## How to Run 
``` shell
 JWT_SECRET=my-secret ./gradlew bootrun 

```
