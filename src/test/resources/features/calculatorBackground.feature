  Feature: Test Background Feature
  Description: To test the Background

  Background:
    Given I go to "converters" page

  @converter4
  Scenario: Validate Fahrenheit to Celsius
    When I click on "Temperature"
    And I set "Fahrenheit" to "Celsius"
    Then I enter into From field "54" and verify "12.2" result

  @converter5
  Scenario: Validate Pound to Kilogram
    When I click on "Weight"
    And I set "Pound" to "Kilogram"
    Then I enter into From field "170" and verify "77" result
