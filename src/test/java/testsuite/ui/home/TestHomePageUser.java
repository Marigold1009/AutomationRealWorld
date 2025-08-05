package testsuite.ui.home;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import constant.Constant;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.article.MArticle;
import model.article.MArticlesResponse;
import model.tag.MTags;
import model.user.MUser;
import model.user.MUserDetail;
import org.openqa.selenium.JavascriptExecutor;
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

public class TestHomePageUser {
    HomePage homePage;
    SignInPage signInPage;
    private ObjectMapper mapper = new ObjectMapper();
    List<MArticle> articleList = new ArrayList<>();
    String token;
    String myUserName;

    @BeforeClass
    public void BeforeClass() throws MalformedURLException, JsonProcessingException {
//        LogFactory.init();
//        DriverManager.startBrowser();
//        homePage = new HomePage();
//        signInPage = new SignInPage();

        RestAssured.baseURI = ApiDataFactory.API_URL;
        Response response = with().request("GET", "/articles");
        MArticlesResponse mArticlesResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        articleList = mArticlesResponse.getArticles();

//        Login by API and Store token
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(EMAIL);
        userDetail.setPassword(PASSWORD);
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response1 = with().header("Content-Type", "application/json")
                .when().body(node).request("POST", "/users/login");
        user = mapper.readValue(response1.getBody().prettyPrint(), MUser.class);
        userDetail = user.getUser();
        token = userDetail.getToken();
        myUserName = userDetail.getUsername();
    }

    @AfterMethod
    public void AfterMethod() {
        DriverManager.quitDriver();
    }

    @BeforeMethod
    public void BeforeMethod() throws MalformedURLException, JsonProcessingException {
//        Go to Home page
        LogFactory.init();
        DriverManager.startBrowser();
        BasePage basePage = new BasePage();
        homePage = new HomePage();
        signInPage = new SignInPage();
        basePage.openPage(HOME_URL);
        basePage.waitForPageTitle(HOME_TITLE);


        //        Go to login page and login
        homePage.click_on_button_sign_in();
        signInPage.enter_email(EMAIL);
        signInPage.enter_password(PASSWORD);
        signInPage.click_on_sign_in_button();
    }

    @Test(enabled = false)
    public void TC1_navigate_to_homepage() {
        homePage.click_home_button();
    }

    @Test(enabled = false)
    public void TC2_navigate_create_article_page() {
        homePage.click_new_post_button();
    }

    @Test(enabled = false)
    public void TC3_navigate_to_setting_page() {
        homePage.click_setting_button();
    }

    @Test(enabled = false)
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

