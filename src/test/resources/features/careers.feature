@careers
Feature: Careers OOP

    @careers1
    Scenario: Careers scenario
        Given I go to "careers" page oop
        And I login as "recruiter"
        Then I verify "recruiter" login
        When I create "automation" position
        And I verify "automation" position created

    @careers2
    Scenario: Careers candidate
        Given I go to "careers" page oop
        When I apply to "automation" position
        Then I verify "candidate" profile is created
        And I see "automation" position in my jobs

    @careers2 @create_position @delete_position
    Scenario: Careers candidate 2
        Given I go to "careers" page oop
        And I apply to "automation" position
        Then I verify "candidate" profile is created
        And I see "automation" position in my jobs

    @careers5 @rest1
    Scenario: REST API Position CRUD
        Given I login to REST API as "recruiter"
        When I create via REST API "automation" position
        Then I verify via REST API position is in the list
        When I update via REST API "automation" position
        Then  I verify via REST API position is updated
        And I delete via REST API created position

    @careers5 @rest2 @configure_admin
    Scenario: REST API Position CRUD 1
        Given I login to REST API as "recruiter"
        When I create via REST API "automation" position
        Then I verify via REST API position is in the list
        When I update via REST API "automation" position
        Then I verify via REST API position is updated
        And I delete via REST API created position

    @careers5 @rest3
    Scenario: REST API Candidate CRUD 2
        Given I login to REST API as "recruiter"
        When I create via REST API "sdet" candidate
        Then I verify via REST API position is in the list
#        Then I verify via REST API candidate is created
        When I update via REST API "sdet" position
        Then  I verify via REST API position is updated
#        Then  I verify via REST API candidate is updated
        And I delete via REST API created position
#        And I delete via REST API created candidate







