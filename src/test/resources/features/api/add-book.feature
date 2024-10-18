Feature: Api request on /books for Adding new books to library - response.

  Background:
    Given the web-service under test is available at "http://localhost:8585"

  Scenario: Webservice returns a success response for a valid add books request
    Given a request body of "features/api/request/add-books-201.json"
    And the content type is "application/json"
    When that POST request is submitted on "/books"
    Then the returned response status is 201
    And the response body matches the content of "features/api/response/add-books-201.json"
    And the database table BOOKS contains the attributes defined in "features/api/fixtures/add-books-201.json" for isbn "123339"

  Scenario: Webservice should return bad request response
    Given a request body of "features/api/request/add-books-bad-request.json"
    And the content type is "application/json"
    When that POST request is submitted on "/books"
    Then the returned response status is 400
    And the response body matches the content of "features/api/response/add-books-bad-request-response.json"

