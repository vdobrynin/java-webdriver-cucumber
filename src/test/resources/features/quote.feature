@quote
Feature: Quote form

  @quote1
  Scenario: Quote responsive design
    Given I open url "https://skryabin.com/market/quote.html"
    When I resize window to 1280 and 1024
    Then element with xpath "//b[@id='location']" should be displayed
    Then element with xpath "//b[@id='currentDate']" should be displayed
    Then element with xpath "//b[@id='currentTime']" should be displayed
    When I resize window to 1000 and 1024
    Then element with xpath "//b[@id='location']" should be displayed
    Then element with xpath "//b[@id='currentDate']" should be displayed
    Then element with xpath "//b[@id='currentTime']" should be displayed
    When I resize window to 600 and 1024
    Then element with xpath "//b[@id='location']" should not be displayed
    Then element with xpath "//b[@id='currentDate']" should not be displayed
    Then element with xpath "//b[@id='currentTime']" should not be displayed
    When I resize window to 1280 and 1024

  @quote2 @smoke
  Scenario: Username field
    Given I open url "https://skryabin.com/market/quote.html"
    When I type "a" into element with xpath "//input[@name='username']"
    And I click on element using JavaScript with xpath "//button[@id='formSubmit']"
    Then element with xpath "//label[@id='username-error']" should be displayed
    When I type "b" into element with xpath "//input[@name='username']"
    Then element with xpath "//label[@id='username-error']" should not be displayed

  @quote3 @smoke
  Scenario: Email field
    Given I open url "https://skryabin.com/market/quote.html"
    When I type "slavaskryabin.com" into element with xpath "//input[@name='email']"
    And I click on element using JavaScript with xpath "//button[@id='formSubmit']"
    And I clear element with xpath "//input[@name='email']"
    When I type "slava@skryabin.com" into element with xpath "//input[@name='email']"
    Then element with xpath "//label[@id='email-error']" should not be displayed

  @quote4
  Scenario: Password field
    Given I open url "https://skryabin.com/market/quote.html"
    Then element with xpath "//input[@id='password']" should have attribute "value" as ""
    Then element with xpath "//input[@id='confirmPassword']" should be disabled
    When I type "password" into element with xpath "//input[@id='password']"
    Then element with xpath "//input[@id='confirmPassword']" should be enabled

  @quote5
  Scenario: Name
    Given I open url "https://skryabin.com/market/quote.html"
    When I click on element with xpath "//input[@id='name']"
    Then element with xpath "//div[@aria-describedby='nameDialog']" should be displayed
    And I type "Slava" into element with xpath "//input[@id='firstName']"
    And I type "Skryabin" into element with xpath "//input[@id='lastName']"
    When I click on element with xpath "//span[text()='Save']"
    Then element with xpath "//input[@id='name']" should have attribute "value" as "Slava Skryabin"

  @quote6
  Scenario: Accepting Privacy Policy
    Given I open url "https://skryabin.com/market/quote.html"
    Then I type "vskryabin" into element with xpath "//input[@name='username']"
    And I type "slava@skryabin.com" into element with xpath "//input[@name='email']"
    And I type "welcome" into element with xpath "//input[@id='password']"
    And I type "welcome" into element with xpath "//input[@id='confirmPassword']"
    And I click on element with xpath "//input[@id='name']"
    And I type "Slava" into element with xpath "//input[@id='firstName']"
    And I type "Skryabin" into element with xpath "//input[@id='lastName']"
    And I click on element with xpath "//div[@class='ui-dialog-buttonset']/..//span[contains(text(),'Save')]"
    And I click on element using JavaScript with xpath "//button[@id='formSubmit']"
    Then element with xpath "//label[@id='agreedToPrivacyPolicy-error']" should be displayed

  @quote7
  Scenario: Non-required fields
    Given I open url "https://skryabin.com/market/quote.html"
    And I type "1234567890" into element with xpath "//input[@name='phone']"
    And I click on element with xpath "//select[@name='countryOfOrigin']/option[contains(text(),'Monaco')]"
    And I click on element with xpath "//div[@id='quotePageForm']//../label[contains(text(),'Gender')]"
    And I click on element with xpath "//input[@name='allowedToContact']"
    And I type "4970 El Camino Real, Los Altos, CA 94022" into element with xpath "//textarea[@id='address']"
    And I click on element with xpath "//select[@name='carMake']//option[contains(text(),'BMW')]"
    And I click on element with xpath "//button[@id='thirdPartyButton']"
    And I accept alert
    And I click on element with xpath "//input[@id='dateOfBirth']"
    And I click on element with xpath "//select[@class='ui-datepicker-month']/option[@value='3']"
    And I click on element with xpath "//select[@class='ui-datepicker-month']/option[@value='10']"
    And I click on element with xpath "//select[@class='ui-datepicker-year']/option[@value='1975']"

  @quote8
  Scenario: Submit the form and verify the data
    Given I open url "https://skryabin.com/market/quote.html"
    Then I type "vskryabin" into element with xpath "//input[@name='username']"
    And I type "slava@skryabin.com" into element with xpath "//input[@name='email']"
    And I type "welcome" into element with xpath "//input[@id='password']"
    And I type "welcome" into element with xpath "//input[@id='confirmPassword']"
    And I click on element with xpath "//input[@id='name']"
    And I type "Slava" into element with xpath "//input[@id='firstName']"
    And I type "Skryabin" into element with xpath "//input[@id='lastName']"
    And I click on element with xpath "//div[@class='ui-dialog-buttonset']/..//span[contains(text(),'Save')]"
    And I type "1234567890" into element with xpath "//input[@name='phone']"
    And I click on element with xpath "//input[@id='dateOfBirth']"
    And I click on element with xpath "//select[@class='ui-datepicker-month']/option[@value='3']"
    And I click on element with xpath "//select[@class='ui-datepicker-month']/option[@value='10']"
    And I click on element with xpath "//select[@class='ui-datepicker-year']/option[@value='1975']"
    And I click on element with xpath "//select[@name='countryOfOrigin']/option[contains(text(),'Monaco')]"
    And I click on element with xpath "//div[@id='quotePageForm']//../label[contains(text(),'Gender')]"
    And I click on element using JavaScript with xpath "//input[@name='allowedToContact']"
    And I type "4970 El Camino Real, Los Altos, CA 94022" into element with xpath "//textarea[@id='address']"
    And I click on element with xpath "//select[@name='carMake']//option[contains(text(),'BMW')]"
    And I click on element with xpath "//input[@name='agreedToPrivacyPolicy']"
    And I click on element using JavaScript with xpath "//button[@id='formSubmit']"
    And I verify required fields

  @quote9
  Scenario: Datepicker
    Given I open url "https://skryabin.com/market/quote.html"
    When I click on element with xpath "//input[@id='dateOfBirth']"
    And I click on element with xpath "//select[@data-handler='selectMonth']/option[11]"
    And I click on element with xpath "//select[@data-handler='selectYear']/option[@value='1981']"
    And I click on element with xpath "//td[@data-handler='selectDay']/a[text()='17']"
    And element with xpath "//input[@id='dateOfBirth']" should have attribute "value" as "11/17/1981"




