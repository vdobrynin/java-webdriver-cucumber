@quote_object
Feature: Quote Feature OOP

  @quote_object1
  Scenario: Quote Required Fields OOP
    Given I go to "quote" page oop
    When I fill out required fields oop
    And I submit the form oop
    Then I verify required fields oop
