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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testsuite.config.ApiDataFactory;
import testsuite.model.article.MArticle;
import testsuite.model.article.MArticleResponse;
import testsuite.model.article.MArticlesResponse;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;

import java.util.List;

import static io.restassured.RestAssured.with;

public class TestMarkUnFavoriteArticle {
    private final ObjectMapper mapper = new ObjectMapper();
    String token;
    String slug;

    @BeforeClass
    public void BeforeClass() throws JsonProcessingException {
        RestAssured.baseURI = ApiDataFactory.API_URL;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

//        Login and get token
        MUserDetail userDetail= new MUserDetail();
        userDetail.setEmail("nguyenthucuc996+1009@gmail.com");
        userDetail.setPassword("123456");
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node =  mapper.valueToTree(user);
        Response response = with().headers("Content-Type","application/json")
                .when().body(node).request("POST","users/login");
        user = mapper.readValue(response.getBody().prettyPrint(),MUser.class);
        userDetail = user.getUser();
        token = userDetail.getToken();

//        Get article list and store slug
        Response response1 = with().header("Content-Type","application/json")
                .when().request("GET","/articles");
        MArticlesResponse articlesResponse = mapper.readValue(response1.getBody().prettyPrint(),MArticlesResponse.class);
        List<MArticle> articles = articlesResponse.getArticles();
        slug = articles.get(0).getSlug();
    }

    @BeforeMethod
    public void BeforeMethod() throws JsonProcessingException {
//        Mark article as favorited
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("GET","/articles/"+slug);
        MArticleResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(),MArticleResponse.class);
        MArticle article = articleResponse.getArticle();
        if(!article.isFavorited()){
            with().headers("Content-Type","application/json", "Authorization","Token "+token)
                    .when().request("POST","/articles/"+slug+"/favorite");
        }
    }

    @Test(description = """
            Testcase UnMark article favorite successfully
            Send delete to endpoint api/articles/{slug}/favorite
            Expect code  and response""")
    public void TC1_MarkUnFavoriteSuccess() throws JsonProcessingException {
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+slug+"/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected code: 200").isEqualTo(200);
        MArticleResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(),MArticleResponse.class);
        MArticle article = articleResponse.getArticle();
        Assertions.assertThat(article.isFavorited()).as("Expected: False").isEqualTo(false);
    }

    @Test(description = """
            Testcase Mark unfavorite article unsuccessfully with empty token
            Send delete to endpoint api/articles/{slug}/favorite
            Expect code and msg""")
    public void TC2_MarkUnFavoriteFailEmptyToken() {
        Response response = with()
                .when().request("DELETE","/articles/"+slug+"/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected: 403").isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: authentication required")
                .contains("authentication required");
    }

    @Test(description = """
            Testcase Mark unfavorite article unsuccessfully with invalid token
            Send delete to endpoint api/articles/{slug}/favorite
            Expect code and msg""")
    public void TC3_MarkUnFavoriteFailInvalidToken() {
        Response response = with().headers("Content-Type","application/json", "Authorization","Token"+token)
                .when().request("DELETE","/articles/"+slug+"/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected:403 ").isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: unsupported authorization type")
                .contains("unsupported authorization type");
    }

    @Test(description = """
            Testcase Mark unfavorite article unsuccessfully with expired token
            Send delete to endpoint api/articles/{slug}/favorite
            Expect code and msg""")
    public void TC4_MarkUnFavoriteFailExpiredToken() {
        String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Im1hcmlnb2xkMDkiLCJleHAiOjE3NDY5NzMzNjYsInN1YiI6ImFjY2VzcyJ9.w3-XXsq1yZX6ItP3aBOFBjiYWPeTQ72pADYH504hV-Y";
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+expiredToken)
                .when().request("DELETE","/articles/"+slug+"/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected code: 403").isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: could not validate credentials")
                .contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Mark unfavorite article unsuccessfully with non existing token
            Send delete to endpoint api/articles/{slug}/favorite
            Expect code and msg""")
    public void TC5_MarkUnFavoriteFailNonExistingToken() {
        String nonExistingToken = "abcdefghydjd";
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+nonExistingToken)
                .when().request("DELETE","/articles/"+slug+"/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected: 403").isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: could not validate credentials")
                .contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Mark unfavorite article unsuccessfully with empty slug
            Send delete to endpoint api/articles/{slug}/favorite
            Expect code and msg""")
    public void TC6_MarkUnFavoriteFailEmptySlug() {
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected: 404").isEqualTo(404);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: article does not exist")
                .contains("article does not exist");
    }

    @Test(description = """
            Testcase UnMark article favorite unsuccessfully with Non existing slug
            Send delete to endpoint api/articles/{slug}/favorite
            Expect code and msg""")
    public void TC7_MarkUnFavoriteFailNonExistingSlug() {
        String nonExistingSlug = slug + System.currentTimeMillis();
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+nonExistingSlug+"favorite");
        Assertions.assertThat(response.statusCode()).as("Expected: 404").isEqualTo(404);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: article does not exist")
                .contains("article does not exist");
    }

    @Test(description = """
            Testcase UnMark article favorite unsuccessfully with unfavrited article
            Send delete to endpoint api/articles/{slug}/favrite
            Expect code and msg""")
    public void TC8_MarkUnFavoriteFailUnMarkedArticle() {
        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+slug+"/favorite");
        Response response= with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+slug+"/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected code: 400").isEqualTo(400);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: article is not favorited")
                .contains("article is not favorited");
    }
}
