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

    @FindBy(xpath = "(//a[@data-gtm-subsection='stamps'])[1]")
    private WebElement stampsButton;

    @FindBy(xpath = "(//a[normalize-space()='Order Now'])[1]")
    private WebElement orderNow;
    @FindBy(xpath = "(//span[normalize-space()='Free Shipping Supplies'])[1]")
    private WebElement closeSupplies;
    @FindBy(xpath = "//span[normalize-space()='ReadyPost Packaging']")
    private WebElement closePackaging;
//    @FindBy(xpath = "(//a[@class='facet-toggle'][normalize-space()='Show More'])[1]")
//    private WebElement openMore;
    @FindBy(xpath = "//label[@class='checkbox-label'][contains(.,'Forms and Labels ')]")
    private WebElement formsAndLabels;
    @FindBy(xpath = "//label[contains(@for,'checkbox-type-Mail Service-Priority Mail Express')]")
    private WebElement priorityMailExpress;
    @FindBy(xpath = "//h4[@class='item-title'][contains(.,'Price')]")
    private WebElement price;
    @FindBy(xpath = "//label[contains(@for,'checkbox-type-Price-$0 to $5')]")
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

        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", trackButton);
        trackButton.click();
    }

    public void clickStamps() {

        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", stampsButton);
        stampsButton.click();
    }

    public void clickBoxes() {

        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", orderNow);
        orderNow.click();
        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", closeSupplies);
        closeSupplies.click();
        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", closePackaging);
        closePackaging.click();
        getExecutor()
            .executeScript("window.scrollBy(0,250)");
        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", price);
        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", lowerPrice);
        getExecutor()
            .executeScript("arguments[0].click();", lowerPrice);
        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", formsAndLabels);
        formsAndLabels.click();
//        openMore.click();
        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", priorityMailExpress);
        priorityMailExpress.click();
    }

    public void clickMenuItem(String menu) {

        getDriver()
            .findElement(By.xpath("//ul[@role='menubar']//a[text()='" + menu + "']"))
            .click();
    }
}
