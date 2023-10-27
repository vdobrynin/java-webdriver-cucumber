package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static support.TestContext.getDriver;
import static support.TestContext.getExecutor;

public class Usps extends Page {

    public Usps() {
        setUrl("https://usps.com/");
    }

    @FindBy(xpath = "(//a[@class='button--primary'][normalize-space()='Buy Stamps'])[1]")
    private WebElement stampsButton;

    @FindBy(xpath = "(//a[normalize-space()='Order Now'])[1]")
    private WebElement orderNow;
    @FindBy(xpath = "(//span[normalize-space()='Free Shipping Supplies'])[1]")
    private WebElement closeSupplies;
    @FindBy(xpath = "//span[normalize-space()='ReadyPost Packaging']")
    private WebElement closePackaging;
    @FindBy(xpath = "(//a[@class='facet-toggle'][normalize-space()='Show More'])[1]")
    private WebElement openMore;
    @FindBy(xpath = "(//label[@class='checkbox-label'][contains(.,'Priority Mail Express ')])[1]")
    private WebElement priorityMailExpress;
    @FindBy(xpath = "//label[@class='checkbox-label'][contains(.,'Forms and Labels ')]")
    private WebElement formsAndLabels;
    @FindBy(xpath = "//label[@class='checkbox-label'][contains(.,'$0 to $5 ')]")
    private WebElement lowerPrice;

    @FindBy(xpath = "//a[@id='quickMenuButtonShip']")
    private WebElement labelsButton;
    @FindBy(xpath = "//input[@id='trackButton']")
    private WebElement trackButton;

    public void clickLabels() {

        labelsButton
            .click();
    }

    public void clickTracking() {

        trackButton
            .click();
    }

    public void clickStamps() {

        stampsButton
            .click();
    }

    public void clickBoxes() {

        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", orderNow);
        orderNow
            .click();
        closeSupplies
            .click();
        closePackaging
            .click();
        openMore
            .click();
        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", priorityMailExpress);
        priorityMailExpress
            .click();
        formsAndLabels
            .click();
        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", lowerPrice);
        getExecutor()
            .executeScript("arguments[0].click();", lowerPrice);
    }

    public void clickMenuItem(String menu) {

        getDriver()
            .findElement(By.xpath("//a[text()='" + menu + "']"))
            .click();
    }
}
