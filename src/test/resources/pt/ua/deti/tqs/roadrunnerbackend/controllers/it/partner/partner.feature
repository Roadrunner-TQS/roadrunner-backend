Feature: Checkin, Checkout an Order for My Pickup Point

  Background:
    Given I am logged in as a partner
    Then I should see the partner dashboard

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





