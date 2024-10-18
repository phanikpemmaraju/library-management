Feature: Api request on /books/{isbn} for removing books from library - response.

  Background:
    Given the web-service under test is available at "http://localhost:8585"

  Scenario: Webservice returns a success response for a valid isbn remove book request
    Given the database table BOOKS is updated with data "features/api/fixtures/add-books-201.json"
    And the database table BOOKS contains isbn "123339"
    When that DELETE request is submitted on "/books/123339"
    Then the returned response status is 200
    And the database table BOOKS doesnt contain isbn "123339"

  Scenario: Webservice should return bad request response
    When that DELETE request is submitted on "/books//"
    Then the returned response status is 400

