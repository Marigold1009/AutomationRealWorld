package testsuite.ui.home;

import static io.restassured.RestAssured.with;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.net.MalformedURLException;
import java.util.List;
import model.article.MArticle;
import model.article.MArticlesResponse;
import org.testng.annotations.*;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.pageobject.HomePage;
import selenium.pageobject.SignInPage;
import testsuite.config.ApiDataFactory;
import utils.logging.LogFactory;

public class TestHomePage {
  private final ObjectMapper mapper = new ObjectMapper();
  HomePage homePage;
  SignInPage signInPage;

  @BeforeClass
  public void beforeClass() throws MalformedURLException {
    RestAssured.baseURI = ApiDataFactory.API_URL;
    LogFactory.init();
    DriverManager.startBrowser();
    homePage = new HomePage();
    signInPage = new SignInPage();
  }

  @AfterClass
  public void afterClass() {
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

  @Test(enabled = false)
  public void TC_navigate_to_sign_in_page() throws InterruptedException {
    homePage.click_on_button_sign_in();
    signInPage.wait_for_email_field();
  }

  @Test(enabled = false)
  public void TC_navigate_to_sign_up_page() {
    //    homePage.click_on_button_sign_up();
    //    signInPage.wait_for_email_field();
  }

  @Test
  public void TC_verify_list_of_articles() throws JsonProcessingException {
    // Call api to get articles
    Response response =
        with().headers("Content-Type", "application/json").when().request("GET", "/articles");
    MArticlesResponse mArticleListResponse =
        mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);

    List<MArticle> articles = mArticleListResponse.getArticles();
    // UI only show 10 articles
    for (int i = 0; i < 9; i++) {
      MArticle article = articles.get(i);
      homePage
          .verify_article_favorite_count(article.getTitle(), String.valueOf(article.getFavoritesCount()))
          .verify_article_author(article.getTitle(), article.getAuthor().getUsername());
    }
  }
}
