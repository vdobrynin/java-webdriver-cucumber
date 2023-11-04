package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static support.TestContext.fluentWait;
import static support.TestContext.getExecutor;

public class UspsPostalStore extends Page { //    @FindBy(xpath = "//div[@class='result-products-holder']")

    @FindBy(xpath = "(//a[@class='dropdown-item'][contains(.,'Price (Low-High)')])[1]")
    private WebElement sortBy;
    @FindBy(xpath = "(//div[@class='results-product-info'])[1]")
    private WebElement firstFoundItem;
    @FindBy(xpath = "//div[@class='results-product-info']")
    private List<WebElement> foundItems;

    @FindBy(xpath = "//label[@class='checkbox-label'][contains(.,'$0 to $5 ')]")
    private WebElement leftFilterBar;
    @FindBy(xpath = "//input[@id='store-search']")
    private WebElement searchInput;
    @FindBy(xpath = "//input[@id='store-search-btn']")
    private WebElement searchButton;

    public void searchFor(String search) {

        searchInput.sendKeys(search);
        searchButton.click();
    }

    public void selectSortBy(String text) {

        sortBy.isSelected();                    // i can't select anymore text, i have to start using click. 10/25/2023 review.
        sortBy.click();
//        new Select(sortBy).selectByVisibleText(text);
    }

    public String getFirstFoundItem() {

        return firstFoundItem.getText();
    }

    //                                        //--> Lecture #14 changes
    public String getLeftFilters() {

        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", leftFilterBar);
        fluentWait.until(ExpectedConditions.visibilityOf(leftFilterBar));
        return leftFilterBar
            .getText();
    }

    public boolean isCheapestItem(String name) {

        for (WebElement item : foundItems) {
            if (item.getText().contains(name)) {
                System.out.println(item.getText());
                By priceSelector = By.xpath("//div[@class='results-product-preview-price']");
                String itemPrice = item
                    .findElement(priceSelector)
                    .getText();
                String firstFoundPrice = firstFoundItem
                    .findElement(priceSelector)
                    .getText();
//                                                            //---> Deals with currency from different country
/*
                Locale locale = new Locale("en", "US");
                NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);

                double itemPriceNumber = formatter.parse(itemPrice).doubleValue();
                double firstPriceNumber = formatter.parse(firstFoundPrice).doubleValue();

                formatter.format(itemPriceNumber);  // format back
                return itemPriceNumber <= firstPriceNumber;
*/
//                return itemPrice
//                    .equals(firstFoundPrice);
                return firstFoundPrice
                    .equals(itemPrice);
            }
        }
        return false;
    }
}

