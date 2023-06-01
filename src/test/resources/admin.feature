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
    And I should see the new partner in the list of shops

  Scenario: Remove a partner shop
    Given I am logged in as an admin
    And I am in the partner shop management page
    When I click on the remove button of the partner shop "Papelaria Criativa"
    Then I should see a success message
    And I should not see the partner shop "Papelaria Criativa" in the list of shops


  Scenario: Accept a new pickup point
    Given I am logged in as an admin
    And I am in the pickup point management page
    When I click on the accept button of the pickup point
    Then I should see a pickup point in the list of pickup points with accepted status 'Yes'


  Scenario: Reject a new pickup point
    Given I am logged in as an admin
    And I am in the pickup point management page
    When I click on the reject button of the pickup point
    Then I should not see a pickup point in the list of pickup

  Scenario: Accept a new package
    Given I am logged in as an admin
    And I am in the package management page
    And I should see the package status as "PENDING"
    When I change the status of the package to "ACCEPTED"
    Then I should see a package in the list of packages with status "ACCEPTED"


  Scenario: Reject a new package
    Given I am logged in as an admin
    And I am in the package management page
    And I should see the package status as "PENDING"
    When I change the status of the package to "DENIED"
    Then I should see a package in the list of packages with status "DENIED"

