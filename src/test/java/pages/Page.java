package pages;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static support.TestContext.getDriver;
import static support.TestContext.getExecutor;

public class Page {

    private String url;

    public Page() {
        PageFactory.initElements(getDriver(), this);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void open() {
        getDriver().get(url);
    }

    public void waitForClickable(WebElement element) {

        new WebDriverWait(getDriver(), 5).until(ExpectedConditions.elementToBeClickable(element));
    }

    public void clickWithJS(WebElement element) {

        getExecutor().executeScript("argument[0].click", element);
    }

    public void click(WebElement element) {

        waitForClickable(element);

        try {
            element.click();

        } catch (ElementClickInterceptedException exception) {

            System.out.println(exception.getMessage());
            clickWithJS(element);
        }
    }
}
