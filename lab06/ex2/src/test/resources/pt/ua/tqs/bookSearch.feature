Feature: Book search in the library

  Background: A library with books
    Given an empty library
    And the following books exist:
      | title                 | author          | published          |
      | The Hobbit            | J.R.R. Tolkien  | 1937-09-21T00:00:00 |
      | The Silmarillion      | J.R.R. Tolkien  | 1977-09-15T00:00:00 |
      | 1984                  | George Orwell   | 1949-06-08T00:00:00 |
      | Animal Farm           | George Orwell   | 1945-08-17T00:00:00 |

  Scenario: Search books by author with results
    When I search for books by author "J.R.R. Tolkien"
    Then I should get 2 books
    And one of them should be titled "The Hobbit"
    And one of them should be titled "The Silmarillion"

  Scenario: Search books by author with no results
    When I search for books by author "Isaac Asimov"
    Then I should get 0 books

  Scenario: Search books published between two dates
    When I search for books published between "1940-01-01T00:00:00" and "1950-12-31T00:00:00"
    Then I should get 2 books
    And one of them should be titled "1984"
    And one of them should be titled "Animal Farm"

  Scenario: Search books published between two dates with no matches
    When I search for books published between "2000-01-01T00:00:00" and "2020-12-31T00:00:00"
    Then I should get 0 books
