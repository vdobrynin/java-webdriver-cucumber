package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CareersHeader extends Page {

    @FindBy(xpath = "//a[@href='/login']")
    private WebElement loginButton;
    @FindBy(xpath = "//a[@href='/recruit']")
    private WebElement recruitButton;
    @FindBy(xpath = "//span[@class='logout-box']/a")
    private WebElement loggedInUser;
    @FindBy(xpath = "//a[@href='/new_candidate']/button")
    private WebElement newCandidate;
    @FindBy(xpath = "(//a[contains(@href, '/positions/')])/h4") //h4[contains(text(),'Principal Automation Engineer')]
    private WebElement chosenPositionTitle;
    @FindBy(xpath = "(//a[contains(@href, '/candidates/')])")
    private WebElement candidate;

    public String getCandidateTitle() {

        click(chosenPositionTitle);
        return new Careers().getCandidateTitle();
    }

    public String getCandidateName() {
        return candidate.getText();
    }

    public CareersNewCandidate clickApply() {
        click(newCandidate);
        return new CareersNewCandidate();
    }

    public CareersRecruit clickRecruit() {
        click(recruitButton);
        return new CareersRecruit();
    }

    public CareersLogin clickLogin() {
        click(loginButton);
        return new CareersLogin();
    }

    public String getLoggedInUser() {

        return loggedInUser.getText();
    }
}
