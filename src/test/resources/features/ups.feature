@ups
Feature: UPS scenarios
    @ups1
    Scenario: UPS end to end
        Given I go to "ups" page
        And I open Shipping menu
        And I go to Create a Shipment
        When I fill out origin shipment fields
        And I fill out destination shipment fields
        And I set packaging type
        And I submit the shipment form
        Then I verify origin shipment fields submitted
        And I cancel the shipment form
        Then I verify shipment form is reset

    @ups2
    Scenario: UPS project 2 – finish scenario
        Given I go to "ups" page
        And I open Shipping menu
        And I go to Create a Shipment
        When I fill out origin shipment fields
        And I fill out destination shipment fields
        And I set packaging type
        And I submit the shipment form
        Then I verify origin shipment fields submitted
        Then I verify total charges appear
        And I select cheapest delivery option
        And I submit the shipment form
        And I check a few more details
        When I submit the shipment form
        And I select Paypal payment type
        Then I review all recorded details on the review page
        And I cancel the shipment form
        Then I verify shipment form is reset
