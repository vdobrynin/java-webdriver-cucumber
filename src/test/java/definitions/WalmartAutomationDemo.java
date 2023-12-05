package definitions;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.time.Duration;

public class WalmartAutomationDemo {

    public static void main(String[] args) throws InterruptedException {

        // Set the path to your ChromeDriver executable
        System.setProperty("webdriver.chrome.driver",
            (System.getProperty("user.dir") + "/src/test/resources/drivers/chromedriver"));
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--force-device-scale-factor=0.75");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--disable-features=VizDisplayCompositor");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
        chromeOptions.addExtensions(new File(System
            .getProperty("user.dir") + "/src/test/resources/config/SelectorsHub 5.1.5.0.crx"));

        // Open Chrome Browser
        WebDriver driver = new ChromeDriver();
        driver.manage()
            .deleteAllCookies();
        driver.manage()
            .timeouts()
            .pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage()
            .timeouts()
            .implicitlyWait(Duration.ofSeconds(5));

        // Navigate to Walmart.com
        driver.get("https://www.walmart.com/");
//        Thread.sleep(7000);

        // Bypassing Captcha
        boolean isPresent;            // if captcha present
        try {
            Actions action = new Actions(driver);
            WebElement element = driver.findElement(By.cssSelector("#px-captcha"));
            Thread.sleep(12000);
            isPresent = element.isDisplayed();
            action = new Actions(driver);
//            Actions click = new Actions(driver);
            action.moveToElement(element)
                .clickAndHold(element);
            action.perform();
            Thread.sleep(12000);
            action.release(element);
            action.perform();
            Thread.sleep(20);
            action.release(element);
            Thread.sleep(12000);
        } catch (NoSuchElementException e) {
            isPresent = false;
        }

        // Perform search
        WebElement searchBox = driver
            .findElement(By.xpath("//input[contains(@data-automation-id,'header-input-search')]"));
        searchBox.sendKeys("pens");
        searchBox.submit();
//        Thread.sleep(5000);
//        Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(driver())
//            .withTimeout(Duration.ofSeconds(30))
//            .pollingEvery(Duration.ofSeconds(5))
//            .ignoring(NoSuchElementException.class);
//        return fluentWait;

        // Bypassing Captcha 2nd time
        boolean isPresent2;            // if captcha present2
        try {
            Actions action2 = new Actions(driver);
            WebElement element2 = driver.findElement(By.cssSelector("#px-captcha"));
            Thread.sleep(5000);
            isPresent2 = element2.isDisplayed();
            action2 = new Actions(driver);
//            Actions actionClick2 = new Actions(driver);
            action2.moveToElement(element2)
                .clickAndHold(element2);
            action2.perform();
            Thread.sleep(12000);
            action2.release(element2);
            action2.perform();
            Thread.sleep(20);
            action2.release(element2);
            Thread.sleep(12000);
        } catch (NoSuchElementException e) {
            isPresent2 = false;
        }

        // Click on the first search result with 'Add' button
        WebElement firstProduct = driver
            .findElement(By.xpath("(//span[@class='mr2'][contains(.,'Add')])[5]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", firstProduct);
        firstProduct
            .click();

        // Wait for the item to be added to the cart
        Thread.sleep(10000);

        // Navigate to the cart
        WebElement cartIcon = driver
            .findElement(By.cssSelector("[data-tl-id='header-cart-icon']"));
        cartIcon
            .click();

        // Validate if 1 item is added to the cart
        WebElement cartCount = driver
            .findElement(By.cssSelector(".cart-count"));
        assert cartCount
            .getText()
            .equals("1") : "Item count in the cart is not 1";

        // Click on "Continue to Checkout" button
        WebElement checkoutButton = driver
            .findElement(By.cssSelector("[data-tl-id='Cart-Cta-Primary']"));
        checkoutButton
            .click();

        // Enter an email and click continue
        WebElement emailInput = driver
            .findElement(By.cssSelector("[data-tl-id='SignIn-Email']"));
        emailInput
            .sendKeys("my-or yours-email@example.com");

        WebElement continueButton = driver
            .findElement(By.cssSelector("[data-tl-id='SignIn-SubmitButton']"));
        continueButton
            .click();

        // Assert that you are requested for a password
        WebElement passwordInput = driver
            .findElement(By.cssSelector("[data-tl-id='SignIn-Password']"));
        assert passwordInput
            .isDisplayed() : "Password input is not displayed";
/*
        if (passwordInput.isDisplayed()) {
            // This means the user already has an account, so sign in
            // You can add your sign-in logic here

            // For now, we'll print a message
            System.out.println("User already has an account. Signing in...");

            // Navigate to the wallet page
            navigateToWalletPage(driver);

            // Assert if the wallet is set up for this account
            assertWalletIsSetUp(driver);

        } else {
            // This means the user doesn't have an account, so add a new account
            // You can add your account creation logic here

            // For now, we'll print a message
            System.out.println("User doesn't have an account. Creating a new account...");

            // Navigate to the wallet page
            navigateToWalletPage(driver);

            // Assert if the wallet is set up for this new account
            assertWalletIsSetUp(driver);
*/
        // Close the browser.
//        driver.quit();
//        driver.close();
        driver.quit();
    }

    private static WebDriver driver() {
        return null;
    }
}
