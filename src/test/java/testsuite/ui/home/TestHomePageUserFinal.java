package testsuite.ui.home;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import constant.Constant;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.article.MArticle;
import model.article.MArticlesResponse;
import model.tag.MTags;
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

import static constant.Constant.*;
import static io.restassured.RestAssured.with;

public class TestHomePageUserFinal {
    HomePage homePage;
    SignInPage signInPage;
    private ObjectMapper mapper = new ObjectMapper();
    List<MArticle> articleList = new ArrayList<>();

    @BeforeClass
    public void BeforeClass() throws MalformedURLException, JsonProcessingException {
        LogFactory.init();
        DriverManager.startBrowser();
        homePage = new HomePage();
        signInPage = new SignInPage();

        RestAssured.baseURI = ApiDataFactory.API_URL;
        Response response = with().request("GET", "/articles");
        MArticlesResponse mArticlesResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        articleList = mArticlesResponse.getArticles();

        BasePage basePage = new BasePage();
        basePage.openPage(HOME_URL);
        basePage.waitForPageTitle(HOME_TITLE);

//        Go to login page and login
        homePage.click_on_button_sign_in();
        signInPage.enter_email(EMAIL);
        signInPage.enter_password(PASSWORD);
        signInPage.click_on_sign_in_button();
    }

    @AfterClass
    public void AfterClass() {
        DriverManager.quitDriver();
    }

    @BeforeMethod
    public void BeforeMethod() throws MalformedURLException {
    }

    @Test
    public void TC1_navigate_to_homepage() {
        homePage.click_home_button();
    }

    @Test
    public void TC2_navigate_create_article_page() {
        homePage.click_new_post_button();
    }

    @Test
    public void TC3_navigate_to_setting_page() {
        homePage.click_setting_button();
    }

    @Test
    public void TC4_navigate_to_profile_page() {
        homePage.click_profile_button();
    }

    @Test(description = "Verify tagList", enabled = false)
    public void TC5_verify_tag_List() throws JsonProcessingException {
        Response response = with().request("GET", "/tags");
        MTags tagListResponse = mapper.readValue(response.getBody().prettyPrint(), MTags.class);
        List<String> tagList = tagListResponse.getTags();
        for (int i = 1; i <= tagList.size(); i++) {
            homePage.verify_all_tag_list(i, tagList.get(i - 1));
        }
    }

    @Test(description = "Verify article list")
    public void TC6_verify_article_list() throws JsonProcessingException {
        homePage.click_on_feed_tab("Global Feed");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);
        for (int i = 0; i < 10; i++) {
            MArticle article = articleList.get(i);
            List<String> articleTagList = new ArrayList<>();
            articleTagList = article.getTagList();
            OffsetDateTime createdAt = OffsetDateTime.parse(article.getCreatedAt());
            String expected_CreatedAt = createdAt.format(formatter);
            homePage
                    .verify_article_author(article.getTitle(), article.getAuthor().getUsername())
                    .verify_article_favorite_count(article.getTitle(), String.valueOf(article.getFavoritesCount()))
                    .verify_article_body(article.getTitle(), article.getDescription())
                    .verify_readmore_button(article.getTitle())
                    .verify_createdAt(article.getTitle(), expected_CreatedAt)
                    .verify_article_title(article.getTitle(), article.getTitle());
            if (!articleTagList.isEmpty()) {
                for (int j = 0; j < articleTagList.size(); j++) {
                    homePage.verify_aricle_tag_list(article.getTitle(), (j + 1), articleTagList.get(j));
                }
            }
        }
    }

    @Test(description = "navigate to author page")
    public void TC7_navigate_to_author_page(){
        homePage.click_on_feed_tab("Global Feed");
        MArticle article = articleList.get(0);
        String author = article.getAuthor().getUsername();
        homePage.click_author_link(article.getTitle(),"https://realworld-ui.ap.ngrok.io/@"+author);
    }

    @Test(description = "Navigate to Detail article by click title")
    public void TC8_navigate_detail_article_by_click_title(){
        homePage.click_on_feed_tab("Global Feed");
        MArticle article = articleList.get(0);
        String title = article.getTitle();
        homePage.click_article_title(title,HOME_URL+"article/"+article.getSlug());
    }

    @Test(description = "Click Readmore button")
    public void TC9_click_readmore_button(){
        MArticle article = articleList.get(0);
        homePage.click_readmore_button(article.getTitle(),HOME_URL+"articlle/"+article.getSlug());
    }

    @Test(description = "click  article tag")
    public void TC10_click_article_tag(){
        homePage.click_on_feed_tab("Global Feed");
        MArticle article = new MArticle();
        for(int i=0; i<10; i++){
            if(!articleList.get(i).getTagList().isEmpty()){
                article = articleList.get(i);
                break;
            }
        }
        for (int j=0; j<article.getTagList().size();j++){
            homePage.click_article_tag(article.getTitle(),j+1,HOME_URL+"article/"+article.getSlug());
            homePage.backToPreviousPage();
            homePage.click_on_feed_tab("Global Feed");
        }

    }


}
