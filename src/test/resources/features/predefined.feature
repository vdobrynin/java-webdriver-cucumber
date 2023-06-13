@predefined
Feature: Smoke steps

  @predefined1
  Scenario: Predefined steps for Google
    Given I open url "https://google.com"
    Then I should see page title as "Google"
    Then element with xpath "//textarea[@name='q']" should be present
    When I type "Behavior Driven Development" into element with xpath "//input[@name='q']"
    Then I click on element using JavaScript with xpath "(//textarea[@value='Google Search'])[2]"
    Then I wait for element with xpath "//*[@id='gsr']" to be present
    Then element with xpath "//*[@id='gsr']" should contain text "Cucumber"

  