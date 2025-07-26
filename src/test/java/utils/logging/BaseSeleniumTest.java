package utils.logging;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import selenium.core.DriverManager;

import java.net.MalformedURLException;

public class BaseSeleniumTest {

    @BeforeClass(alwaysRun = true)
    public void setup() throws MalformedURLException {
        LogFactory.init();
        DriverManager.startBrowser(); // sets ThreadLocal driver
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        DriverManager.quitDriver(); // closes the browser and cleans ThreadLocal
    }
}
