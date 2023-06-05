Feature: Admin Interaction
  # Enter feature description here

  Scenario: Search for nearby pickup points
    Given I am logged in as an admin
    And I am in the pickup point management page
    When I search for pickup points in "Aveiro"
    Then I should see a list of pickup points

  Scenario: See All the Packages Present at Each Pickup Location
    Given I am logged in as an admin
    And  I am in the package management page
    When I select the pickup point "Papelaria Criativa"
    Then I should see a list of packages

  Scenario: See one package and track it
    Given I am logged in as an admin
    And  I am in the package management page
    When I select the package
    Then I should see the package details


  Scenario: Remove a partner shop
    Given I am logged in as an admin
    And I am in the partner shop management page
    When I click on the remove button of the partner shop
    Then I should not see the partner shop in the list of partner shops


  Scenario: Accept a new pickup point
    Given I am logged in as an admin
    And I am in the pickup point management page
    When I click on the accept button of the pickup point
    Then I should see a pickup point in the list of pickup points with accepted status Yes


  Scenario: Reject or Remove a pickup point
    Given I am logged in as an admin
    And I am in the pickup point management page
    When I click on the remove button of the pickup point
    Then I should not see a pickup point in the list of pickup

  Scenario: Accept a new package
    Given I am logged in as an admin
    And I am in the package management page
    And I should see the package status as "PENDING"
    When I change the status of the package to "SHIPPING"
    Then I should see a package in the list of packages with status "SHIPPING"


  Scenario: Reject a new package
    Given I am logged in as an admin
    And I am in the package management page
    And I should see the package status as "PENDING"
    When I change the status of the package to "DENIED"
    Then I should see a package in the list of packages with status "DENIED"

  Scenario: View the statistics
    Given I am logged in as an admin
    And I am in the statistics page
    Then I should see the statistics