    @Test(description = "Verify article list", enabled = false)
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
            if (article.isFavorited()) {
                homePage.verify_marked_favorited_button(article.getTitle());
            } else {
                homePage.verify_unmarked_favorited_button(article.getTitle());
            }
        }
    }

    @Test(description = "navigate to author page", enabled = false)
    public void TC7_navigate_to_author_page() {
        homePage.click_on_feed_tab("Global Feed");
        MArticle article = articleList.get(0);
        String author = article.getAuthor().getUsername();
        homePage.click_author_link(article.getTitle(), "https://realworld-ui.ap.ngrok.io/@" + author);
    }

    @Test(description = "Navigate to Detail article by click title", enabled = false)
    public void TC8_navigate_detail_article_by_click_title() {
        homePage.click_on_feed_tab("Global Feed");
        MArticle article = articleList.get(0);
        String title = article.getTitle();
        homePage.click_article_title(title, HOME_URL + "article/" + article.getSlug());
    }

    @Test(description = "Click Readmore button", enabled = false)
    public void TC9_click_readmore_button() {
        homePage.click_on_feed_tab("Global Feed");
        MArticle article = articleList.get(0);
        homePage.click_readmore_button(article.getTitle(), HOME_URL + "article/" + article.getSlug());
    }

    @Test(description = "click  article tag", enabled = false)
    public void TC10_click_article_tag() {
        homePage.click_on_feed_tab("Global Feed");
        MArticle article = new MArticle();
        for (int i = 0; i < 10; i++) {
            if (!articleList.get(i).getTagList().isEmpty()) {
                article = articleList.get(i);
                break;
            }
        }
        for (int j = 0; j < article.getTagList().size(); j++) {
            homePage.click_article_tag(article.getTitle(), j + 1, HOME_URL + "article/" + article.getSlug());
            homePage.backToPreviousPage();
            homePage.click_on_feed_tab("Global Feed");
        }

    }

    @Test(description = "Verify feed article", enabled = false)
    public void TC11_verify_feed_tab_without_article() {
        homePage.verify_no_article_feed();
    }

    @Test(description = "Verify article feed")
    public void TC12_verify_article_feed() throws JsonProcessingException, InterruptedException {
//        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
//        String token1 = (String) js.executeScript("return window.localStorage.getItem('jwt');");
//        System.out.println(token1);
//        Follow user to get feed
        String username = "";
        for (MArticle article : articleList) {
            if (!article.getAuthor().getUsername().equals(myUserName)) {
                username = article.getAuthor().getUsername();
                break;
            }
        }
        Response rs01 = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().request("POST", "/profiles/" + username + "/follow");
        int sttCode = rs01.statusCode();

//        Get feed articles
        Response responseArticleList = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().request("GET", "/articles/feed");
        MArticlesResponse feedListResponse = mapper.readValue(responseArticleList.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> feedList = feedListResponse.getArticles();
        int index = feedList.size();
        if (index > 10) {
            index = 10;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);
        homePage.refreshPage();
        for (int i = 0; i < index; i++) {
            MArticle article = feedList.get(i);
            List<String> articleTagList = article.getTagList();
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

            if (article.isFavorited()) {
                homePage.verify_marked_favorited_button(article.getTitle());
            } else {
                homePage.verify_unmarked_favorited_button(article.getTitle());
            }
        }

//        After verify, unfollow user
        with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().request("DELETE", "/profiles/" + username + "/follow");
    }

    @Test(description = "Verify favoried article")
    public void TC13_Verify_favorited_article() {
        homePage.click_on_feed_tab("Global Feed");
//        Call API to mark article
        MArticle article = articleList.get(0);
        with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().request("POST", "/articles/" + article.getSlug() + "/favorite");
        homePage.verify_marked_favorited_button(article.getTitle());
    }

    @Test(description = "Verify favoried article")
    public void TC14_Verify_unmark_favorited_article() {
        homePage.click_on_feed_tab("Global Feed");
//        Call API to unmark article
        MArticle article = articleList.get(0);
        with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().request("DELETE", "/articles/" + article.getSlug() + "/favorite");
        homePage.verify_unmarked_favorited_button(article.getTitle());
    }

    @Test(description = "Click tagName on tag List")
    public void TC15_verify_article_list_by_tag() throws JsonProcessingException {
//        Call API to get tagList
        Response response = with().header("Content-Type", "application/json")
                .when().request("GET", "/tags");
        MTags tagListResponse = mapper.readValue(response.getBody().prettyPrint(), MTags.class);
        List<String> tagList = tagListResponse.getTags();
        String tag = tagList.get(0);

//        Call API to get article by tag
        Response response1 = with().header("Content-Type", "application/json")
                .when().request("GET", "/articles/");
        MArticlesResponse articlesResponse = mapper.readValue(response1.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleListByTag = articlesResponse.getArticles();
        int index = articleListByTag.size();

//        Click tag button on UI
        homePage.click_on_feed_tab(tagList.get(0));
        if (index > 10) {
            index = 10;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);
        homePage.refreshPage();
        for (int i = 0; i < index; i++) {
            MArticle article = articleListByTag.get(i);
            List<String> articleTagList = article.getTagList();
            OffsetDateTime createdAt = OffsetDateTime.parse(article.getCreatedAt());
            String expected_CreatedAt = createdAt.format(formatter);
            homePage
                    .verify_article_author(article.getTitle(), article.getAuthor().getUsername())
                    .verify_article_favorite_count(article.getTitle(), String.valueOf(article.getFavoritesCount()))
                    .verify_article_body(article.getTitle(), article.getDescription())
                    .verify_readmore_button(article.getTitle())
                    .verify_createdAt(article.getTitle(), expected_CreatedAt)
                    .verify_article_title(article.getTitle(), article.getTitle());
            for (int j = 0; j < articleTagList.size(); j++) {
                homePage.verify_article_tag_in_tag_tab(tag, article.getTagList());
            }

            if (article.isFavorited()) {
                homePage.verify_marked_favorited_button(article.getTitle());
            } else {
                homePage.verify_unmarked_favorited_button(article.getTitle());
            }
        }
    }
}
