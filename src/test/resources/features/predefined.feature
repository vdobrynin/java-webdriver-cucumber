@predefined
Feature: Smoke steps

    @predefined1
    Scenario: Predefined steps for Google
        Given I open url "https://google.com"
        Then I should see page title as "Google"
        Then element with xpath "//textarea[@name='q']" should be present
        When I type "Behavior Driven Development" into element with xpath "//textarea[@name='q']"
        Then I click on element using JavaScript with xpath "(//*[@value='Google Search'])[2]"
        Then I wait for element with xpath "//*[@id='gsr']" to be present
        Then element with xpath "//*[@id='gsr']" should contain text "Cucumber"

#    @predefined2
#    Scenario: Steps for Yandex
#        Given I open url "https://yandex.com/"
#        Then I should see page title as "Yandex"
#        Then element with xpath "//input[@id='text']" should be present
#        When I type "Behavior Driven Development" into element with xpath "//input[@id='text']"
#        And I click on element with xpath "//button[type='submit']"
#        And I wait for element with xpath "//ul[@id='search-result']" to be present
#        Then element with xpath "//ul[@id='search-result']" should contain text "BDD"

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
        Then element with xpath "//input[@id='ybar-sbq']" should be present
        When I type "Java" into element with xpath "//input[@id='ybar-sbq']"
        And I click on element using JavaScript with xpath "//button[@id='ybar-search']"
        Then I wait for element with xpath "//div[@id='main']" to be present
        Then element with xpath "//div[@id='main']" should contain text "Java"