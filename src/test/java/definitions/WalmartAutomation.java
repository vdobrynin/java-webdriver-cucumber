package definitions;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WalmartAutomation {
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(20);
    private static final String PROFILE_DIR = System
            .getProperty("user.home") + "/.walmart-chrome-profile";
    // Override with -Dwalmart.captcha.timeout=300 when more time is needed to
    // complete the "Robot or human?" challenge.
    private static final long CAPTCHA_TIMEOUT_SECONDS = Long
            .getLong("walmart.captcha.timeout", 60L);
    // Override with -Dwalmart.email=you@example.com to test a registered account.
    private static final String SIGN_IN_EMAIL = System
            .getProperty("walmart.email", "my-or-yours-email@example.com");

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", System
                .getProperty("user.dir") + "/src/test/resources/drivers/chromedriver");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--window-size=1920,1200");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--force-device-scale-factor=0.75");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--disable-features=VizDisplayCompositor");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
        // A clean session avoids stale bot-check state that Walmart stores in a
        // reused Chrome profile.
        chromeOptions.addArguments("--incognito");
        chromeOptions.addArguments("--user-data-dir=" + PROFILE_DIR);
        WebDriver driver = new ChromeDriver(chromeOptions);

        try {
            driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            driver.get("https://www.walmart.com/");
            if (!waitPastBotCheck(driver)) {
                return;
            }

            if (!clickFirstAddToCart(driver)) {
                System.out.println("No Add-to-cart button found on the home page; trying search.");
                driver.get("https://www.walmart.com/search?q=pens");
                if (!waitPastBotCheck(driver)) {
                    return;
                }
                if (!clickFirstAddToCart(driver)) {
                    System.out.println("No Add-to-cart button found; Walmart may be showing a different page.");
                    return;
                }
            }

            sleep(4000);
            if (!waitPastBotCheck(driver)) {
                return;
            }

            WebElement cartButton = waitForVisible(driver, By
                    .cssSelector("[data-automation-id='cart-button-header']"));
            String cartLabel = cartButton.getAttribute("aria-label");
            System.out.println("Cart status: " + cartLabel);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartButton);

            sleep(6000);
            if (!waitPastBotCheck(driver)) {
                return;
            }
            sleep(2000);

            if (!waitForCartToLoad(driver)) {
                System.out.println("Cart is still loading; refreshing the cart page.");
                driver.navigate().refresh();
                if (!waitForCartToLoad(driver)) {
                    System.out.println("Cart did not finish loading.");
                    return;
                }
            }

            WebElement checkoutButton = waitForFirstVisible(driver, By
                    .cssSelector("[data-automation-id='checkout']"), By
                    .cssSelector("button[data-automation-id='cart-checkout-button']"), By
                    .cssSelector("button[data-testid='cart-checkout-button']"), By
                    .xpath("//button[contains(normalize-space(),'Continue to checkout')]"));
            if (checkoutButton == null) {
                System.out.println("Checkout button was not found.");
                return;
            }
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkoutButton);
            sleep(6000);
            if (!waitPastBotCheck(driver)) {
                return;
            }

            WebElement emailInput = waitForFirstVisible(driver, By
                    .cssSelector("input[name='email']"), By
                    .cssSelector("input[type='email']"), By
                    .id("email"), By
                    .cssSelector("input[aria-label='Phone number or email (required)']"));
            if (emailInput == null) {
                System.out.println("Sign-in email field was not found; opening the sign-in page.");
                if (!openSignInPage(driver)) {
                    System.out.println("Sign-in page was not available.");
                    return;
                }
                if (!waitPastBotCheck(driver)) {
                    return;
                }
                emailInput = waitForFirstVisible(driver, By
                        .cssSelector("input[aria-label='Phone number or email (required)']"), By
                        .cssSelector("input[name='email']"), By
                        .cssSelector("input[type='email']"), By.id("email"));
            }
            if (emailInput == null) {
                System.out.println("Sign-in email field was not found.");
                return;
            }
            emailInput.sendKeys(SIGN_IN_EMAIL);

            WebElement continueButton = waitForFirstVisible(driver, By
                    .id("login-continue-button"), By
                    .xpath("//button[contains(normalize-space(),'Continue')]"));
            if (continueButton != null) {
                try {
                    continueButton.click();
                } catch (WebDriverException exception) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueButton);
                }
            }
            sleep(2000);
            if (!waitPastBotCheck(driver)) {
                return;
            }

            WebElement passwordInput = waitForFirstVisible(driver, By
                    .cssSelector("input[type='password']"), By
                    .cssSelector("input[name='password']"), By.id("password"));
            if (passwordInput == null || !passwordInput.isDisplayed()) {
                System.out.println("Password field was not displayed.");
                return;
            }
            System.out.println("Password field is displayed.");
        } catch (TimeoutException exception) {
            System.out.println("Walmart flow stopped because an expected element did not appear.");
        } catch (WebDriverException exception) {
            System.out.println("Walmart flow stopped because the browser session was interrupted.");
        } finally {
            try {
                driver.quit();
            } catch (WebDriverException ignored) {
                // The browser may already be closed after a failed captcha attempt.
            }
        }
    }

    private static boolean openSignInPage(WebDriver driver) {
        try {
            WebElement accountLink = findFirstDisplayed(driver, By
                    .cssSelector("a[link-identifier='Account']"), By
                    .cssSelector("a[href*='identity.walmart.com/account/login']"));
            if (accountLink == null) {
                return false;
            }
            try {
                accountLink.click();
            } catch (WebDriverException exception) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accountLink);
            }
            sleep(2000);
            WebElement signInLink = findFirstDisplayed(driver, By
                    .cssSelector("[data-testid='sign-in']"), By
                    .xpath("//a[contains(normalize-space(),'Sign in or create account')]"));
            if (signInLink == null) {
                return false;
            }
            try {
                signInLink.click();
            } catch (WebDriverException exception) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", signInLink);
            }
            return true;
        } catch (WebDriverException exception) {
            System.out.println("Could not open the sign-in page: " + exception.getMessage());
            return false;
        }
    }

    private static boolean clickFirstAddToCart(WebDriver driver) {
        for (int attempt = 1; attempt <= 6; attempt++) {
            WebElement addButton = findFirstDisplayed(driver, By
                    .cssSelector("button[data-automation-id='add-to-cart']"), By
                    .cssSelector("button[data-testid='add-to-cart']"), By
                    .xpath("//button[contains(@aria-label,'Add') and contains(translate(@aria-label,'CART','cart'),'cart')]"));
            if (addButton == null) {
                sleep(1000);
                continue;
            }
            try {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});", addButton);
                Thread.sleep(750);
                addButton = findFirstDisplayed(driver, By
                        .cssSelector("button[data-automation-id='add-to-cart']"), By
                        .cssSelector("button[data-testid='add-to-cart']"), By
                        .xpath("//button[contains(@aria-label,'Add') and contains(translate(@aria-label,'CART','cart'),'cart')]"));
                if (addButton == null) {
                    sleep(1000);
                    continue;
                }
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addButton);
                System.out.println("Added the first available product to the cart.");
                return true;
            } catch (WebDriverException exception) {
                sleep(1000);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    private static boolean closeCaptchaDialogIfPresent(WebDriver driver) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            if (!isBotCheckDisplayed(driver)) {
                return true;
            }
            WebElement closeButton = findFirstDisplayed(driver, By
                    .cssSelector("button[aria-label='Close dialog']"), By
                    .cssSelector("[data-dca-name='ItemUnnamedDialogCloseButton']"));
            if (closeButton == null) {
                sleep(1000);
                continue;
            }
            try {
                closeButton.click();
            } catch (WebDriverException exception) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeButton);
            }
            sleep(1500);
        }
        return !isBotCheckDisplayed(driver);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    private static boolean waitPastBotCheck(WebDriver driver) {
        if (!isBotCheckDisplayed(driver)) {
            return true;
        }

        if (closeCaptchaDialogIfPresent(driver)) {
            System.out.println("Dismissed the Walmart verification dialog.");
            return true;
        }

        System.out.println("Walmart requires human verification. Trying automatic completion first; if needed, solve the \"Robot or human?\" challenge in the opened Chrome window.");
        if (attemptCaptchaHold(driver)) {
            System.out.println("Human verification completed.");
            return true;
        }

        System.out.println("Automatic verification did not complete. Solve the challenge in the Chrome window within " + CAPTCHA_TIMEOUT_SECONDS + " seconds (bring Google Chrome to the front if it is hidden).");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(CAPTCHA_TIMEOUT_SECONDS))
                    .until(currentDriver -> !isBotCheckDisplayed(currentDriver));
            return true;
        } catch (TimeoutException exception) {
            System.out.println("Human verification was not completed; skipping the remaining Walmart flow. " + "Walmart may also be blocking automated traffic from this network.");
            return false;
        }
    }

    private static boolean attemptCaptchaHold(WebDriver driver) {
        try {
            WebElement captcha = driver.findElement(By.cssSelector("#px-captcha"));
            if (!captcha.isDisplayed()) {
                return false;
            }
            Actions actions = new Actions(driver);
            actions.moveToElement(captcha).clickAndHold(captcha).perform();
            try {
                Thread.sleep(12000);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return false;
            } finally {
                new Actions(driver).release().perform();
            }
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(currentDriver -> !isBotCheckDisplayed(currentDriver));
            return true;
        } catch (WebDriverException exception) {
            return false;
        }
    }

    private static boolean isBotCheckDisplayed(WebDriver driver) {
        if (driver.getCurrentUrl().contains("/blocked")) {
            return true;
        }
        for (WebElement captcha : driver.findElements(By.cssSelector("#px-captcha"))) {
            try {
                if (captcha.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // The page changed while the captcha was being checked.
                return true;
            }
        }
        return !driver
                .findElements(By.xpath("//*[contains(text(),'Robot or human?')]"))
                .isEmpty();
    }

    private static WebElement waitForVisible(WebDriver driver, By locator) {
        return new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private static WebElement waitForFirstVisible(WebDriver driver, By... locators) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
            wait.ignoring(StaleElementReferenceException.class);
            return wait.until(currentDriver -> findFirstDisplayed(currentDriver, locators));
        } catch (TimeoutException exception) {
            return null;
        }
    }

    private static boolean waitForCartToLoad(WebDriver driver) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.invisibilityOfElementLocated(
                            By.xpath("//span[@role='alert']//title[text()='Loading']")));
            return true;
        } catch (TimeoutException exception) {
            return false;
        }
    }

    private static WebElement findFirstDisplayed(WebDriver driver, By... locators) {
        for (By locator : locators) {
            for (WebElement element : driver.findElements(locator)) {
                try {
                    if (element.isDisplayed()) {
                        return element;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // The page re-rendered the element; keep looking.
                }
            }
        }
        return null;
    }
}
