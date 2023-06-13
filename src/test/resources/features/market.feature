@market
Feature: Marketing app

  @market1
  Scenario: Base scenario
    Given I open quote page
    Given I go to "quote" page
    And I print page details
    When I fill out required fields
    And I submit the form
    Then I verify required fields

  @market2
  Scenario: Navigation
    Given I go to "google" page
    And I print page details
    And I print console logs
    And I go to "yahoo" page
    And I go to "quote" page
    And  I go back and forward, then refresh the page

  @market3
  Scenario: Custom steps
    Given I go to "quote" page
    And I print page details
    Given I go to "google" page
    And I go back and forward, then refresh the page
    And I go to "quote" page
    And I change resolution to "phone"
    And I change resolution to "desktop"
    And I fill out required fields
    When I fill out optional fields
    And I "accept" agreement
    And I "dismiss" agreement
    When I verify email field behavior
    And I submit the page
    Then I verify that fields values recorded correctly

  @market4
  Scenario: Switch to cases
    Given I go to "quote" page
    When I "accept" third party agreement
#    When I "reject" third party agreement
    And I input "John" "Doe" as contact
    And I validate document "Document 2" present



