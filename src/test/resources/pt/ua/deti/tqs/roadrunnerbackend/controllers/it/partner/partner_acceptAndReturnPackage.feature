Feature: Accept, Return an Order for My Pickup Point

  Background:
    Given I am logged in as a partner
    Then I should see the partner dashboard

  Scenario: Accept Return an Order
    Given I have an order
    Then I should see the order status as "DELIVERED"
    When I return the order
    Then I should see the order status as "RETURNED"




