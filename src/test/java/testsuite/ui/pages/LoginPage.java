package testsuite.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

//    Elements
    private By emailField = By.xpath("//input[@type='email']");
    private By passwordField = By.xpath("//input[@type='password']");
    private By loginBtn = By.xpath("//button[@type='submit']");

//    Action
    public void inputEmail(String email){
        driver.findElement(emailField).sendKeys(email);
    }

    public void inputPassword(String password){
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLoginBtn(){
        driver.findElement(loginBtn).click();
    }
}
