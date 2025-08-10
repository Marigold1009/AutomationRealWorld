package testsuite.ui.articles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.article.MArticle;
import model.article.MArticleResponse;
import model.article.MArticlesResponse;
import model.comments.MCommentDetail;
import model.comments.MComments;
import org.testng.ITestResult;
import org.testng.annotations.*;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.pageobject.DetailedArticlePage;
import selenium.pageobject.HomePage;
import testsuite.config.ApiDataFactory;
import utils.logging.LogFactory;

import java.net.MalformedURLException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static constant.Constant.HOME_URL;
import static io.restassured.RestAssured.with;

public class DetailedArticlesGuestModeTestSuite {
    BasePage basePage;
    HomePage homePage;
    DetailedArticlePage detailedArticlePage;
    ObjectMapper mapper = new ObjectMapper();
    MArticle article = new MArticle();
    List<MCommentDetail> commentList = new ArrayList<>();

    @BeforeClass
    public void beforeClass() throws MalformedURLException, JsonProcessingException {
//        Init LogFactory
        LogFactory.init();
        DriverManager.startBrowser();
        basePage = new BasePage();
        homePage = new HomePage();
        detailedArticlePage = new DetailedArticlePage();

//        Get article list and go to detail article page
        RestAssured.baseURI = ApiDataFactory.API_URL;
        Response response = with().header("Content-Type","application/json")
                        .when().request("GET","/articles");
        MArticlesResponse mArticlesResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList = mArticlesResponse.getArticles();
        String articleSlug = articleList.get(0).getSlug();


//        Get detailed article
        Response responseArticle = with().header("Content-Type","application/json")
                .when().request("GET","/articles/"+articleSlug);
        MArticleResponse articleResponse = mapper.readValue(responseArticle.getBody().prettyPrint(), MArticleResponse.class);
        article = articleResponse.getArticle();

//        Get article comment
        Response response2 = with().header("Content-Type","application/json")
                .when().request("GET","/articles/"+articleSlug+"/comments");
        MComments responseComments = mapper.readValue(response2.getBody().prettyPrint(), MComments.class);
        commentList = responseComments.getComments();

//        Open page
        basePage.openPage(HOME_URL+"article/"+articleSlug);
    }

    @AfterClass
    public void AfterClass(){
        DriverManager.quitDriver();
    }

    @BeforeGroups({"loginMode_global"})
    public void beforeGroup_LoginMode_Global() {
//        Create an acc
//        Home page --> Global tab
    }

    @BeforeGroups({"loginMode_yourFeed"})
    public void beforeGroup_LoginMode_YourFeed() {
//        Create an acc
//        Home page --> Follow some author
//        Your Feed tab
    }

    @BeforeGroups({"loginMode_myArticles"})
    public void beforeGroup_LoginMode_MyArticles() {
//        Create an account --> Login
//        Post an article
//        Home --> My Articles
    }

    @BeforeGroups({"loginMode_myFavorite"})
    public void beforeGroup_LoginMode_MyFavorite() {
//        Create an acc
//        Submit favorite some articles
//        Home Page --> Favorites articles
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        String groupName[] = result.getMethod().getGroups();
        if (Arrays.asList(groupName).contains("guest_mode")) {
//            Close Browser
        }
//        Delete Account --> Close Browser
    }

    @Test(description = "Verify Article Title")
    public void TC1_VerifyArticleTitle() {
        detailedArticlePage.verify_article_title(article.getTitle());
    }

    @Test(description = "Verify Author")
    public void TC2_VerifyAuthor() {
        detailedArticlePage.verify_author(article.getAuthor().getUsername());
    }

    @Test(description = "Redirect author page")
    public void TC3_GotoAuthorPage() {
        detailedArticlePage.click_author_link(HOME_URL+"@"+article.getAuthor().getUsername());
        detailedArticlePage.backToPreviousPage();
    }

    @Test(description = "Verify created date")
    public void TC4_VerifyCreatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);
        OffsetDateTime createdAt = OffsetDateTime.parse(article.getCreatedAt());
        String expected_CreatedAt = createdAt.format(formatter);
        detailedArticlePage.verify_createdDate(expected_CreatedAt);
    }

    @Test(description = "Verify Content of Article")
    public void TC5_VerifyArticleContent() {
        detailedArticlePage.verify_article_body(article.getBody());
    }

    @Test(description = "Verify Article TagList")
    public void TC6_VerifyArticleTagList() {
        List<String> tags = article.getTagList();
        detailedArticlePage.verify_tag(tags.size(),tags);
    }

    @Test(description = "Verify Article comments list")
    public void TC7_VerifyArticleComments() throws InterruptedException {
//        Need check has comment or not
        String expected_msg = "Sign in or sign up to add comments on this article.";
        detailedArticlePage.verify_login_msg(expected_msg);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);
        if(commentList.isEmpty()){
            detailedArticlePage.verify_no_comment();
        }else {
            for (int i=0; i<commentList.size(); i++){
                MCommentDetail comment = commentList.get(i);
                detailedArticlePage.verify_content_comment(comment.getBody());
                String author = comment.getAuthor().getUsername();
                detailedArticlePage.verify_author_comment(author);
                detailedArticlePage.click_author_comment(HOME_URL+"@"+author);
                Thread.sleep(3000);
                detailedArticlePage.backToPreviousPage();
                OffsetDateTime createdAt = OffsetDateTime.parse(comment.getCreatedAt());
                String expected_CreatedAt = createdAt.format(formatter);
                detailedArticlePage.verify_date_comment(expected_CreatedAt);
            }
        }
    }

    @Test(description = "Redirect to Login Page")
    public void TC8_GotoLoginPage() throws InterruptedException {
//        Click Sign in button
        detailedArticlePage.click_sign_in_button(HOME_URL+"login");
        detailedArticlePage.backToPreviousPage();
    }

    @Test(description = "Redirect to SIgn up page")
    public void TC9_GotoSignupPage() throws InterruptedException {
//        Click Sign up button
        detailedArticlePage.click_sign_up_button(HOME_URL+"register");
        detailedArticlePage.backToPreviousPage();
    }
}
