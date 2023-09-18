package definitions;

import cucumber.api.java.en.*;
//import io.cucumber.java.en.*;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static support.TestContext.getDriver;

public class ConverterStepDefs {
    @When("I click on {string}")
    public void iClickOn(String tab) {
        //getDriver().findElement(By.xpath("//div[@id='menu']//a[text()='" + tab + "']")).click();
        getDriver().findElement(By.xpath("//a[contains(@href,'" + tab + "')]")).click();
    }

    @And("I set {string} to {string}")
    public void iSetTo(String from, String to) {
        getDriver().findElement(By.xpath("//select[@id='calFrom']//option[text()='" + from + "']")).click();
        getDriver().findElement(By.xpath("//select[@id='calTo']//option[text()='" + to + "']")).click();
    }

    @Then("I enter into From field {string} and verify {string} result")
    public void iEnterIntoFromFieldAndVerifyResult(String fromValue, String toValue) {
        getDriver().findElement(By.xpath("//input[@name='fromVal']")).sendKeys(fromValue);
        String result = getDriver().findElement(By.xpath( "//input[@name='toVal']")).getAttribute("value");
        assertThat(result).contains(toValue);
        System.out.println(result);
    }

    @When("I click on {string} button")
    public void iClickOnButton(String tab) {
        getDriver().findElement(By.xpath("//a[contains(text(),'" + tab + "')]")).click();
    }

    @And("I verify {string}")
    public void iVerify(String tab_content) throws InterruptedException {
        Thread.sleep(3000);
        String result = getDriver().findElement(By.xpath("//*[contains(text(),'" + tab_content + "')]")).getText();
        assertThat(result).contains(tab_content);
    }
}
