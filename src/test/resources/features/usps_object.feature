@usps_object
Feature: Usps Test Suite

    @usps_object1
    Scenario: Usps Stamps
        Given I go to "usps" page oop
        When I go to "Stamps" oop
        And I sort by "Price (Low-High)" oop
#        Then I verify that "Tiffany Lamp" is cheapest oop

    @usps_object2
    Scenario: Usps Boxes
        Given I go to "usps" page oop
        When I go to "boxes" oop
        And I verify section "$0 to $5" exists oop
        And I sort by "Price (Low-High)" oop
        Then I verify that "Priority Mail Express Sticker" is cheapest oop

    @usps_object3
    Scenario: Usps Shoes
        Given I go to "usps" page oop
        When I go to "Postal Store" menu oop
        And I search for "shoe box" in store oop
        And I sort by "Price (Low-High)" oop
        Then I verify that "Priority Mail Shoe Box" is cheapest oop
#
    @usps_object4
    Scenario: Usps Labels
        Given I go to "usps" page oop
        When I go to "labels" oop
        Then I verify that "Sign In" is required oop
        Then I verify that "Sign Up" is possible oop

    @usps_object5
    Scenario: Usps Tracking
        Given I go to "usps" page oop
        When I go to "tracking" oop
        Then I verify that "Sign Up" is possible oop
