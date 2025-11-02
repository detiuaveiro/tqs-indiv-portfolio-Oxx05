Feature: ZeroMonos Booking System
  As a citizen or staff member
  I want to manage bulky waste collection bookings
  So that collection requests are tracked and updated efficiently

  Background:
    Given the web application is running

  # Objective 1: Citizen creates a booking
  Scenario: Citizen creates a valid booking request
    Given I open the citizen booking page
    When I fill in a valid date, municipality "Lisboa" and description "Teste UI"
    And I submit the booking form
    Then I should see a success message containing the token
    And the token list should include the new token

  # Objective 2: Citizen checks an existing booking
  Scenario: Citizen checks a booking status
    Given I have a booking token
    And I open the citizen booking page
    When I enter the token in the lookup field
    And I click "Ver estado"
    Then I should see the booking details including "Teste UI"

  # Objective 3: Citizen cancels a booking
  Scenario: Citizen cancels a booking
    Given I have a booking token
    And I open the citizen booking page
    When I enter the token in the lookup field
    And I click "Cancelar"
    Then I should see a message "Pedido cancelado"

  # Objective 4: Staff lists and filters bookings
  Scenario: Staff filters bookings by municipality
    Given I open the staff page
    When I filter bookings by municipality "Lisboa"
    Then the booking table should contain a booking with description "Teste UI"

  # Objective 5: Staff updates the booking status
  Scenario: Staff updates booking state
    Given I open the staff page
    When I filter bookings by municipality "Lisboa"
    And I click "Avançar Estado" on the booking with description "Teste UI"
    Then the booking's state should advance