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
import testsuite.utils.ApiLogFactory;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.List;

import static io.restassured.RestAssured.with;
import static testsuite.config.ApiDataFactory.USER_001;
import static testsuite.config.ApiDataFactory.expired_token;

public class TestMarkUnFavoriteArticle {
    private final ObjectMapper mapper = new ObjectMapper();
    private StringWriter requestResponseLog;
    private PrintStream requestResponseCaptureLog;
    String token;
    String slug;

    @BeforeClass
    public void BeforeClass() throws JsonProcessingException {
        RestAssured.baseURI = ApiDataFactory.API_URL;
        requestResponseLog = ApiLogFactory.getWriter();
        requestResponseCaptureLog = ApiLogFactory.getStream();
        RestAssured.requestSpecification =
        RestAssured.given().filters(new RequestLoggingFilter(requestResponseCaptureLog)
                , new ResponseLoggingFilter(requestResponseCaptureLog));

//        Login and get token
        MUserDetail userDetail= ApiDataFactory.getUserByEmail(USER_001);
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
        requestResponseLog.write("\\n========== Mark unfavorite article ===========\\n");
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
        requestResponseLog.write("\\n========== Mark unfavorite article without token===========\\n");
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
        requestResponseLog.write("\\n========== Mark unfavorite article with invalid token ===========\\n");
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
        requestResponseLog.write("\\n========== Mark unfavorite article with expired token===========\\n");
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+expired_token)
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
        requestResponseLog.write("\\n========== Mark unfavorite article with non existing token===========\\n");
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
        requestResponseLog.write("\\n========== Mark unfavorite article without slug===========\\n");
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
        requestResponseLog.write("\\n========== Mark unfavorite article with non existing slug===========\\n");
        String nonExistingSlug = slug + System.currentTimeMillis();
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+nonExistingSlug+"favorite");
        Assertions.assertThat(response.statusCode()).as("Expected: 404").isEqualTo(404);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: article does not exist")
                .contains("article does not exist");
    }

    @Test(description = """
            Testcase UnMark article favorite unsuccessfully with unfavorited article
            Send delete to endpoint api/articles/{slug}/favrite
            Expect code and msg""")
    public void TC8_MarkUnFavoriteFailUnMarkedArticle() {
        requestResponseLog.write("\\n========== Mark unfavorite article for unfavorited articles ===========\\n");
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
