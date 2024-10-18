Feature: Api request on /books/author/{author} for retrieving books from library - response.

  Background:
    Given the web-service under test is available at "http://localhost:8585"

  Scenario: Webservice returns a success book response for a valid author get book request
    Given the database table BOOKS is updated with data "features/api/fixtures/add-books-201.json"
    And the database table BOOKS contains author "Robert"
    When a GET request is submitted on "/books/author/Robert"
    Then the returned response status is 200
    And the response body matches multiple records the content of "features/api/response/get-books-by-author-200.json"

  Scenario: Webservice should return bad request response
    When a GET request is submitted on "/books/author//"
    Then the returned response status is 400

  Scenario: Webservice should return success response with no data
    When a GET request is submitted on "/books/author/test"
    Then the returned response status is 200
    And the response body matches multiple records the content of "features/api/response/get-books-author-not-available-200.json"
