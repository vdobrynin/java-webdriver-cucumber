package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import java.util.Map;

public class CareersLogin extends CareersHeader {

    @FindBy(xpath = "//label[@for='loginUsername']/../input")
    private WebElement username;
    @FindBy(xpath = "//label[@for='loginPassword']/../input")
    private WebElement password;
    @FindBy(xpath = "//button[@id='loginButton']")
    private WebElement submit;

    public CareersLogin fillUsername(String username) {

        this.username.sendKeys(username);
        return this;
    }

    public CareersLogin fillPassword(String password) {

        this.password.sendKeys(password);
        return this;
    }

    public Careers clickSubmit() {

        click(submit);
        return new Careers();
    }

    public Careers login(String username, String password) {            // because same name as variable we'll using "this"

        this.username.sendKeys(username);
        this.password.sendKeys(password);
        click(submit);
        return new Careers();
    }

    public Careers login(Map<String, String> user) {            // polymorphism same method with different arguments

        return login(user.get("username"), user.get("password"));
    }
}
