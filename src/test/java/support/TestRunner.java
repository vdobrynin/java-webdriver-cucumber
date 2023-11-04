package support;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(

    plugin = {"pretty", "html:target/cucumber/report.html", "json:target/cucumber/report.json"},
    features = {"src/test/resources/features"},
    glue = {"definitions", "support"},
    tags = "@dice"//"@usps_object"//"@predefined" // same as VM option -Dcucumber.options="--tags @predefined"
)
public class TestRunner {

    @BeforeClass
    public static void setup() { System.out.println("BeforeAll"); }

    @AfterClass
    public static void teardown() {
        System.out.println("AfterAll");
    }
}
