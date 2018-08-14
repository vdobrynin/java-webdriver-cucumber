@predefined
Feature: Smoke steps

  @predefined1
  Scenario: Predefined steps for Google
    Given I open url "https://google.com"
    Then I should see page title as "Google"
    Then element with xpath "//input[@name='q']" should be present
    When I type "Java" into element with xpath "//input[@name='q']"
    Then I click on element using JavaScript with xpath "//*[@name='btnK']"
    Then I wait for element with xpath "//*[@id='ires']" to be present
    Then element with xpath "//*[@id='ires']" should contain text "Java"


  @predefined2
  Scenario: Predefined steps for Yahoo
    Given I open url "https://yahoo.com"
    Then I should see page title as "Yahoo"
    Then element with xpath "//input[@name='q']" should be present
    When I type "Java" into element with xpath "//input[@name='q']"
    Then I click on element using JavaScript with xpath "//*[@name='btnK']"
    Then I wait for element with xpath "//*[@id='ires']" to be present
    Then element with xpath "//*[@id='ires']" should contain text "Java"


  @predefined1
  Scenario: Predefined steps for Bing
    Given I open url "https://bing.com"
    Then I should see page title as "Bing"
    Then element with xpath "//input[@name='q']" should be present
    When I type "Java" into element with xpath "//input[@name='q']"
    Then I click on element using JavaScript with xpath "//*[@name='btnK']"
    Then I wait for element with xpath "//*[@id='ires']" to be present
    Then element with xpath "//*[@id='ires']" should contain text "Java"