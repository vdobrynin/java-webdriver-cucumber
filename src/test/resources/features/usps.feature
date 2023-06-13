@usps
Feature: USPS fill out forms

  @usps1
  Scenario: Validate ZIP code for Portnov Computer School
    Given I go to "usps" page
    And I go to Lookup ZIP page by address
    And I fill out "4970 El Camino Real" street, "Los Altos" city, "CA" state
    Then I validate "94022" zip code exists in the result

  @usps2
  Scenario: Validate ZIP for Other address
    Given I go to "usps" page
    And I go to Lookup ZIP page by address
    And I fill out "1820-22 S Grant St" street, "San Mateo" city, "CA" state
    Then I validate "94402" zip code exists in the result

  @usps3
  Scenario Outline: Validate ZIP code Other address more
    Given I go to "usps" page
    And I go to Lookup ZIP page by address
    And I fill out "<street>" street, "<city>" city, "<state>" state
    Then I validate "<zip>" zip code exists in the result
    Examples:
      |     street             |    city          |  state   |   zip    |
      | 4970 El Camino Real    |  Los Altos       |    CA    |  94022   |
      | 1820-22 S Grant St     |  San Mateo       |    CA    |  94402   |
      | 299 Bayshore Blvd      |  San Francisco   |    CA    |  94124   |

  @usps4
  Scenario: Calculate price
    Given I go to "usps" page
    When I go to Calculate Price Page
    And I selected "United Kingdom" with "Postcard" shape
    And I define "2" quantity
    Then I calculate the price and validate cost is "$2.30"

  @usps5
  Scenario: Wrong store id does not match
    Given I go to "usps" page
    When I go to "Postal Store" tab
    And I enter "12345" into store search
    Then I search and validate no products found

  @usps6
  Scenario: One item found
    Given I go to "usps" page
    When I go to Stamps and Supplies page
    And I open Stamps
    And choose category Priority Mail
    Then I verify 1 item found in result

  @usps7
  Scenario: Verify color
    Given I go to "usps" page
    When I go to Stamps and Supplies page
    And I open Stamps
    When I unselect Stamps checkbox
    And select size "Large"
    And I click "blue" color
    Then I verify "Blue" and "Large" filters
    Then I verify "20% OFF" sale

  @usps8
  Scenario: Verify location
    Given I go to "USPS" page
    When I perform "Passports" search
    And I select "Passport Application" in results
    And I click "Find a Post Office" button
    And I select "94022" zip code within "10 miles" and search
#    Then I verify "MOUNTAIN VIEW" present in search results
#   When I open "MOUNTAIN VIEW" search results
#    Then I verify "211 HOPE ST" address, "9:00am - 12:00pm" appointment hours, "1:00pm - 3:00pm" photo hours
#    When I go back to list
#    Then I verify I'm on Find Locations search page


  @usps9
  Scenario: PO Box
    Given I go to "usps" page
    When I go to "PO Boxes" under "Track & Manage"
    And I reserve new PO box for "94022"
    Then I verify that "Los Altos — Post Office™" present
    And I verify that "Size 5-XL" PO Box is available in "Los Altos — Post Office™"


