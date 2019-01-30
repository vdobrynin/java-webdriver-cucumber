@predefined
Feature: Smoke steps

  @predefined1
  Scenario: Predefined steps for Google
    Given I open url "https://google.com"
    Then I should see page title as "Google"
    Then element with xpath "//input[@name='q']" should be present
    When I type "Behavior Driven Development" into element with xpath "//input[@name='q']"
    Then I click on element using JavaScript with xpath "(//input[@value='Google Search'])[2]"
    Then I wait for element with xpath "//*[@id='ires']" to be present
    Then element with xpath "//*[@id='ires']" should contain text "Cucumber"

  @predefined2
  Scenario: Steps for Yandex
    Given I open url "https://yandex.com/"
    Then I should see page title as "Yandex"
    Then element with xpath "//*[@id='text']" should be present
    When I type "Behavior Driven Development" into element with xpath "//*[@id='text']"
    And I click on element with xpath "//*[@type='submit']"
    And I wait for element with xpath "//div[@class='content__left']" to be present
    Then element with xpath "//div[@class='content__left']" should contain text "Cucumber"

  @predefined3
  Scenario: Predefined steps for Bing
    Given I open url "http://www.bing.com/"
    Then element with xpath "//*[@id='sb_form_q']" should be present
    When I type "Behavior Driven Development" into element with xpath "//*[@id='sb_form_q']"
    And I click on element with xpath "//*[@name='go']"
    Then I wait for element with xpath "//*[@id='b_results']" to be present
    Then element with xpath "//*[@id='b_results']" should contain text "Cucumber"