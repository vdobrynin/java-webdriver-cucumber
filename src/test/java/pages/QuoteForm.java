package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static support.TestContext.getExecutor;

public class QuoteForm extends Page {

    public QuoteForm() {

        setUrl("https://skryabin.com/market/quote.html");
    }

    @FindBy(xpath = "//input[@name='username']")
    private WebElement username;
    @FindBy(xpath = "//input[@name='email']")
    private WebElement email;
    @FindBy(xpath = "//input[@id='password']")
    private WebElement password;
    @FindBy(xpath = "//input[@id='confirmPassword']")
    private WebElement confirmPassword;

    @FindBy(xpath = "//input[@id='name']")
    private WebElement name;

                                                // --> Name dialog - start -
    @FindBy(xpath = "//input[@id='firstName']")
    private WebElement firstName;
    @FindBy(xpath = "//input[@id='middleName']")
    private WebElement middleName;
    @FindBy(xpath = "//input[@id='lastName']")
    private WebElement lastName;
    @FindBy(xpath = "//span[text()='Save']")
    private WebElement saveButton;
                                                // --> Name dialog - end -

    @FindBy(xpath = "//input[@name='agreedToPrivacyPolicy']")
    private WebElement privacy;
    @FindBy(xpath = "//button[@id='formSubmit']")
    private WebElement submitButton;

    public void clickSubmit() {
        //        submitButton.click();                                                     //--> first solution will not work
        getExecutor().executeScript("arguments[0].click();", submitButton);           // see above like PrivacyPolicy
    }

    public void clickPrivacy() {
        //        privacy.click();                                                    //--> first solution will not work
        getExecutor().executeScript("arguments[0].click();", privacy);          // this is better to use JavaScript
    }

    public void fillName(String firstName, String lastName) {

        name.click();
        this.firstName.sendKeys(firstName);
        this.lastName.sendKeys(lastName);
        saveButton.click();
    }

    public void fillName(String firstName, String middleName, String lastName) {

        name.click();
        this.firstName.sendKeys(firstName);
        this.middleName.sendKeys(middleName);
        this.lastName.sendKeys(lastName);
        saveButton.click();
    }

    public void fillUsername(String value) {
        username.sendKeys(value);
    }

    public void fillEmail(String value) {
        email.sendKeys(value);
    }

    public void fillPassword(String value) {
        password.sendKeys(value);
    }

    public void fillConfirmPassword(String value) { confirmPassword.sendKeys(value); }

    public void fillOutRequiredFields() { }

}
