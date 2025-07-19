package testsuite.api.articles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import model.article.MArticle;
import model.article.MArticleCreate;
import model.article.MArticleResponse;
import model.article.MArticlesResponse;
import model.user.MUser;
import model.user.MUserDetail;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testsuite.config.ApiDataFactory;
import utils.logging.LogFactory;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.with;
import static testsuite.config.ApiDataFactory.USER_001;
import static testsuite.config.ApiDataFactory.expired_token;

public class TestUpdateArticle {
    private final ObjectMapper mapper = new ObjectMapper();
    private StringWriter requestResponseLog;
    private PrintStream requestResponseCaptureStream;
    private String token;
    String userName;
    private String validSlug;
    private String invalidSlug;
    private String NoAuthorSlug;
    private MArticle validArticle = new MArticle();

    @BeforeClass
    public void BeforeClass() throws JsonProcessingException {
        RestAssured.baseURI = ApiDataFactory.API_URL;
        requestResponseLog = LogFactory.getWriter();
        requestResponseCaptureStream = LogFactory.getStream();
        RestAssured.requestSpecification =
        RestAssured.given().filters(new RequestLoggingFilter(requestResponseCaptureStream)
                , new ResponseLoggingFilter(requestResponseCaptureStream));

//        Login and store token
        MUserDetail userDetail = ApiDataFactory.getUserByEmail(USER_001);
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);

        Response response = with().headers("Content-Type", "application/json")
                .when().body(node).request("POST", "/users/login");
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        userDetail = user.getUser();
        token = userDetail.getToken();
        userName = userDetail.getUsername();

//        create an article and get slug
        MArticle articleCreate = new MArticle();
        articleCreate.setTitle("create article");
        articleCreate.setDescription("Article created by script");
        articleCreate.setBody("This is an article created by script");
        articleCreate.setTagList(Arrays.asList("autocreate", "marigold09Create"));
        MArticleCreate articleCreate1 = new MArticleCreate();
        articleCreate1.setArticle(articleCreate);
        JsonNode node1 = mapper.valueToTree(articleCreate1);
        Response response1 = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(node1).request("POST", "/articles");
        articleCreate1 = mapper.readValue(response1.getBody().prettyPrint(), MArticleCreate.class);
        validArticle = articleCreate1.getArticle();
        validSlug = validArticle.getSlug();
        invalidSlug = validSlug + System.currentTimeMillis();

//        Get article and get 1 slug of another author
        Response response2 = with().request("GET", "/articles");
        MArticlesResponse articlesResponse1 = mapper.readValue(response2.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList = articlesResponse1.getArticles();
        for (MArticle article : articleList) {
            if (!article.getAuthor().getUsername().equals(userName)) {
                NoAuthorSlug = article.getSlug();
                break;
            }
        }
    }

