package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class UspsPostalStore extends Page {

  @FindBy(xpath = "(//a[@class='dropdown-item'][normalize-space()='Price (Low-High)'])[1]")
  @CacheLookup
  private WebElement sortByPrice;

  @FindBy(xpath = "(//div[@class='results-product-info'])[1]")
  private WebElement firstFoundItem;
  @FindBy(xpath = "//div[@class='results-product-info']")
  private List<WebElement> foundItems;

  @FindBy(xpath = "//div[@class='result-checkbox-filter-by-holder']")
  private WebElement leftFilterBar;
  @FindBy(xpath = "//input[@id='store-search']")
  private WebElement searchInput;
  @FindBy(xpath = "//input[@id='store-search-btn']")
  private WebElement searchButton;

  public void searchFor(String search) {
    searchInput.sendKeys(search);
    searchButton.click();
  }

  public void selectSortByPrice(String text) {
    new Select(sortByPrice).selectByVisibleText(text);
  }

  public String getFirstFoundItem() {
    return firstFoundItem.getText();
  }
                                        //--> Lecture #14 changes
  public String getLeftFilters() {
    return leftFilterBar.getText();
  }

  public boolean isCheapestItem(String name) {

    for (WebElement item : foundItems) {
      if (item.getText().contains(name)) {
        By priceSelector = By.xpath("//div[@class='results-product-preview-price']");
        String itemPrice = item.findElement(priceSelector).getText();
        String firstFoundPrice = firstFoundItem.findElement(priceSelector).getText();
        //--> Deals with currency from different country
        ////Locale locale = new Locale("en", "US");
        //NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);

        //double itemPriceNumber = formatter.parse(itemPrice).doubleValue();
        //double firstPriceNumber = formatter.parse(firstFoundPrice).doubleValue();

        ////formatter.format(itemPriceNumber);  // format back
        ////return itemPriceNumber <= firstPriceNumber;
        return itemPrice.equals(firstFoundPrice);
        //return firstFoundPrice.equals(itemPrice);
      }
    }
    return false;
  }
}

