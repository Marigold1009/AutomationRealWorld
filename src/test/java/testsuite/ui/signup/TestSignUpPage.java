package testsuite.ui.signup;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.pageobject.HomePage;
import selenium.pageobject.SignUpPage;
import utils.logging.LogFactory;

import java.net.MalformedURLException;

import static constant.Constant.*;

public class TestSignUpPage {
    HomePage homePage;
    SignUpPage signUpPage;

    @BeforeMethod
    public void BeforeMethod() throws MalformedURLException {
        LogFactory.init();
        DriverManager.startBrowser();
        BasePage basePage = new BasePage();
        homePage = new HomePage();
        signUpPage = new SignUpPage();
        basePage.openPage(HOME_URL);
        homePage.click_on_button_sign_up();
    }

    @AfterMethod
    public void AfterMethod() {
        DriverManager.quitDriver();
    }

    @Test(description = "Sign up successfully", enabled = false)
    public void TC1_signup_success() {
        signUpPage.wait_for_user_field();
        signUpPage.enter_username(BASE_USER_NAME + System.currentTimeMillis());
        signUpPage.enter_email(BASE_EMAIL + System.currentTimeMillis() + EMAIL_DOMAIN);
        signUpPage.enter_password(PASSWORD);
        signUpPage.click_signup_button();
    }

    @Test(description = "Sign up with existing user")
    public void TC2_signup_with_existing_user(){
        signUpPage.wait_for_user_field();
        signUpPage.enter_username(BASE_USER_NAME);
        signUpPage.enter_email(BASE_EMAIL + System.currentTimeMillis() + EMAIL_DOMAIN);
        signUpPage.enter_password(PASSWORD);
        signUpPage.click_signup_button();
        signUpPage.verify_error_msg("user with this username already exists");
    }

    @Test(description = "Sign up with existing email")
    public void TC3_signup_with_existing_email(){
        signUpPage.enter_username(BASE_USER_NAME+System.currentTimeMillis());
        signUpPage.enter_email(EMAIL);
        signUpPage.enter_password(PASSWORD);
        signUpPage.click_signup_button();
        signUpPage.verify_error_msg("user with this email already exists");
    }

    @Test(description = "Sign up with existing email")
    public void TC3_signup_with_invalid_email(){
        signUpPage.enter_username(BASE_USER_NAME);
        signUpPage.enter_email(EMAIL);
        signUpPage.enter_password(PASSWORD);
        signUpPage.click_signup_button();
//        Verify the tootip
    }

}
