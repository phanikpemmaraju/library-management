# Library Management project

### Introduction

A Restful web-service application that provides functionality to support Library management activities:
-

- add-books
- find-books-by-ISBN
- find-books-by-author
- remove-books
- borrow-books
- return-books

#### Pre-requisites

- Java 1.8
- Maven


### Run locally

Application can be run in below multiple ways

```bash
java -jar target/library-management-1.0.jar
```

```
mvn spring-boot:run
```

````
Open the project in any IDE, and run the Application.class
````
## General Assumptions/Considerations:
In order to achieve the functionality of a library management service in the given stipulated time, following assumptions and considerations are made.

1. Use of H2 in-memory database to persist the book records.
2. Resilience4j library is used for Rate limiting feature.
3. Caffeine cache is used to cache the frequently used data to improve performance. ( find all books for an author )
4. BDD cucumber framework used for IT tests.
5. Given in the timeframe, i couldn't able to finish/add the JWT token security feature.
6. Data Validations are in place on the apis.
7. SOLID patterns used across.
8. Proper error handling and appropriate HTTP status codes used.
9. Excellent code coverage.
10. DTO and Domain objects segregation.
11. MapStruct is used for mapping to/from request payloads to domain objects.


## Request Payloads and Requests:

Once the application is run locally (http://localhost:8282), you can test the application using the request payloads located below.

`` src/test/resources/features/api/request``

All the endpoints and scenarios can be found in the below.

``src/test/resources/features/api/add-book.feature``
``src/test/resources/features/api/borrow-book-by-isbn.feature``
``src/test/resources/features/api/find-book-by-author.feature``
``src/test/resources/features/api/find-book-by-isbn.feature``
``src/test/resources/features/api/rate-limit.feature``
``src/test/resources/features/api/remove-book.feature``
``src/test/resources/features/api/return-book-by-isbn.feature``





