@workday
Feature: Applying to position

    @workday1
    Scenario: Validate OAuth
        Given I go to "careers" page
        And I select any position
        And I go with Apply with LinkedIn
        Then I verify login window opens

    @dice
    Scenario: Validate an OAuth
        Given I go to "dice" page
        And I select any tech position
        And I go with Apply
        Then I verify opens login window

