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
    When I click on the pickup location "Papelaria Criativa"
    Then I should see a list of packages

  Scenario: Add a new partner shop
    Given I am logged in as an admin
    When I click on the add partner button
    And I fill the form with the following information:
      | Name | Address | City | Latitude | Longitude |
      | Papelaria Criativa | Rua do Comercio, 3810-200 Aveiro | Aveiro | 40.6405 | -8.6538 |
    And I click on the submit button
    Then I should see a success message
    And I should see the new partner in the list of pickup points