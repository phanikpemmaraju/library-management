Feature: Api request on /books/{isbn} for retrieving books from library - response.

  Background:
    Given the web-service under test is available at "http://localhost:8585"

  Scenario: Webservice returns a success book response for a valid isbn get book request
    Given the database table BOOKS is updated with data "features/api/fixtures/add-books-201.json"
    And the database table BOOKS contains isbn "123339"
    When a GET request is submitted on "/books/123339"
    Then the returned response status is 200
    And the response body matches the content of "features/api/response/find-books-by-isbn-200.json"

  Scenario: Webservice should return bad request response
    When a GET request is submitted on "/books//"
    Then the returned response status is 400

  Scenario: Webservice should return not found response
    When a GET request is submitted on "/books/1234"
    Then the returned response status is 404
