package testsuite.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import testsuite.utils.ConfigLoader;
import testsuite.utils.DriverFactory;

public class BaseTest {
//    Driver and setup for each test

    protected WebDriver driver;

    @BeforeMethod
    public void setUp(){
        driver = DriverFactory.getDriver("chrome");
//        driver.manage().window().maximize();
        driver.get(ConfigLoader.getValue("base.url"));
    }

    @AfterMethod
    public void teardown(){
        if(driver!=null){
            driver.quit();
        }
    }
}
