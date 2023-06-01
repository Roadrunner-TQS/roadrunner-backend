Feature: Checkin, Checkout an Order for My Pickup Point

  Background:
    Given I am logged in as a partner
    And I have a pickup point

    Scenario: Checkin an Order for My Pickup Point
      Given I have an order
      And I should see the order status as "INTRANSIT"
      When I checkin the order
      Then I should see the order status as "AVAILABLE"

    Scenario: Checkout an Order for My Pickup Point
      Given I have an order
      And I should see the order status as "AVAILABLE"
      When I checkout the order
      Then I should see the order status as "DELIVERED"

Feature: Accept Return

      Background:
         Given I am logged in as a partner
         And I have a pickup point

     Scenario: Accept Return
        Given I have an order
        And I should see the order status as "DELIVERED"
        When I accept the return
        Then I should see the order status as "RETURNED"


Feature: Register a new Pickup Point

  Scenario:
    Given I am in Signup page
    When I change to Sign in page
    Then I should see the form to register a new user
    When I fill the form to register with the following information:
        | email | password | first_name | last_name |
        | client@roadrunner.com | 123456 | Client | Roadrunner |
    And I click on the submit button
    Then I should see the form to register a new pickup point
    When I fill the form pickup point with the following information:
        | phone | name | address | city | Latitude | Longitude | Longitude |
        | 123456 | Pickup Point | 1234 Roadrunner St | Acme | 123456 | 123456 | 123456 |
    And I click on the submit button
    Then I should see the message sucessfully registered






