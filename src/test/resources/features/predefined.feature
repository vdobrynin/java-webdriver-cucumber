@predefined
Feature: Smoke steps

  @predefined1
  Scenario: Predefined steps for Google page
    Given I open url "https://google.com"
    Then I should see page title as "Google"
    Then element with xpath "//input[@name='q']" should be present
    When I type "Java" into element with xpath "//input[@name='q']"
    Then I click on element using JavaScript with xpath "//*[@name='btnK']"
    Then I wait for element with xpath "//*[@id='ires']" to be present
    Then element with xpath "//*[@id='ires']" should contain text "Java"

  @predefined2
  Scenario: Predefined steps for Bing
    Given I open url "http://www.bing.com/"
    Then element with xpath "//*[@id='sb_form_q']" should be present
    When I type "Java" into element with xpath "//*[@id='sb_form_q']"
    And I click on element using JavaScript with xpath "//*[@name='go']"
    Then I wait for element with xpath "//*[@id='b_results']" to be present
    Then element with xpath "//*[@id='b_results']" should contain text "Java"