    @Test(description = """
            Testcase Update article success
            Send Put to end point: /articles/{slug}
            Expected result: 200 and return article with new info""")
    public void TC1_UpdateArticleSuccessAllFields() throws JsonProcessingException {
        MArticle articleUpdate = new MArticle();
        String titleUpdate = validArticle.getTitle() + System.currentTimeMillis();
        String descriptionUpdate = "This is description after updating article";
        String bodyUpdate = "This is body after updating article";
        articleUpdate.setTitle(titleUpdate);
        articleUpdate.setDescription(descriptionUpdate);
        articleUpdate.setBody(bodyUpdate);
        MArticleCreate article = new MArticleCreate();
        article.setArticle(articleUpdate);

        JsonNode node = mapper.valueToTree(article);
        requestResponseLog.write("\\n========== Update article ===========\\n");
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(node).request("PUT", "/articles/" + validSlug);
        MArticleResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticleResponse.class);
        MArticle article1 = articleResponse.getArticle();
        Assertions.assertThat(response.statusCode()).as("Expected stt: 200").isEqualTo(200);
        Assertions.assertThat(article1.getTitle()).as("Expected: s;ugUpdate").contains(titleUpdate);
        Assertions.assertThat(article1.getDescription()).as("Expected: description update").contains(descriptionUpdate);
        Assertions.assertThat(article1.getBody()).as("Expected: body update").contains(bodyUpdate);
        validSlug = article1.getSlug();
    }

    @Test(description = """
            Testcase: Update title article sucessfully
            Send PUT to end point: /articles/{slug}
            Expected stt 200 and return article""")
    public void TC2_UpdateArticle_Title_Success() throws JsonProcessingException {
        MArticle articleUpdate = new MArticle();
        articleUpdate = validArticle;
        articleUpdate.setTitle(validArticle.getTitle() + System.currentTimeMillis());
        MArticleCreate articleUpdate01 = new MArticleCreate();
        articleUpdate01.setArticle(articleUpdate);
        JsonNode node = mapper.valueToTree(articleUpdate01);
        requestResponseLog.write("\\n========== Update article title ===========\\n");
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .body(node).request("PUT", "/articles/" + validSlug);
        MArticleResponse mArticleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticleResponse.class);
        MArticle articleResponse = mArticleResponse.getArticle();
        Assertions.assertThat(response.statusCode()).as("Expected: 200").isEqualTo(200);
        Assertions.assertThat(articleResponse.getTitle()).contains(articleUpdate.getTitle());
        Assertions.assertThat(articleResponse.getBody()).contains(articleUpdate.getBody());
        Assertions.assertThat(articleResponse.getDescription()).contains(articleUpdate.getDescription());
        validSlug = articleResponse.getSlug();
    }

    @Test(description = """
            Testcase: Update body article successfully
            Send PUT to end point: /articles/{slug}
            Expected stt: 200 and return article""")
    public void TC3_UpdateArticle_Body_Success() throws JsonProcessingException {
        MArticle articleUpdate = new MArticle();
        articleUpdate = validArticle;
        articleUpdate.setBody(validArticle.getBody() + System.currentTimeMillis());
        MArticleCreate articleUpdate01 = new MArticleCreate();
        articleUpdate01.setArticle(articleUpdate);
        JsonNode node = mapper.valueToTree(articleUpdate01);
        requestResponseLog.write("\\n========== Update article body===========\\n");
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .body(node).request("PUT", "/articles/" + validSlug);
        MArticleResponse mArticleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticleResponse.class);
        MArticle articleResponse = mArticleResponse.getArticle();
        Assertions.assertThat(response.statusCode()).as("Expected: 200").isEqualTo(200);
        Assertions.assertThat(articleResponse.getTitle()).contains(articleUpdate.getTitle());
        Assertions.assertThat(articleResponse.getBody()).contains(articleUpdate.getBody());
        Assertions.assertThat(articleResponse.getDescription()).contains(articleUpdate.getDescription());
    }

    @Test(description = """
            Testcase: Update Description successfully
            Send PUT to end point: /articles/{slug}
            Expected stt: 200 and return article""")
    public void TC4_UpdateArticle_Description_Success() throws JsonProcessingException {
        MArticle articleUpdate = new MArticle();
        articleUpdate = validArticle;
        articleUpdate.setDescription(validArticle.getBody() + System.currentTimeMillis());
        MArticleCreate articleUpdate01 = new MArticleCreate();
        articleUpdate01.setArticle(articleUpdate);
        JsonNode node = mapper.valueToTree(articleUpdate01);
        requestResponseLog.write("\\n========== Update article description ===========\\n");
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .body(node).request("PUT", "/articles/" + validSlug);
        MArticleResponse mArticleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticleResponse.class);
        MArticle articleResponse = mArticleResponse.getArticle();
        Assertions.assertThat(response.statusCode()).as("Expected: 200").isEqualTo(200);
        Assertions.assertThat(articleResponse.getTitle()).contains(articleUpdate.getTitle());
        Assertions.assertThat(articleResponse.getBody()).contains(articleUpdate.getBody());
        Assertions.assertThat(articleResponse.getDescription()).contains(articleUpdate.getDescription());
    }

    @Test(description = """
            Testcase: Update Article Unsuccessfully without header
            Send PUT to end point: /articles/{slug}
            Expected stt:  and return msg""")
    public void TC5_UpdateArticle_Fail_NoHeader() {
        MArticle articleUpdate = new MArticle();
        articleUpdate = validArticle;
        articleUpdate.setDescription(validArticle.getBody() + System.currentTimeMillis());
        MArticleCreate articleUpdate01 = new MArticleCreate();
        articleUpdate01.setArticle(articleUpdate);
        JsonNode node = mapper.valueToTree(articleUpdate01);
        requestResponseLog.write("\\n========== Update article without token ===========\\n");
        Response response = with()
                .body(node).request("PUT", "/articles/" + validSlug);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).as("Expected code: 403").isEqualTo(403);
        Assertions.assertThat(responseString).as("Expected msg: authentication required").contains("authentication required");
    }

    @Test(description = """
            Testcase: Update Article Unsuccessfully with invalid token
            Send PUT to end point: /articles/{slug}
            Expected stt:  and return msg""")
    public void TC6_UpdateArticle_Fail_InvalidToken() {
        MArticle articleUpdate = new MArticle();
        articleUpdate = validArticle;
        articleUpdate.setDescription(validArticle.getBody() + System.currentTimeMillis());
        MArticleCreate articleUpdate01 = new MArticleCreate();
        articleUpdate01.setArticle(articleUpdate);
        JsonNode node = mapper.valueToTree(articleUpdate01);
        requestResponseLog.write("\\n========== Update article with invalid token ===========\\n");
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token" + token)
                .body(node).when().request("PUT", "/articles/" + validSlug);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).as("Expected code 403").isEqualTo(403);
        Assertions.assertThat(responseString).as("unsupported authorization type").contains("unsupported authorization type");
    }

    @Test(description = """
            Testcase: Update Articles Unsuccessfully with expired token
            Send PUT to end point: /articles/{slug}
            Expected stt and msg""")
    public void TC7_UpdateArticle_Fail_ExpiredToken() {
        MArticle articleUpdate = new MArticle();
        articleUpdate = validArticle;
        articleUpdate.setDescription(validArticle.getBody() + System.currentTimeMillis());
        MArticleCreate articleUpdate01 = new MArticleCreate();
        articleUpdate01.setArticle(articleUpdate);
        JsonNode node = mapper.valueToTree(articleUpdate01);
        requestResponseLog.write("\\n========== Update article with expired token ===========\\n");
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + expired_token)
                .body(node).when().request("PUT", "/articles/" + validSlug);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).as("Expected code 403").isEqualTo(403);
        Assertions.assertThat(responseString).as("could not validate credentials").contains("could not validate credentials");
    }

    @Test(description = """
            Testcase: Update Article Unsuccessfully with empty article
            Send PUT to end point: /articles/{slug}
            Expected code: and msg""")
    public void TC8_UpdateArticle_Fail_EmptyTitle() throws JsonProcessingException {
        MArticle articleUpdate = new MArticle();
        articleUpdate.setTitle(null);
        articleUpdate.setBody(validArticle.getBody() + System.currentTimeMillis());
        articleUpdate.setDescription(validArticle.getDescription() + System.currentTimeMillis());
        MArticleCreate articleUpdate01 = new MArticleCreate();
        articleUpdate01.setArticle(articleUpdate);
        JsonNode node = mapper.valueToTree(articleUpdate01);
        requestResponseLog.write("\\n========== Update article with empty titile ===========\\n");
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .body(node).when().request("PUT", "/articles/" + validSlug);
        Assertions.assertThat(response.statusCode()).as("Expected stt 200").isEqualTo(200);
        MArticleResponse mArticleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticleResponse.class);
        articleUpdate = mArticleResponse.getArticle();
        Assertions.assertThat(articleUpdate.getTitle()).contains(validArticle.getTitle());
        Assertions.assertThat(articleUpdate.getBody()).contains(validArticle.getBody());
        Assertions.assertThat(articleUpdate.getDescription()).contains(validArticle.getDescription());
    }

    @Test(description = """
            Testcase: Update Article Unsuccessfully with invalid slug
            Send PUT to end point: /articles/{slug}
            Expected code: msg """)
    public void TC9_UpdateArticle_Fail_InvalidSlug() {
        MArticle articleUpdate = new MArticle();
        articleUpdate = validArticle;
        articleUpdate.setDescription(validArticle.getBody() + System.currentTimeMillis());
        MArticleCreate articleUpdate01 = new MArticleCreate();
        articleUpdate01.setArticle(articleUpdate);
        JsonNode node = mapper.valueToTree(articleUpdate01);
        requestResponseLog.write("\\n========== Update article with invalid slug ===========\\n");
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .body(node).when().request("PUT", "/articles/" + NoAuthorSlug);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).as("Expected code 403").isEqualTo(403);
        Assertions.assertThat(responseString).as("you are not an author of this article")
                .contains("you are not an author of this article");
    }

    @Test(description = """
            Testcase: Update Article Unsuccessfully without slug
            Send PUT to endpoint: /articles/
            Expected code: 405 and msg: Method Not Allowed""")
    public void TC10_UpdateArticles_Fail_EmptySlug() {
        MArticle articleUpdate = new MArticle();
        articleUpdate = validArticle;
        articleUpdate.setDescription(validArticle.getBody() + System.currentTimeMillis());
        MArticleCreate articleUpdate01 = new MArticleCreate();
        articleUpdate01.setArticle(articleUpdate);
        JsonNode node = mapper.valueToTree(articleUpdate01);
        requestResponseLog.write("\\n========== Update article with empty slug ===========\\n");
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .body(node).when().request("PUT", "/articles");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(405);
        Assertions.assertThat(responseString).contains("Method Not Allowed");
    }

    @Test(description = """
            Testcase: Update article unsuccessfully with non-existing article
            Send PUT to endpoint: /articles/{slug}
            Expected code: 404, msg article does not exist""")
    public void TC11_UpdateArticles_Fail_InvalidSlug(){
        MArticle articleUpdate = new MArticle();
        articleUpdate = validArticle;
        articleUpdate.setDescription(validArticle.getBody() + System.currentTimeMillis());
        MArticleCreate articleUpdate01 = new MArticleCreate();
        articleUpdate01.setArticle(articleUpdate);
        JsonNode node = mapper.valueToTree(articleUpdate01);
        requestResponseLog.write("\\n========== Update article with invalid slug ===========\\n");
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().body(node).request("PUT","/articles/"+invalidSlug);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
        Assertions.assertThat(responseString).contains("article does not exist");
    }
}
