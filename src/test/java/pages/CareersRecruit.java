package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import support.Loggable;

import java.util.List;

import static support.TestContext.getDriver;

public class CareersRecruit extends CareersHeader implements Loggable {

    @FindBy(xpath = "//a[@href='/new_position']/h4")
    private WebElement newPositionButton;

    @FindBy(xpath = "//h4[@class='card-title']")
    private List<WebElement> positionTitles;

    public CareersNewPosition clickNewPositionButton() {

        click(newPositionButton);
        return new CareersNewPosition();

    }

    public String getLastCreatedPositionTitle() throws InterruptedException {

        new WebDriverWait(getDriver(), 5).until(driver -> positionTitles.size() > 2);
        int size = positionTitles.size();

//    System.out.println("\nTotal position: " + size);
        getLogger().info("\nTotal position: " + size);

        WebElement lastPositionTitle = positionTitles.get(size - 1);
        return lastPositionTitle.getText();
    }
}
