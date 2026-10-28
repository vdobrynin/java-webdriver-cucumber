@predefined
Feature: Smoke steps
    @predefined1
    Scenario: Predefined steps for Google
        Given I open url "https://google.com"
        Then I should see page title as "Google"
        Then element with xpath "//textarea[@name='q']" should be present
        When I type "Behavior Driven Development" into element with xpath "//textarea[@name='q']"
        Then I wait for element with xpath "//ul[@role='listbox']" to be present
        Then element with xpath "//ul[@role='listbox']" should contain text "behavior driven development"

    @predefined2
    Scenario: Steps for Wikipedia
        Given I open url "https://en.wikipedia.org/wiki/Main_Page"
        Then I should see page title as "Wikipedia, the free encyclopedia"
        Then element with xpath "//input[@id='searchInput']" should be present
        When I type "Behavior Driven Development" into element with xpath "//input[@id='searchInput']"
        And I click on element with xpath "//button[normalize-space()='Search']"
        And I wait for element with xpath "//h1[@id='firstHeading']" to be present
        Then element with xpath "//h1[@id='firstHeading']" should contain text "Behavior-driven development"

    @predefined3
    Scenario: Predefined steps for Bing
        Given I open url "https://www.bing.com/"
        Then element with xpath "//form[@id='sb_form']" should be present
        When I type "Behavior Driven Development" into element with xpath "//*[@id='sb_form_q']"
        And I click on element with xpath "//label[@id='search_icon']//*[name()='svg']" to submit
        Then I wait for element with xpath "//ol[@id='b_results']" to be present
        Then element with xpath "//ol[@id='b_results']" should contain text "BDD"

    @predefined4
    Scenario: Steps for Yahoo
        Given I open url "https://www.yahoo.com/"
        Then I should see page title as "Yahoo | Mail, Weather, Search, Politics, News, Finance, Sports & Videos"
        Then element with xpath "//input[@id='uh-sbq']" should be present
        When I type "Java" into element with xpath "//input[@id='uh-sbq']"
        Then element with xpath "//input[@id='uh-sbq']" should have attribute "value" as "Java"
