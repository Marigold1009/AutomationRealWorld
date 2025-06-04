package testsuite.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory {
    //    init Driver
    private static WebDriver driver;

    public static WebDriver getDriver(String browser) {
        if (driver == null) {
            switch (browser.toLowerCase()) {
                case "chrome":
//                    System.setProperty("webdriver.chrome.driver", "path to chrome driver");
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Not support browser" + browser);
            }
            driver.manage().window().maximize();
        }
        return driver;
    }

    public static void quitDriver(){
        if(driver!=null){
            driver.quit();
            driver = null;
        }
    }
}

