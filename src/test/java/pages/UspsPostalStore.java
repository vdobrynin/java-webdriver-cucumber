package pages;

import static support.TestContext.getExecutor;

import java.text.ParseException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UspsPostalStore extends Page {

  @FindBy(
    xpath = "(//a[@class='dropdown-item'][contains(.,'Price (Low-High)')])[1]"
  )
  private WebElement sortBy;

  @FindBy(xpath = "(//div[@class='results-product-info'])[1]")
  private WebElement firstFoundItem;

  @FindBy(xpath = "//div[@class='results-product-info']")
  private List<WebElement> foundItems;

  @FindBy(xpath = "//label[contains(@class,'checkbox-label')]")
  private List<WebElement> leftFilters;

  @FindBy(xpath = "//input[@id='store-search']")
  private WebElement searchInput;

  @FindBy(xpath = "//input[@id='store-search-btn']")
  private WebElement searchButton;

  public void searchFor(String search) {
    searchInput.sendKeys(search);
    searchButton.click();
  }

  public void selectSortBy(String text) {
    sortBy.isSelected();
    sortBy.click();
    //        new Select(sortBy).selectByVisibleText(text);     // i can't select anymore text, I have to start using click. 10/25/2023 review.
  }

  public String getFirstFoundItem() {
    return firstFoundItem.getText();
  }

  //                                        //--> Lecture #14 changes
  public String getLeftFilters() {
    StringBuilder filters = new StringBuilder();
    for (WebElement filter : leftFilters) {
      if (filter.isDisplayed()) {
        filters.append(filter.getText()).append(" ");
      }
    }
    return filters.toString();
  }

  public boolean isCheapestItem(String name) throws ParseException {
    for (WebElement item : foundItems) {
      if (item.getText().contains(name)) {
        System.out.println(item.getText());
        By priceSelector = By.xpath(
          "//div[@class='results-product-preview-price']"
        );
        String itemPrice = item.findElement(priceSelector).getText();
        String firstFoundPrice = firstFoundItem
          .findElement(priceSelector)
          .getText();
        //                                                            //---> Deals with currency for different country
        /*               //Locale locale = new Locale("en", "US");
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "US"););
                double itemPriceNumber = formatter.parse(itemPrice).doubleValue();
                double firstPriceNumber = formatter.parse(firstFoundPrice).doubleValue();
                //formatter.format(itemPriceNumber);                  // format back
                return itemPriceNumber <= firstPriceNumber;
*/
        return itemPrice.equals(firstFoundPrice);
        //                return firstFoundPrice
        //                    .equals(itemPrice);
      }
    }
    return false;
  }
}
