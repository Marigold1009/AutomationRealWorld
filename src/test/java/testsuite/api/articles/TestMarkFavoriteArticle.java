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
import testsuite.model.article.MArticle;
import testsuite.model.article.MArticleResponse;
import testsuite.model.article.MArticlesResponse;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;

import java.util.List;

import static io.restassured.RestAssured.with;

public class TestMarkFavoriteArticle {
    private final ObjectMapper mapper = new ObjectMapper();
    private String token;
    MArticle articleTest = new MArticle();
    String slug;

    @BeforeClass
    public void BeforeClass() throws JsonProcessingException {
        RestAssured.baseURI = "https://realworld-api.ap.ngrok.io/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

//        login and store token
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail("nguyenthucuc996@gmail.com");
        userDetail.setPassword("Cucvantho09");
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = with().headers("Content-Type", "application/json")
                .when()
                .body(node)
                .request("POST", "users/login");
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        token = user.getUser().getToken();

//        Get list article and unmark 1 article
        Response articleList = with().header("Content-Type", "application/json")
                .when().request("GET", "/articles");
        MArticlesResponse articleResponse = mapper.readValue(articleList.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList1 = articleResponse.getArticles();
        articleTest = articleList1.get(0);
        slug = articleTest.getSlug();
    }

    @BeforeMethod
    public void BeforeMethod() throws JsonProcessingException {
//        Remove mark specific an article
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when()
                .request("GET", "/articles/" + slug);
        MArticleResponse mArticleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticleResponse.class);
        MArticle article = mArticleResponse.getArticle();
        if (article.isFavorited()) {
            with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                    .when().request("DELETE", "articles/" + slug + "/favorite");
        }
    }

    @Test(description = """
            Testcase Mark article favourite successfully
            Send post to endpoint api/articles/{slug}/favorite
            Expect code  and response""")
    public void TC1_MarkFarvouriteSuccess() throws JsonProcessingException {
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when()
                .request("POST", "articles/" + slug + "/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected code 200").isEqualTo(200);
        MArticleResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticleResponse.class);
        MArticle article = articleResponse.getArticle();
        Assertions.assertThat(article.isFavorited()).as("Expected is true").isEqualTo(true);
    }

    @Test(description = """
            Testcase Mark article favourite unsuccessfully with empty token
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC2_MarkFarvouriteFailEmptyToken() throws JsonProcessingException {
        Response response = with().headers("Content-Type", "application/json")
                .when()
                .request("POST", "articles/" + slug + "/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected stt: 403").isEqualTo(403);
        String ressponseString = response.getBody().prettyPrint();
        Assertions.assertThat(ressponseString).as("Expected msg: authentication required").contains("authentication required");
    }

    @Test(description = """
            Testcase Mark article favourite unsuccessfully with invalid token
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC3_MarkFarvouriteFailInvalidToken() {
        Response response = with().headers("Content-Type", "application/json", "Authorization" ,"Authorization"+ token)
                .when()
                .request("POST", "/articles/" + slug + "/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected code: 403").isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: unsupported authorization type")
                .contains("unsupported authorization type");
    }

    @Test(description = """
            Testcase Mark article favourite unsuccessfully with expired token
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC4_MarkFarvouriteFailExpiredToken() {
        String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Im1hcmlnb2xkMDkiLCJleHAiOjE3NDY5NzMzNjYsInN1YiI6ImFjY2VzcyJ9.w3-XXsq1yZX6ItP3aBOFBjiYWPeTQ72pADYH504hV-Y";
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + expiredToken)
                .when()
                .request("POST", "/articles/" + slug + "/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected code: 403").isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: could not validate credentials")
                .contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Mark article favourite unsuccessfully with wrong format token
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC5_MarkFarvouriteFailWrongFormatToken() {
        Response response = with().headers("Content-Type", "application/json", "Authorization" ,token)
                .when()
                .request("POST", "/articles/" + slug + "/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected code: 403").isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: unsupported authorization type")
                .contains("unsupported authorization type");
    }

    @Test(description = """
            Testcase Mark article favourite unsuccessfully with empty slug
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC6_MarkFarvouriteFailEmptySlug() {
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when()
                .request("POST", "/articles/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected code: 405").isEqualTo(405);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: Method Not Allowed")
                .contains("Method Not Allowed");
    }

    @Test(description = """
            Testcase Mark article favourit unsuccessfully with Non existing slug
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC7_MarkFarvouriteFailNonExistingSlug() {
        String nonExistingslug = slug + System.currentTimeMillis();
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when()
                .request("POST", "/articles/" + nonExistingslug + "/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected code: 404").isEqualTo(404);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: article does not exist")
                .contains("article does not exist");
    }

    @Test(description = """
            Testcase Mark article favourit unsuccessfully with marked slug
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC8_MarkFarvouriteFailMarkedSlug() {
        with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().request("POST", "articles/" + slug + "/favorite");
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().request("POST", "articles/" + slug + "/favorite");
        Assertions.assertThat(response.statusCode()).as("Expected code: 400").isEqualTo(400);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: you are already marked this articles as favorite")
                .contains("you are already marked this articles as favorite");
    }
}
