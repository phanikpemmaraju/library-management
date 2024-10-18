Feature: Api request on /books/return/{isbn} for borrowing books from library - response.

  Background:
    Given the web-service under test is available at "http://localhost:8585"

  Scenario: Webservice returns a success book response for a valid isbn return book request
    Given the database table BOOKS is updated with data "features/api/fixtures/add-books-201.json"
    And the database table BOOKS contains isbn "123339"
    When that PUT request is submitted on "/books/borrow/123339"
    Then the returned response status is 200
    And the response body matches the content of "features/api/response/borrow-books-by-isbn-200.json"
    And the database table BOOKS contains isbn "123339" with available copies 1
    When that PUT request is submitted on "/books/return/123339"
    Then the returned response status is 200
    And the response body matches the content of "features/api/response/return-books-by-isbn-200.json"
    And the database table BOOKS contains isbn "123339" with available copies 2


  Scenario: Webservice returns a not acceptable response when any of the copies are not borrowed
    Given the database table BOOKS is updated with data "features/api/fixtures/add-books-201.json"
    And the database table BOOKS contains isbn "123339"
    When that PUT request is submitted on "/books/return/123339"
    Then the returned response status is 406

  Scenario: Webservice should return bad request response
    When that PUT request is submitted on "/books/return//"
    Then the returned response status is 400

  Scenario: Webservice should return not found response
    When that PUT request is submitted on "/books/return/1234"
    Then the returned response status is 404
