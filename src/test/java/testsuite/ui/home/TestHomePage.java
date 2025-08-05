package testsuite.ui.home;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.article.MArticle;
import model.article.MArticlesResponse;
import model.tag.MTags;
import org.assertj.core.api.Assertions;
import org.testng.annotations.*;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.pageobject.HomePage;
import selenium.pageobject.SignInPage;
import testsuite.config.ApiDataFactory;
import utils.logging.LogFactory;

import java.net.MalformedURLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static io.restassured.RestAssured.with;

public class TestHomePage {
  HomePage homePage;
  SignInPage signInPage;
  private ObjectMapper mapper = new ObjectMapper();

  @BeforeClass
  public void beforeClass() throws MalformedURLException {
    LogFactory.init();
    DriverManager.startBrowser();
    homePage = new HomePage();
    signInPage = new SignInPage();

    RestAssured.baseURI = ApiDataFactory.API_URL;
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

  @Test(description = "Navigate to sign in page", enabled = false)
  public void TC1_navigate_to_sign_in_page() {
    homePage.click_on_button_sign_in();
    signInPage.wait_for_email_field();

  }

  @Test(description = "Navigate to sign_up_page", enabled = false)
  public void TC2_navigate_to_sign_up_page(){
    homePage.click_on_button_sign_up();
  }

  @Test(description = "Verify tagList")
  public void TC3_verify_tag_List() throws JsonProcessingException {
    Response response = with().request("GET","/tags");
    MTags tagListResponse = mapper.readValue(response.getBody().prettyPrint(), MTags.class);
    List<String> tagList = tagListResponse.getTags();
    for(int i=1; i<=tagList.size();i++){
      homePage.verify_all_tag_list(i,tagList.get(i-1));
    }
  }

  @Test(description = "Verify list of article", enabled = false)
  public void TC4_verify_article_list() throws JsonProcessingException {
//    Get article list by API
    Response response = with().request("GET","/articles");
    MArticlesResponse mArticlesResponse = mapper.readValue(response.getBody().prettyPrint(),MArticlesResponse.class);
    List<MArticle> articleList = mArticlesResponse.getArticles();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);
    for (int i=0; i<10; i++){
      MArticle article = articleList.get(i);
      List<String> articleTagList = new ArrayList<>();
      articleTagList = article.getTagList();
      OffsetDateTime createdAt = OffsetDateTime.parse(article.getCreatedAt());
      String expected_CreatedAt = createdAt.format(formatter);
      homePage
              .verify_article_author(article.getTitle(),article.getAuthor().getUsername())
              .verify_article_favorite_count(article.getTitle(),String.valueOf(article.getFavoritesCount()))
              .verify_article_body(article.getTitle(),article.getDescription())
              .verify_readmore_button(article.getTitle())
              .verify_createdAt(article.getTitle(),expected_CreatedAt)
              .verify_article_title(article.getTitle(),article.getTitle());
      if(!articleTagList.isEmpty()){
        for(int j=0; j<articleTagList.size();j++){
          homePage.verify_aricle_tag_list(article.getTitle(),(j+1),articleTagList.get(j));
        }
      }
    }
  }
}
