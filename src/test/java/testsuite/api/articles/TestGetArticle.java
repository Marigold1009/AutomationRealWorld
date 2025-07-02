package testsuite.api.articles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testsuite.model.article.MArticle;
import testsuite.model.article.MArticleResponse;
import testsuite.model.article.MArticlesResponse;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;

import java.util.List;

import static io.restassured.RestAssured.with;

public class TestGetArticle {
    private final ObjectMapper mapper = new ObjectMapper();
    private  String token;
    private String slug;
    private MArticle articleLogin;
    private MArticle articlesGuest;

    @BeforeClass
    public void BeforeClass() throws JsonProcessingException {
        RestAssured.baseURI =  "https://realworld-api.ap.ngrok.io/api";
        RestAssured.filters(new RequestLoggingFilter(),new ResponseLoggingFilter());

//        Login and store token
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail("nguyenthucuc996@gmail.com");
        userDetail.setPassword("Cucvantho09");
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = with().headers("Content-Type","application/json")
                .when()
                .body(node)
                .request("POST","/users/login");
        user = mapper.readValue(response.getBody().prettyPrint(),MUser.class);
        userDetail = user.getUser();
        token = userDetail.getToken();

//        Get Article list to get slug login mode
        Response articleList = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when()
                .request("GET","/articles");
        MArticlesResponse articlesResponse = mapper.readValue(articleList.getBody().prettyPrint(),MArticlesResponse.class);
        List<MArticle> articleList1 = articlesResponse.getArticles();
        slug = articleList1.get(0).getSlug();
        articleLogin = articleList1.get(0);

//        Get article list guest mode
        Response response1 = with().when().request("GET","/articles");
        MArticlesResponse articlesResponse1 = mapper.readValue(response1.getBody().prettyPrint(),MArticlesResponse.class);
        List<MArticle> articleList2 = articlesResponse1.getArticles();
        articlesGuest = articleList2.get(0);
    }

    @Test(description = """
            Testcase: Get Article successfilly
            Send Get toendpoint: /articles/{slug}
            Expected code 200, and return article""")
    public void TC1_GetArticleSuccess() throws JsonProcessingException {
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when()
                .request("GET","/articles/"+slug);
        Assertions.assertThat(response.statusCode()).as("Expected code: 200").isEqualTo(200);
        MArticleResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(),MArticleResponse.class);
        MArticle article1 = articleResponse.getArticle();
        Assertions.assertThat(article1).usingRecursiveComparison().isEqualTo(articleLogin);
    }

    @Test(description = """
            Testcase: Guest get article success
            Send Get to end point: /articles/{slug}
            Expected code: 200 and return article""")
    public void TC2_GuestGetArticlesSuccess() throws JsonProcessingException {
        Response response = with().headers("Content-Type","application/json")
                .when().request("GET","/articles/"+slug);
        MArticleResponse article1 = mapper.readValue(response.getBody().prettyPrint(),MArticleResponse.class);
        MArticle article2 = article1.getArticle();
        Assertions.assertThat(response.statusCode()).as("Expected code: 200").isEqualTo(200);
        Assertions.assertThat(article2).usingRecursiveComparison().isEqualTo(articlesGuest);
    }

    @Test(description = """
            Testcase: Can not get article with emty slug
            Send get to end point: /articles
            Expected code: 422 and msg""")
    public void TC3_GetArticleFail_InvalidSlug(){
        String invalidSlug = slug + System.currentTimeMillis();
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("GET","/articles/"+invalidSlug);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).as("Expected code: 404").isEqualTo(404);
        Assertions.assertThat(responseString).as("Expected msg: article does not exist")
                .contains("article does not exist");
    }

    @Test(description = """
            Testcase: Get article fail with wrong format token
            Send Get to end point: /articles/{slug}
            Expected code and msg""")
    public void TC4_TestArticleFail_WrongFormatToken(){
        Response response = with().headers("Content-Type","application/json","Authorization","Token"+token)
                .when().request("GET","/articles/"+slug);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).as("Expected code : 403").isEqualTo(403);
        Assertions.assertThat(responseString).as("Expected msg: unsupported authorization type").contains("unsupported authorization type");
    }

    @Test(description = """
            Testcase: Get article fail with expired token
            Send get to end point: /articles/{slug}
            Expected code and msg""")
    public void TC5_GetArticleFail_ExpiredToken(){
        String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Im1hcmlnb2xkMDkiLCJleHAiOjE3NDY5NzMzNjYsInN1YiI6ImFjY2VzcyJ9.w3-XXsq1yZX6ItP3aBOFBjiYWPeTQ72pADYH504hV-Y";
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+expiredToken)
                .when().request("GET","/articles/"+slug);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).as("Expected code: 403").isEqualTo(403);
        Assertions.assertThat(responseString).as("Expected msg: could not validate credentials")
                .contains("could not validate credentials");
    }
}
