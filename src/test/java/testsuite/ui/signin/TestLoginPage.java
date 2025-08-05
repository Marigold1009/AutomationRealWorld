package testsuite.ui.signin;

import org.testng.annotations.*;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.pageobject.HomePage;
import selenium.pageobject.SignInPage;
import utils.logging.LogFactory;

import java.net.MalformedURLException;

import static constant.Constant.*;

public class TestLoginPage {
    HomePage homePage;
    SignInPage signInPage;

    @BeforeMethod
    public void BeforeMethod() throws MalformedURLException {
        LogFactory.init();
        DriverManager.startBrowser();
        BasePage basePage = new BasePage();
        basePage.openPage(HOME_URL);
        homePage = new HomePage();
        signInPage = new SignInPage();
        homePage.click_on_button_sign_in();
    }

    @AfterMethod
    public void AfterMethod() {
        DriverManager.quitDriver();
    }

    @Test(description = "Login successfully")
    public void TC1_Login_Success() {
        signInPage.waitForPageTitle("Conduit");
        signInPage.wait_for_email_field();
        signInPage.enter_email(EMAIL);
        signInPage.wait_for_password_field();
        signInPage.enter_password(PASSWORD);
        signInPage.click_on_sign_in_button();
    }

    @Test(description = "Login with empty email")
    public void TC2_login_without_email() {
        signInPage.waitForPageTitle("Conduit");
        signInPage.wait_for_password_field();
        signInPage.enter_password(PASSWORD);
        signInPage.click_on_sign_in_button();
    }

    @Test(description = "Login without password")
    public void TC3_login_without_password() {
        signInPage.waitForPageTitle("Conduit");
        signInPage.wait_for_email_field();
        signInPage.enter_email(EMAIL);
        signInPage.click_on_sign_in_button();
    }

    @Test(description = "Login with all empty value")
    public void TC4_login_without_all_credential() {
        signInPage.waitForPageTitle("Conduit");
        signInPage.wait_for_email_field();
        signInPage.wait_for_password_field();
        signInPage.click_on_sign_in_button();
    }

    @Test(description = "Login with wrong email")
    public void TC5_login_with_wrong_email() {
        signInPage.waitForPageTitle("Conduit");
        signInPage.wait_for_email_field();
        signInPage.enter_email(INVALID_EMAIL);
        signInPage.wait_for_password_field();
        signInPage.enter_password(PASSWORD);
        signInPage.click_on_sign_in_button();
        signInPage.verify_error_message(TOOL_TIP_PART01+"'"+INVALID_EMAIL+"'"+TOOL_TIP_PART02);
    }

    @Test(description = "Login with wrong password")
    public void TC6_login_with_wrong_password() {
        signInPage.waitForPageTitle("Conduit");
        signInPage.wait_for_email_field();
        signInPage.enter_email(EMAIL);
        signInPage.wait_for_password_field();
        signInPage.enter_password(INVALID_PASSWORD);
        signInPage.click_on_sign_in_button();
    }

    @Test(description = "Login with wrong password and email")
    public void TC7_login_with_wrong_all_credentials() {
        signInPage.waitForPageTitle("Conduit");
        signInPage.wait_for_email_field();
        signInPage.enter_email(INVALID_EMAIL);
        signInPage.wait_for_password_field();
        signInPage.enter_password(INVALID_PASSWORD);
        signInPage.click_on_sign_in_button();
        signInPage.verify_error_message(TOOL_TIP_PART01+"'"+INVALID_EMAIL+"'"+TOOL_TIP_PART02);
    }

    @Test(description = "navigate to Sign up page")
    public void TC8_navigate_to_sign_up_page(){
        signInPage.waitForPageTitle("Conduit");
        signInPage.click_need_an_account();
    }
}
