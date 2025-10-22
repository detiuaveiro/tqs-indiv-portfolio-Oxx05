Feature: Login in practice site

  Scenario: Successful search
    When I navigate to "https://cover-bookstore.onrender.com/"
    And I search for "Harry Potter"
    Then I should see 1 result
    And one of them should be titled "Harry Potter and the Sorcerer's Stone"


  Scenario: Unsuccessful search
    When I navigate to "https://cover-bookstore.onrender.com/"
    And I search for "Nonexistent Book Title"
    Then I should see 0 results
