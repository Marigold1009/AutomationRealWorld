package testsuite.ui.authorpage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.article.MArticle;
import model.article.MArticlesResponse;
import model.article.MAuthor;
import model.user.MUser;
import model.user.MUserDetail;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.pageobject.AuthorPage;
import selenium.pageobject.HomePage;
import selenium.pageobject.SignInPage;
import testsuite.config.ApiDataFactory;
import utils.logging.LogFactory;

import java.net.MalformedURLException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static constant.Constant.*;
import static io.restassured.RestAssured.with;

public class AuthorPageTestSuite {
    String author;
    String token;
    ObjectMapper mapper = new ObjectMapper();
    HomePage homePage;
    AuthorPage authorPage;
    SignInPage signInPage;
    BasePage basePage;
    List<MArticle> articleList = new ArrayList<>();
    @BeforeClass
    public void beforeClass() throws JsonProcessingException, MalformedURLException, InterruptedException {
//  Get and get an author by API
        RestAssured.baseURI = ApiDataFactory.API_URL;
        Response response = with().header("Content-Type","application/json").request("GET","/articles");
        MArticlesResponse articlesResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList1 = articlesResponse.getArticles();
        for(MArticle article: articleList1){
            if(!article.getAuthor().getUsername().equals("marigold09")){
                author = article.getAuthor().getUsername();
                break;
            }
        }

//        Login and store token
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(EMAIL);
        userDetail.setPassword(PASSWORD);
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response responseUser = with().header("Content-Type","application/json")
                        .when().body(node).request("POST","/users/login");
        user = mapper.readValue(responseUser.getBody().prettyPrint(), MUser.class);
        userDetail = user.getUser();
        token = userDetail.getToken();

        //        Base on API response to get list
        Response articlsesListResponse = with().header("Content-Type","application/json")
                .when().queryParam("author",author).request("GET","/articles/");
        MArticlesResponse articlesResponse1 = mapper.readValue(articlsesListResponse.getBody().prettyPrint(),MArticlesResponse.class);
        articleList = articlesResponse1.getArticles();

        LogFactory.init();
        DriverManager.startBrowser();
        homePage = new HomePage();
        authorPage = new AuthorPage();
        signInPage = new SignInPage();
        basePage = new BasePage();
        basePage.openPage(HOME_URL);

//        Login
        homePage.click_on_button_sign_in();
        signInPage.enter_email(EMAIL);
        signInPage.enter_password(PASSWORD);
        signInPage.click_on_sign_in_button();
        Thread.sleep(2000);
        basePage.switchToNewTab();
        basePage.openPage(HOME_URL+"@"+author);

    }

    @AfterClass
    public void afterClass() {
        DriverManager.quitDriver();
    }

    @Test(description = "Verify Author Name")
    public void TC1_VerifyAuthorName() {
        authorPage.verify_author_name(author);
    }

    @Test(description = "Follow author")
    public void TC2_FollowAuthor() {
//        Before need unfollow_Author by call API
        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/profiles/"+author+"/follow");
//        Click follow button
        authorPage.click_follow_button();
    }

    @Test(description = "Unfollow author")
    public void TC3_UnfollowAuthor() {
//      Before _Follow user by call API
        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("POST","/profiles/"+author+"/follow");
//        Click follow button
        authorPage.click_unfollow_button();
    }

    @Test(description = "Verify Article list of the author")
    public void TC4_VerifyArticleList() throws JsonProcessingException {

        int index = articleList.size();
        if (index > 10) {
            index = 10;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);
        homePage.refreshPage();
        for (int i = 0; i < index; i++) {
            MArticle article = articleList.get(i);
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
                .when().request("DELETE", "/profiles/" + author + "/follow");
    }

    @Test(description = "Go to detailed article by click title")
    public void TC5_GotoDetailByClickingTitle() {
        for(MArticle article: articleList){
            homePage.click_article_title(article.getTitle(),HOME_URL+"article/"+article.getSlug());
            basePage.backToPreviousPage();
        }
    }

    @Test(description = "Go to Readmore button")
    public void TC6_GotoDetailByClickingReadmoreButton() {
//        Click Content
        for (MArticle article: articleList){
            homePage.click_readmore_button(article.getTitle(),HOME_URL+"article/"+article.getSlug());
            basePage.backToPreviousPage();
        }
    }

    @Test(description = "Go to detailed article by click tag")
    public void TC7_GotoDetailByClickingTag() {
        for (MArticle article: articleList){
            List<String> tags = new ArrayList<>();
            tags = article.getTagList();
            for (String tag: tags){
                homePage.click_article_tag(article.getTitle(),tags.indexOf(tag)+1,HOME_URL+"article/"+article.getSlug());
                basePage.backToPreviousPage();
            }
        }
    }

    @Test(description = "Switch to favorite tab")
    public void TC8_Witch_to_favorite_tab(){
        homePage.click_on_feed_tab("Favorited Articles");
        String actualUrl = homePage.getUrl();
        Assertions.assertThat(actualUrl).isEqualTo(HOME_URL+"@"+author+"/favorites");
    }

    @Test(description = "Verify favorites articles list")
    public void TC9_Verify_favorited_articles_list() throws JsonProcessingException {
//        Call API to get article list
        Response favoritedArticleList = with().header("Content-Type","application/json")
                .when().queryParam("favorited",author).request("GET","/articles");
        MArticlesResponse mArticlesResponse = mapper.readValue(favoritedArticleList.getBody().prettyPrint(),MArticlesResponse.class);
        List<MArticle> favoritedList = mArticlesResponse.getArticles();
        if(favoritedList.isEmpty()){
            homePage.verify_no_article_feed();
        }else {
            int index = favoritedList.size();
            if (index > 5) {
                index = 5;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);
            homePage.refreshPage();
            for (int i = 0; i < index; i++) {
                MArticle article = favoritedList.get(i);
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
        }

    }
}
