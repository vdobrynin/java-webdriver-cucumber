package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static support.TestContext.*;

public class Usps extends Page {

    public Usps() {
        setUrl("https://usps.com/");
    }

    @FindBy(xpath = "(//a[@data-gtm-subsection='stamps'])[1]")
//    @FindBy(xpath = "(//a[@class='button--primary'][normalize-space()='Buy Stamps'])[1]")
    private WebElement stampsButton;

    @FindBy(xpath = "(//a[normalize-space()='Order Now'])[1]")
    private WebElement orderNow;
    @FindBy(xpath = "(//span[normalize-space()='Free Shipping Supplies'])[1]")
    private WebElement closeSupplies;
    @FindBy(xpath = "//span[normalize-space()='ReadyPost Packaging']")
    private WebElement closePackaging;
//    @FindBy(xpath = "(//a[@class='facet-toggle'][normalize-space()='Show More'])[1]")
//    private WebElement openMore;
    @FindBy(xpath = "//label[contains(@for,'checkbox-type-Mail Service-Priority Mail Express')]")
    private WebElement priorityMailExpress;
//    @FindBy(xpath = "//label[@class='checkbox-label'][contains(.,'Forms and Labels ')]")
//    private WebElement formsAndLabels;
    @FindBy(xpath = "//label[@class='checkbox-label'][contains(.,'$0 to $5 ')]")
    private WebElement lowerPrice;

    @FindBy(xpath = "(//a[@data-gtm-label='click-n-ship-link'])[1]")
    private WebElement labelsButton;
    @FindBy(xpath = "//p[contains(.,'Track a Package')]")
    private WebElement trackButton;

    public void clickLabels() {
        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", labelsButton);
        labelsButton.click();
    }

    public void clickTracking() {

        trackButton.click();
    }

    public void clickStamps() {

        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", stampsButton);
        getWait()
            .until(ExpectedConditions.elementToBeClickable(stampsButton));
        stampsButton
            .click();
    }

    public void clickBoxes() {

        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", orderNow);
        getWait()
            .until(ExpectedConditions.elementToBeClickable(orderNow));
        orderNow
            .click();
        closeSupplies
            .click();
        closePackaging.click();
//        openMore.click();
        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", priorityMailExpress);
        priorityMailExpress
            .click();
//        formsAndLabels.click();
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
