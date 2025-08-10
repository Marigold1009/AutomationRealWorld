package testsuite.ui.articles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.article.MArticle;
import model.article.MArticlesResponse;
import model.user.MUser;
import model.user.MUserDetail;
import org.testng.ITestResult;
import org.testng.annotations.*;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.pageobject.CreateArticlePage;
import selenium.pageobject.HomePage;
import selenium.pageobject.SignInPage;
import testsuite.config.ApiDataFactory;
import utils.logging.LogFactory;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import static constant.Constant.*;
import static io.restassured.RestAssured.with;

public class CreateArticleTestSuite {

    BasePage basePage;
    HomePage homePage;
    SignInPage signInPage;
    CreateArticlePage createArticlePage;
    String token;
    String articleExistingTitle;
    String articleExistingSummary;
    String articleExistingBody;
    String articleTitle;
    String articleBody;
    String articleSummary;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public void BeforeClass() throws JsonProcessingException {
        LogFactory.init();
//        Login and store token
        RestAssured.baseURI = ApiDataFactory.API_URL;
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(EMAIL);
        userDetail.setPassword(PASSWORD);
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = with().header("Content-Type","application/json")
                .body(node).request("POST","/users/login");
        user = mapper.readValue(response.getBody().prettyPrint(),MUser.class);
        userDetail = user.getUser();
        token = userDetail.getToken();

//        Get list article and get 1 existing title
        Response response1 = with().header("Content-Type","application/json")
                .when().request("GET","/articles");
        MArticlesResponse mArticlesResponse = mapper.readValue(response1.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articles = mArticlesResponse.getArticles();
        articleExistingTitle = articles.get(0).getTitle();
        articleTitle = articleExistingTitle + System.currentTimeMillis();
        articleSummary = articles.get(0).getDescription();
        articleBody = articles.get(0).getBody();
        articleExistingSummary = "This is existing summary";
        articleExistingBody = "This is existing body";
    }
//    @BeforeGroups(groups = {"existing_data"})
//    public void beforeGroup_ExistingData() {
////        Create an acc
////        Create an article
////        Go to Create articles panel
//    }
//
//    @AfterGroups({"existing_data"})
//    public void afterGroup_ExistingData(){
////        Delete the article
////        Delete the acc
//    }

    @BeforeMethod
    public void beforeMethod() throws MalformedURLException, InterruptedException {
//        String groupName[] = result.getMethod().getGroups();
//        if (Arrays.asList(groupName).contains("existing_data")) {
//        } else {
////            Create an account
////            Go to create article page
////            Close Browser
//        }
        DriverManager.startBrowser();
        basePage = new BasePage();
        homePage = new HomePage();
        signInPage = new SignInPage();
        createArticlePage = new CreateArticlePage();

//        Open url and login and go to create page
        basePage.openPage(HOME_URL);
        homePage.click_on_button_sign_in();
        signInPage.enter_email(EMAIL);
        signInPage.enter_password(PASSWORD);
        signInPage.click_on_sign_in_button();
        Thread.sleep(3000);
        homePage.click_new_post_button();
    }

    @AfterMethod
    public void afterMethod() {
//        String groupName[] = result.getMethod().getGroups();
//        if(Arrays.asList(groupName).contains("existing_data")){
//
//        }else {
////            Delete the article
////            Delete the acc
////            Close Browser
//        }
//        Delete the acc

        DriverManager.quitDriver();
    }

    @Test(description = "Create article successfully full fields")
    public void TC1_CreateArticle_Success_FullFields() throws InterruptedException {
//        input value --> Publish button
        createArticlePage.enter_article_title(articleTitle);
        createArticlePage.enter_article_summary(articleSummary);
        createArticlePage.enter_article_body(articleBody);
        createArticlePage.click_publish_button();
        Thread.sleep(3000);
        String slug = articleTitle.trim().toLowerCase().replaceAll("\\s","-");
        createArticlePage.verify_after_publish_article_success(HOME_URL+"article/"+slug);

//        Delete the article
        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+slug);
    }

