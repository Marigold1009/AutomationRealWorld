package testsuite.ui.articles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.article.MArticle;
import model.article.MArticleCreate;
import model.article.MArticleResponse;
import model.article.MArticlesResponse;
import model.user.MUser;
import model.user.MUserDetail;
import org.testng.annotations.*;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.pageobject.DetailedArticlePage;
import selenium.pageobject.HomePage;
import selenium.pageobject.SignInPage;
import selenium.pageobject.UpdateArticlePage;
import testsuite.config.ApiDataFactory;
import utils.logging.LogFactory;

import java.net.MalformedURLException;
import java.util.List;

import static constant.Constant.*;
import static io.restassured.RestAssured.with;

public class UpdateArticlesTestSuite {
    ObjectMapper mapper = new ObjectMapper();
    BasePage basePage;
    HomePage homePage;
    SignInPage signInPage;
    UpdateArticlePage updateArticlePage;
    DetailedArticlePage detailedArticlePage;
    String existing_article_title;
    String token;
    String articleSlug;
    String articleDes;
    String articleContent;
    String articleTitle;
    String article_slug_update;
    String existing_body;
    String existing_des;

    @BeforeClass
    public void BeforeClass() throws JsonProcessingException, MalformedURLException {
//        Login and get 1 article
        RestAssured.baseURI = ApiDataFactory.API_URL;
        Response response01 = with().header("Content-Type","application/json")
                .when().request("GET","/articles");
        MArticlesResponse mArticlesResponse = mapper.readValue(response01.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList = mArticlesResponse.getArticles();
        existing_article_title = articleList.get(0).getTitle();
        existing_body = articleList.get(0).getBody();
        existing_des = articleList.get(0).getDescription();

//        Create an article by API
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(EMAIL);
        userDetail.setPassword(PASSWORD);
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response02 = with().header("Content-Type","application/json")
                .when().body(node).request("POST","/users/login");
        user = mapper.readValue(response02.getBody().prettyPrint(), MUser.class);
        userDetail = user.getUser();
        token = userDetail.getToken();
    }

    @BeforeMethod
    public void beforeMethod() throws MalformedURLException, JsonProcessingException, InterruptedException {
//        Create an article
        MArticle article = new MArticle();
        article.setTitle("create article " + System.currentTimeMillis());
        article.setDescription("This is description ");
        article.setBody("This is article content ");
        MArticleCreate mArticleCreate = new MArticleCreate();
        mArticleCreate.setArticle(article);
        JsonNode node1 = mapper.valueToTree(mArticleCreate);
        Response response03 = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().body(node1).request("POST","/articles");
        MArticleResponse mArticleResponse = mapper.readValue(response03.getBody().prettyPrint(), MArticleResponse.class);
        article = mArticleResponse.getArticle();
        articleSlug = article.getSlug();
        articleContent = article.getBody();
        articleDes = article.getDescription();
        articleTitle = article.getTitle();

//        Go to update the articles page
        //        Init Log
        LogFactory.init();
        DriverManager.startBrowser();
        basePage = new BasePage();
        homePage = new HomePage();
        signInPage = new SignInPage();
        detailedArticlePage = new DetailedArticlePage();
        updateArticlePage = new UpdateArticlePage();
        basePage.openPage(HOME_URL);
        homePage.click_on_button_sign_in();
        signInPage.enter_email(EMAIL);
        signInPage.enter_password(PASSWORD);
        signInPage.click_on_sign_in_button();
        Thread.sleep(3000);
        signInPage.switchToNewTab();
        signInPage.openPage(HOME_URL+"editor/"+articleSlug);
    }

    @AfterMethod
    public void AfterMethod() throws JsonProcessingException {
        DriverManager.quitDriver();
        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+article_slug_update);

        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+articleSlug);
    }


    @Test(description = "Update article successfully")
    public void TC1_UpdateArticle_Success_FullFields() throws InterruptedException {
        String article_title_update = articleTitle+System.currentTimeMillis();
        Thread.sleep(3000);
        updateArticlePage.enter_article_title(article_title_update);
        String article_body_update = articleContent+System.currentTimeMillis();
        updateArticlePage.enter_article_body(article_body_update);
        String article_des_update = articleDes+System.currentTimeMillis();
        updateArticlePage.enter_article_description(article_des_update);
        updateArticlePage.click_publish_button();
        Thread.sleep(3000);
        article_slug_update = article_title_update.trim().toLowerCase().replaceAll("\\s","-");
        updateArticlePage.verify_after_updating_success(HOME_URL+"article/"+article_slug_update);
        detailedArticlePage.verify_article_title(article_title_update);
        detailedArticlePage.verify_article_body(article_body_update);
    }

    @Test(description = "Update article successfully with empty description")
    public void TC2_UpdateArticle_Success_EmptyDes() throws InterruptedException {
        updateArticlePage.enter_article_description("");
        updateArticlePage.click_publish_button();
        updateArticlePage.verify_after_updating_success(HOME_URL+"article/"+articleSlug);
    }

    @Test(description = "Update article successfully with empty Body")
    public void TC3_UpdateArticle_Success_EmptyBody() throws InterruptedException {
        updateArticlePage.enter_article_body("");
        updateArticlePage.click_publish_button();
        updateArticlePage.verify_after_updating_success(HOME_URL+"article/" + articleSlug );

    }

    @Test(description = "Update article successfully with empty tag")
    public void TC4_UpdateArticle_Success_EmptyTag(){
    }

    @Test(description = "Update article successfully with existing description", groups = {"existing_data"})
    public void TC5_UpdateArticle_Success_ExistingDes() throws InterruptedException {
        updateArticlePage.enter_article_description(existing_des);
        updateArticlePage.click_publish_button();
        updateArticlePage.verify_after_updating_success(HOME_URL+"article/"+articleSlug);
    }

    @Test(description = "Update article successfully with existing Body", groups = {"existing_data"})
    public void TC6_UpdateArticle_Success_ExistingBody() throws InterruptedException {
        updateArticlePage.enter_article_body(existing_body);
        updateArticlePage.click_publish_button();
        updateArticlePage.verify_after_updating_success(HOME_URL+"article/"+articleSlug);
    }

    @Test(description = "Update article successfully with existing tag", groups = {"existing_data"})
    public void TC7_UpdateArticle_Success_ExistingTag(){

    }

    @Test(description = "Update article fail with empty title")
    public void TC8_UpdateArticle_Fail_EmptyTitle(){
        updateArticlePage.enter_article_title("");
        updateArticlePage.click_publish_button();
    }

    @Test(description = "Update article successfully with existing title", groups = {"existinf_data"})
    public void TC9_UpdateArticle_Fail_ExistingTitle(){
        updateArticlePage.enter_article_title(existing_article_title);
        updateArticlePage.click_publish_button();
    }

    @Test(description = "Clean data", enabled = false)
    public void CleanData() throws JsonProcessingException {
        Response response = with().queryParam("author","marigold09").request("GET","/articles");
        MArticlesResponse mArticlesResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList = mArticlesResponse.getArticles();
        for(MArticle article: articleList){
            with().headers("Content-Type","application/json","Authorization","Token "+token)
                    .request("DELETE","/articles/"+article.getSlug());
        }
    }
}
