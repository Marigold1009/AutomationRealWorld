package testsuite.config;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import testsuite.utils.ConfigLoader;

public class TestSuiteSetUp {
    private WebDriver driver;

    @BeforeSuite
    public void beforeSuite(){
//        Open Browser and access page
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(ConfigLoader.getValue("base.url"));

//        Go to sign up page and create an acc
        driver.findElement(By.xpath("//a[@href='/register']")).click();
        driver.findElement(By.xpath("//input[@placeholder='Username']"))
                .sendKeys(ConfigLoader.getValue("test.username"));
        driver.findElement(By.xpath("//input[@placeholder='Email']"))
                .sendKeys(ConfigLoader.getValue("test.email"));
        driver.findElement(By.xpath("//input[@placeholder='Password']"))
                .sendKeys(ConfigLoader.getValue("test.password"));
        driver.findElement(By.xpath("//button[@type='submit']")).click();

//        Check acc created successfully
        if(driver.getPageSource().contains("Your Feed")){
            System.out.println("Set up acc successfully");
        }else {
            System.out.println("Set up acc unsucessfully");
        }
//        Close Browser
        driver.quit();
    }

    @AfterSuite
    public void afterSuite(){
//        Delete the account
    }
}
