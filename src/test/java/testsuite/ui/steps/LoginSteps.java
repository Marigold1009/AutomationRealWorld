package testsuite.ui.steps;

import org.openqa.selenium.WebDriver;
import testsuite.ui.pages.LoginPage;

public class LoginSteps {
    private WebDriver driver;
    private LoginPage loginPage;

    public LoginSteps(WebDriver driver) {
        this.driver = driver;
        this.loginPage = loginPage;
    }

    public void loginWith(String email, String password){
        loginPage.inputEmail(email);
        loginPage.inputPassword(password);
        loginPage.clickLoginBtn();
    }

}
