@converter
Feature: Converter function

  @converter1
  Scenario: Validate Fahrenheit to Celsius
    Given I go to "converters" page
    When I click on "Temperature"
    And I set "Fahrenheit" to "Celsius"
    Then I enter into From field "54" and verify "12.2" result

  @converter2
  Scenario Outline: Validate more
    Given I go to "converters" page
    When I click on "<tab>"
    And I set "<from>" to "<to>"
    Then I enter into From field "<fromValue>" and verify "<toValue>" result
    Examples:
      | tab         | from       | to        | fromValue | toValue |
      | Temperature | Fahrenheit | Celsius   | 54        | 12.2    |
      | Weight      | Pound      | Kilogram  | 170       | 77      |
      | Length      | Mile       | Kilometer | 50        | 80.4    |


  @google1
  Scenario Outline: Validate various
    Given I go to "google" page
    When I click on "<tab>" button
    And I verify "<tab_content>"
    Examples:
      | tab     | tab_content       |
      | About   | Our mission is to |
      | Store   | Special Offers    |
      | Images  | How Search works  |