    @Test(description = "Create article success with empty description")
    public void TC2_CreateArticle_Success_EmptyDes() throws InterruptedException {
        createArticlePage.enter_article_title(articleTitle);
        createArticlePage.enter_article_body(articleBody);
        createArticlePage.click_publish_button();
        Thread.sleep(3000);
        String slug = articleTitle.trim().toLowerCase().replaceAll("\\s","-");
        createArticlePage.verify_after_publish_article_success(HOME_URL+"article/"+slug);

//        Delete the article
        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+slug);
    }

    @Test(description = "Create article success with empty body")
    public void TC3_CreateArticle_Success_EmptyBody() throws InterruptedException {
        createArticlePage.enter_article_title(articleTitle);
        createArticlePage.enter_article_summary(articleSummary);
        createArticlePage.click_publish_button();
        Thread.sleep(3000);
        String slug = articleTitle.trim().toLowerCase().replaceAll("\\s","-");
        createArticlePage.verify_after_publish_article_success(HOME_URL+"article/"+slug);

//        Delete the article
        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+slug);
    }

    @Test(description = "Create article success with empty tag")
    public void TC4_CreateArticle_Success_EmptyTag() throws InterruptedException {
        createArticlePage.enter_article_title(articleTitle);
        createArticlePage.enter_article_summary(articleSummary);
        createArticlePage.enter_article_body(articleBody);
        createArticlePage.click_publish_button();
        Thread.sleep(3000);
        String slug = articleTitle.trim().toLowerCase().replaceAll("\\s","-");
        createArticlePage.verify_after_publish_article_success(HOME_URL+"article/"+slug);

//        Delete the article
        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+slug);
    }
    @Test(description = "Create article success with existing description", groups = {"existing_data"})
    public void TC5_CreateArticle_Success_ExistingDes() throws InterruptedException {
        createArticlePage.enter_article_title(articleTitle);
        createArticlePage.enter_article_summary(articleExistingSummary);
        createArticlePage.enter_article_body(articleBody);
        createArticlePage.click_publish_button();
        Thread.sleep(3000);
        String slug = articleTitle.trim().toLowerCase().replaceAll("\\s","-");
        createArticlePage.verify_after_publish_article_success(HOME_URL+"article/"+slug);

//        Delete the article
        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+slug);
    }

    @Test(description = "Create article success with existong body", groups = {"existing_data"})
    public void TC6_CreateArticle_Success_ExistingBody() throws InterruptedException {
        createArticlePage.enter_article_title(articleTitle);
        createArticlePage.enter_article_summary(articleSummary);
        createArticlePage.enter_article_body(articleExistingBody);
        createArticlePage.click_publish_button();
        Thread.sleep(3000);
        String slug = articleTitle.trim().toLowerCase().replaceAll("\\s","-");
        createArticlePage.verify_after_publish_article_success(HOME_URL+"article/"+slug);

//        Delete the article
        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+slug);

    }

    @Test(description = "Create article success with existing tag", groups = {"existing_data"})
    public void TC7_CreateArticle_Success_ExistingTag() throws InterruptedException {
        createArticlePage.enter_article_title(articleTitle);
        createArticlePage.enter_article_summary(articleExistingSummary);
        createArticlePage.enter_article_body(articleExistingBody);
        createArticlePage.click_publish_button();
        Thread.sleep(3000);
        String slug = articleTitle.trim().toLowerCase().replaceAll("\\s","-");
        createArticlePage.verify_after_publish_article_success(HOME_URL+"article/"+slug);

//        Delete the article
        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+slug);
    }

    @Test(description = "Create article fail_Empty all fields")
    public void TC8_CreateArticle_Fail_EmptyAllFields() {
        createArticlePage.click_publish_button();
    }

    @Test(description = "Create article fail_Empty title")
    public void TC9_CreateArticle_Fail_EmptyTitle() {
        createArticlePage.enter_article_summary(articleSummary);
        createArticlePage.enter_article_body(articleBody);
        createArticlePage.click_publish_button();
    }

    @Test(description = "Create article fail_existing title", groups = "existing_data")
    public void TC10_CreateArticle_Fail_ExistingTitle() {
        createArticlePage.enter_article_title(articleExistingTitle);
        createArticlePage.enter_article_summary(articleSummary);
        createArticlePage.enter_article_body(articleBody);
        createArticlePage.click_publish_button();
    }
}
