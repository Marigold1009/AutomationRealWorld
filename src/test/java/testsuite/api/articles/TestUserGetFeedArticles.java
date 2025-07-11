package testsuite.api.articles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testsuite.config.ApiDataFactory;
import testsuite.model.article.*;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static io.restassured.RestAssured.with;

public class TestUserGetFeedArticles {
    private final ObjectMapper mapper = new ObjectMapper();
    String token = "";
    List<String> authors = new ArrayList<>();
    List<MAuthor> authorObject = new ArrayList<>();
    int total;
    List<MArticle> articleListDefaultParam = new ArrayList<>();

    @BeforeClass
    public void beforeClass() throws JsonProcessingException {
//        list base url
        RestAssured.baseURI = ApiDataFactory.API_URL;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

//        Login and store toke
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail("nguyenthucuc996+1009@gmail.com");
        userDetail.setPassword("123456");
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = with().headers("Content-Type", "application/json")
                .when()
                .body(node)
                .request("POST", "/users/login");
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        token = user.getUser().getToken();

//        Prepare data to get feed
        Response responseArticle = with().request("GET", "articles");
        MArticlesResponse articleResponse = mapper.readValue(responseArticle.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList = articleResponse.getArticles();
        for (MArticle article : articleList) {
            MAuthor author01 = article.getAuthor();
            if (!author01.getUsername().equals("marigold09")) {
                authors.add(author01.getUsername());
                authorObject.add(author01);
            }
        }
        authors = authors.subList(0, 3);
        authorObject = authorObject.subList(0, 3);
        List<String> uniqueAthorFollow = new ArrayList<>(new LinkedHashSet<>(authors));

        for (String author : uniqueAthorFollow) {
            Response responseFollow = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                    .when()
                    .pathParam("username", author)
                    .post("/profiles/{username}/follow");
        }
    }

    @Test(description = """
            Test case default params
            Send get to endpoint: api/articles/feeds
            Expect total articles equals or less than 20, start at latest article""", priority = 0)
    public void TC1_GetFeedsWithoutParam() throws JsonProcessingException {
//        Send request
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when()
                .request("GET", "articles/feed");

//        Assert
        Assertions.assertThat(response.statusCode()).as("Expected: 200").isEqualTo(200);
        MArticlesResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        total = articleResponse.getArticlesCount();
        articleListDefaultParam = articleResponse.getArticles();
        for (MArticle article : articleListDefaultParam) {
            Assertions.assertThat(authors).contains(article.getAuthor().getUsername());
        }
        Assertions.assertThat(articleListDefaultParam.size()).isLessThanOrEqualTo(20);
        Instant latestCreated = Instant.parse(articleListDefaultParam.get(0).getCreatedAt());
        boolean checkLatest = true;
        Instant articleCreatedAt;
        for (int i = 1; i < articleListDefaultParam.size(); i++) {
            articleCreatedAt = Instant.parse(articleListDefaultParam.get(i).getCreatedAt());
            if (latestCreated.isBefore(articleCreatedAt)) {
                checkLatest = false;
                break;
            }
        }
        Assert.assertTrue(checkLatest);
    }

    @Test(description = """
            Test case with valid param
            Send get to endpoint: api/articles/feeds
            Expect: total articles equal <=limit, end first item at offset article""", priority = 1)
    public void TC2_GetFeedsWithValidParam() throws JsonProcessingException {
        int limit = 0;
        int offset = 0;
        if (total >= 2) {
            limit = total - 1;
            offset = 1;
        } else {
            limit = total;
            offset = 0;
        }

//        Send request
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .when()
                .request("GET", "articles/feed");

//        Assert
        MArticlesResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articlesList = articleResponse.getArticles();
        Assertions.assertThat(response.statusCode()).as("Expected: 200").isEqualTo(200);
        for (MArticle article : articlesList) {
            Assertions.assertThat(authors).as("Expected: author in the list").contains(article.getAuthor().getUsername());
        }
        Assertions.assertThat(articleResponse.getArticlesCount()).as("Total equals limit").isLessThanOrEqualTo(limit);
    }

    @Test(description = """
            Test case with valid limit
            Send get to endpoint: api/articles/feeds
            Expect: total items <= limit,first item = latest item""", priority = 1)
    public void TC3_GetFeedsWithValidLimit() throws JsonProcessingException {
//        Send request
        int limit = 0;
        if (total > 2) {
            limit = total - 1;
        }
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .queryParam("limit", limit)
                .when()
                .request("GET", "articles/feed");

//        Assert
        MArticlesResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList = articleResponse.getArticles();
        Assertions.assertThat(response.statusCode()).as("Expected code: 200").isEqualTo(200);
        for (MArticle article : articleList) {
            Assertions.assertThat(authors).as("Author in the list").contains(article.getAuthor().getUsername());
        }
        Assertions.assertThat(articleResponse.getArticlesCount()).as("Total equals limit").isLessThanOrEqualTo(limit);
    }

    @Test(description = """
            Testcase: Input expired token
            Send get to endpoint: api/articles/feeds
            Expect: Show msg""",priority = 2)
    public void TC11_GetFeedsWithExpiredToken() {
        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Im1hcmlnb2xkMDkiLCJleHAiOjE3NDY5NzMzNjYsInN1YiI6ImFjY2VzcyJ9.w3-XXsq1yZX6ItP3aBOFBjiYWPeTQ72pADYH504hV-Y";
        Response response = with().headers("Content-Type", "application/json", "Authorization ", "Token " + token)
                .when()
                .request("GET", "articles/feed");
        Assertions.assertThat(response.statusCode()).as("Expected: 400").isEqualTo(400);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected: could not validate credentials")
                .contains("invalid header name");
    }

    @Test(description = """
            Testcase with valid  offest
            Send get to endpoint:api/articles/feeds
            Expect: total item<=20, start at offset""", priority = 1)
    public void TC4_GetFeedWithValidOffset() throws JsonProcessingException {
        int offset = 0;
        if (total > 2) {
            offset = 2;
        }
//        Send request
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .queryParam("offset", offset)
                .when()
                .request("GET", "articles/feed");
        MArticlesResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList = articleResponse.getArticles();
        for (MArticle article : articleList) {
            Assertions.assertThat(authors).as("Author in the list").contains(article.getAuthor().getUsername());
        }
        Assertions.assertThat(response.statusCode()).as("Expected code: 200").isEqualTo(200);
        Assertions.assertThat(articleResponse.getArticlesCount()).as("Expected<=20").isLessThanOrEqualTo(20);

    }

    @Test(description = """
            Testcase with invalid params
            Send get to endPoint: api/articles/feed
            Expect: Show msg""", priority = 1)
    public void TC5_GetFeedWithInvalidParam() throws JsonProcessingException {
//        Send request
        String limit = "limit";
        String offset = "offset";
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .when()
                .request("GET", "articles/feed");

//       Assert
        MErrorResponse errorResponse = mapper.readValue(response.getBody().prettyPrint(), MErrorResponse.class);
        List<MErrorDetails> errorDetails = errorResponse.getErrors();
        for (MErrorDetails error : errorDetails) {
            Assertions.assertThat(error.getMsg()).as("value is not a valid integer").contains("value is not a valid integer");
            Assertions.assertThat(error.getType()).as("type_error.integer").contains("type_error.integer");
        }
        Assertions.assertThat(errorDetails.get(0).getLoc().get(1)).as("Expected: limit").contains("limit");
        Assertions.assertThat(errorDetails.get(1).getLoc().get(1)).as("Expected: offset").contains("offset");
    }

    @Test(description = """
            Testcase with invalid limit, valid offset
            Send get to endPoint: api/articles/feed
            Expect: Show msg""", priority = 1)
    public void TC6_GetFeedWithInvalidLimitValidOffset() throws JsonProcessingException {
//        Send request and get response
        int limit = 0;
        int offset = 0;
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .when()
                .request("GET", "articles/feed");

//        Assert
        MErrorResponse errorResponse = mapper.readValue(response.getBody().prettyPrint(), MErrorResponse.class);
        List<MErrorDetails> errorDetails = errorResponse.getErrors();
        Assertions.assertThat(response.statusCode()).as("Expected code: 422").isEqualTo(422);
        Assertions.assertThat(errorDetails.get(0).getLoc().get(1)).as("Expected: limit").contains("limit");
        Assertions.assertThat(errorDetails.get(0).getMsg()).as("ensure this value is greater than or equal to 1")
                .contains("ensure this value is greater than or equal to 1");
    }

    @Test(description = """
            Testcase with invalid offset, valid limit
            Send get to endPoint: api/articles/feed
            Expect: Show msg""",priority = 1)
    public void TC7_GetFeedWithValidLimitInvalidOffset() throws JsonProcessingException {
//      Send request
        int offset = total;
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .queryParam("offset", offset)
                .when()
                .request("GET", "articles/feed");

//        Assert
        Assertions.assertThat(response.statusCode()).as("Expected code: 200").isEqualTo(200);
        MArticlesResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        Assertions.assertThat(articleResponse.getArticlesCount()).as("Expected: 0").isEqualTo(0);
    }

    @Test(description = """
            Testcase with invalid limit
            Send get to endPoint: api/articles/feed
            Expect: Show msg""",priority = 1)
    public void TC8_GetFeedWithInvalidPLimit() throws JsonProcessingException {
//        Send request
        int limit = -1;
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .queryParam("limit", limit)
                .when()
                .request("GET", "articles/feed");

//        Asset
        MErrorResponse errorResponse = mapper.readValue(response.getBody().prettyPrint(), MErrorResponse.class);
        List<MErrorDetails> errorDetails = errorResponse.getErrors();
        Assertions.assertThat(response.statusCode()).as("Expected: 422").isEqualTo(422);
        Assertions.assertThat(errorDetails.get(0).getLoc().get(1)).as("Expected: limit").contains("limit");
        Assertions.assertThat(errorDetails.get(0).getMsg()).as("Expected: ensure this value is greater than or equal to 1")
                .contains("ensure this value is greater than or equal to 1");
    }

    @Test(description = """
            Testcase with invalid offset, string
            Send get to endPoint: api/articles/feed
            Expect: Show msg""",priority = 1)
    public void TC9_GetFeedWithInvalidOffset() throws JsonProcessingException {
        String offset = "offset";
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .queryParam("offset", offset)
                .when()
                .request("GET", "articles/feed");

        Assertions.assertThat(response.statusCode()).as("Expected: 422").isEqualTo(422);
        MErrorResponse errorResponse = mapper.readValue(response.getBody().prettyPrint(), MErrorResponse.class);
        List<MErrorDetails> errorDetails = errorResponse.getErrors();
        Assertions.assertThat(errorDetails.get(0).getLoc().get(1)).as("Expected: offset").contains("offset");
        Assertions.assertThat(errorDetails.get(0).getMsg()).as("Expected: value is not a valid integer")
                .contains("value is not a valid integer");
    }

    @Test(description = """
            Testcase: No set token into header
            Send get to endpoint: api/articles/feeds
            Expect: Show msg""",priority = 1)
    public void TC10_GetFeedsWithoutAuthorize() {
        Response response = with().headers("Content-Type", "application/json")
                .when()
                .request("GET", "/articles/feed");

        Assertions.assertThat(response.statusCode()).as("Expected: 403").isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected: authentication required")
                .contains("authentication required");
    }

    @Test(description = """
            Testcase: Input invalid token
            Send get to endpoint: api/articles/feeds
            Expect: Show msg""",priority = 1)
    public void TC12_GetFeedsWithInvalidToken() {
        Response response = with().headers("Content-Type", "application/json", "Authorization ", token)
                .when()
                .request("GET", "articles/feed");
        Assertions.assertThat(response.statusCode()).as("Expected: 403").isEqualTo(400);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected: unsupported authorization type")
                .contains("invalid header name");
    }
}
