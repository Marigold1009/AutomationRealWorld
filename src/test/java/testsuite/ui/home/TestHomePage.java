package testsuite.ui.home;

import org.assertj.core.api.Assertions;
import org.testng.annotations.*;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.pageobject.HomePage;
import selenium.pageobject.SignInPage;
import utils.logging.LogFactory;

import java.net.MalformedURLException;

public class TestHomePage {
  HomePage homePage;
  SignInPage signInPage;

  @BeforeClass
  public void beforeClass() throws MalformedURLException {
    LogFactory.init();
    DriverManager.startBrowser();
    homePage = new HomePage();
    signInPage = new SignInPage();
  }

  @AfterClass
  public void afterClass(){
    DriverManager.quitDriver();
  }

  @BeforeMethod
  public void beforeMethod() {
    String homeUrl = "https://realworld-ui.ap.ngrok.io/";
    String pageTitle = "Conduit";
    // Open homepage
    BasePage basePage = new BasePage();
    basePage.openPage(homeUrl);
    basePage.waitForPageTitle(pageTitle);
  }

  @Test
  public void TC_navigate_to_sign_in_page() {
    homePage.click_on_button_sign_in();
    signInPage.wait_for_email_field();
  }
}
