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
import model.comments.MComment;
import model.comments.MCommentDetail;
import model.comments.MComments;
import model.user.MUser;
import model.user.MUserDetail;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.pageobject.DetailedArticlePage;
import selenium.pageobject.HomePage;
import selenium.pageobject.SignInPage;
import testsuite.config.ApiDataFactory;
import utils.logging.LogFactory;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Objects;

import static constant.Constant.*;
import static io.restassured.RestAssured.with;

public class DetailedArticleLoginModeTestSuite {
    BasePage basePage;
    DetailedArticlePage detailedArticlePage;
    HomePage homePage;
    SignInPage signInPage;
    String token;
    ObjectMapper mapper = new ObjectMapper();
    String articleSlug;
    String owner_article_slug;
    String userName;

    @BeforeClass
    public void BeforeClass() throws MalformedURLException, JsonProcessingException, InterruptedException {
//        Login and store Token
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(EMAIL);
        userDetail.setPassword(PASSWORD);
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        RestAssured.baseURI = ApiDataFactory.API_URL;
        Response response = with().header("Content-Type", "application/json")
                .when().body(node).request("POST", "/users/login");
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        userDetail = user.getUser();
        token = userDetail.getToken();
        userName = userDetail.getUsername();

//        Get article list and get 1 articles
        Response response1 = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().request("GET", "/articles");
        MArticlesResponse mArticlesResponse = mapper.readValue(response1.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList = mArticlesResponse.getArticles();
        for(MArticle article: articleList){
            if(!Objects.equals(userName, article.getAuthor().getUsername())){
                articleSlug = article.getSlug();
                break;
            }
        }


        LogFactory.init();
        DriverManager.startBrowser();
        basePage = new BasePage();
        homePage = new HomePage();
        signInPage = new SignInPage();
        detailedArticlePage = new DetailedArticlePage();

//        Create 1 article, owner = user login by call Api
        MArticle owner_article = new MArticle();
        owner_article.setTitle(articleSlug + System.currentTimeMillis());
        owner_article.setBody("This is the body");
        owner_article.setDescription("This is the description");
        MArticleCreate mArticleCreate = new MArticleCreate();
        mArticleCreate.setArticle(owner_article);
        JsonNode node1 = mapper.valueToTree(mArticleCreate);
        Response response2 = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(node1).request("POST", "/articles");
        MArticleResponse articleResponse = mapper.readValue(response2.getBody().prettyPrint(), MArticleResponse.class);
        owner_article = articleResponse.getArticle();
        owner_article_slug = owner_article.getSlug();
        basePage.openPage(HOME_URL);
        homePage.click_on_button_sign_in();
        signInPage.enter_email(EMAIL);
        signInPage.enter_password(PASSWORD);
        signInPage.click_on_sign_in_button();
        Thread.sleep(3000);
        signInPage.switchToNewTab();
        signInPage.openPage(HOME_URL + "article/" + articleSlug);
        Thread.sleep(3000);
    }

    @Test(description = "Create a comment", priority = 1)
    public void TC1_CreateComment() throws JsonProcessingException {
//        Input value and Publish button
        detailedArticlePage.input_comment("This is a comment test " + System.currentTimeMillis());
        detailedArticlePage.click_publish_button();

//        Get all comments for articles
        Response responseComment = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().request("GET", "/articles/" + articleSlug + "/comments");
        MComments commentResponse = mapper.readValue(responseComment.getBody().prettyPrint(), MComments.class);
        List<MCommentDetail> mCommentDetails = commentResponse.getComments();

//        Check new post comment in the list
        detailedArticlePage.verify_content_comment(mCommentDetails.get(0).getBody());
    }

    @Test(description = "Delete a comment", priority = 2)
    public void TC2_DeleteComment() {
//        Click delete comment
        detailedArticlePage.click_delete_comment_button();
    }

    @Test(description = "Click edit and delete article", priority = 3)
    public void TC3_Owner_article() throws InterruptedException {
        basePage.switchToNewTab();
        basePage.openPage(HOME_URL + "article/" + owner_article_slug);
        detailedArticlePage.click_edit_article_button(HOME_URL + "editor/" + owner_article_slug);
        detailedArticlePage.backToPreviousPage();
        detailedArticlePage.click_delete_article_button(HOME_URL);
    }

}
