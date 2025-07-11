package testsuite.api.articles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.testng.annotations.*;
import testsuite.config.ApiDataFactory;
import testsuite.model.article.MArticle;
import testsuite.model.article.MArticleCreate;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;
import testsuite.utils.ApiLogFactory;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.List;

public class TestCreateArticle {
    private static final Logger logger = LogManager.getLogger(TestCreateArticle.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private String token;
    private String title = "test Create Auto01";
    private StringWriter requestResponseLog;
    private PrintStream requestResponseCaptureStream;

    @BeforeClass
    public void beforeClass() throws JsonProcessingException {
        ApiLogFactory.init();
        requestResponseLog = ApiLogFactory.getWriter();
        requestResponseCaptureStream = ApiLogFactory.getStream();
        RestAssured.baseURI = ApiDataFactory.API_URL;

        // Set a global request specification with logging filters
        RestAssured.requestSpecification = RestAssured.given()
                .filters(
                        new RequestLoggingFilter(requestResponseCaptureStream),
                        new ResponseLoggingFilter(requestResponseCaptureStream)
                );

        // Login and save token
        MUserDetail userDetail = ApiDataFactory.getUserByEmail(ApiDataFactory.USER_001);

        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = RestAssured.given().headers("Content-Type", "application/json")
                .when()
                .body(node)
                .post("/users/login");

        Assertions.assertThat(response.statusCode()).as("Expected stt code: 200").isEqualTo(200);
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        token = user.getUser().getToken();
    }

    @BeforeGroups("create_article")
    public void BeforeGroupCreateArticle() {
        String slug = title.toLowerCase().replaceAll("\\s+", "-");
        Response response = RestAssured.given().header("Content-Type", "application/json")
                .request("GET", "/articles/" + slug);
        String responseString = response.getBody().prettyPrint();
        if (responseString.contains("article does not exist")) {
            requestResponseLog.write("The article does not exist, we don't need to delete");
        } else {
            requestResponseLog.write("The article exists, we need to delete it before test");
            Response responseDelete = RestAssured.given().headers("Content-Type", "application/json",
                            "Authorization", "Token " + token)
                    .request("DELETE", "/articles/" + slug);
            int responseDeleteCode = responseDelete.statusCode();
            if (responseDeleteCode == 204) {
                System.out.println("Delete the article successfully");
            } else {
                System.out.println("Something went wrong");
            }
        }
    }

    @DataProvider(name = "valid_params")
    public Object[][] dp_validParams() {
        return new Object[][]{
                {title,
                        "Description Create Auto01",
                        "Body Create Auto01",
                        List.of("autotag01", "autotag02")}
        };
    }

    @Test(description = """
            Testcase Create article successfully
            Send post to end point: /articles
            Expected code 200 and content""",
            dataProvider = "valid_params", groups = {"create_article"})
    public void TC1_CreateArticleSuccess(String title, String description, String body, List<String> tagList) throws JsonProcessingException {
//        Init MArticle
        MArticle article = new MArticle();
        article.setTitle(title);
        article.setDescription(description);
        article.setBody(body);
        article.setTagList(tagList);

//        Init MArticleCreate
        MArticleCreate articleCreate = new MArticleCreate();
        articleCreate.setArticle(article);

//        Prepare data for body
        JsonNode node = mapper.valueToTree(articleCreate);

//        Send request and get response
        requestResponseLog.write("\n========== Create article ===========\n");
        Response response = RestAssured.given().headers("Content-Type", "application/json",
                        "Authorization", "Token " + token)
                .when()
                .body(node)
                .request("POST", "/articles");

//        Assert
        JsonNode rootNode = mapper.readTree(response.getBody().asString());
        JsonNode articleNode = rootNode.get("article");
        MArticle articleResponse = mapper.treeToValue(articleNode, MArticle.class);
        Assertions.assertThat(response.statusCode()).as("Expected stt code: 201")
                .isEqualTo(201);
        Assertions.assertThat(articleResponse.getTitle()).as("Expected title: {title}")
                .contains(title);
        Assertions.assertThat(articleResponse.getBody()).as("Expected title: {body}")
                .contains(body);
        Assertions.assertThat(articleResponse.getDescription()).as("Expected title: {description}")
                .contains(description);
    }

    @Test(description = """
            Testcase Can not create article with empty token
            Send post to end point: /articles
            Expected code and msg""", dataProvider = "valid_params")
    public void TC2_CreateArticleFail_EmptyToken(String title, String description, String body, List<String> tagList) {
//        Init MArticle
        MArticle article = new MArticle();
        article.setTitle(title);
        article.setDescription(description);
        article.setBody(body);
        article.setTagList(tagList);

//        Init MrticleCreate
        MArticleCreate articleCreate = new MArticleCreate();
        articleCreate.setArticle(article);

//        Prepare data for body
        JsonNode node = mapper.valueToTree(articleCreate);

//        Send request and get response
        Response response = RestAssured.given().headers("Content-Type", "application/json")
                .when()
                .body(node)
                .request("POST", "/articles");

//        Assert
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: authentication required")
                .contains("authentication required");
        Assertions.assertThat(response.statusCode()).as("Expected code 403").isEqualTo(403);
    }

    @Test(description = """
            Testcase Create article fail with invalid token
            Send post to end point: /articles
            Expected code: 403 and msg unsupported authorization type""", dataProvider = "valid_params")
    public void TC3_CreateArticleFail_InvalidToken(String title, String description, String body, List<String> tagList) {
//        Init MArticle
        MArticle article = new MArticle();
        article.setTitle(title);
        article.setDescription(description);
        article.setBody(body);
        article.setTagList(tagList);

//        Init MrticleCreate
        MArticleCreate articleCreate = new MArticleCreate();
        articleCreate.setArticle(article);

//        Prepare data for body
        JsonNode node = mapper.valueToTree(articleCreate);

//        Send request and get response
        Response response = RestAssured.given().headers("Content-Type", "application/json",
                        "Authorization", "abc")
                .when()
                .body(node)
                .request("POST", "/articles");

//        Assert
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: unsupported authorization type")
                .contains("unsupported authorization type");
        Assertions.assertThat(response.statusCode()).as("Expected code 403").isEqualTo(403);
    }

    @Test(description = """
            Testcase: Can not create article without title
            Send post to endpoint: /articles
            Expected code and msg""", dataProvider = "valid_params")
    public void TC4_CreateArticleFail_EmptyTitle(String title, String description, String body, List<String> tagList) {
//        Init MArticle
        MArticle article = new MArticle();
        article.setDescription(description);
        article.setBody(body);
        article.setTagList(tagList);

//        Init MrticleCreate
        MArticleCreate articleCreate = new MArticleCreate();
        articleCreate.setArticle(article);

//        Prepare data for body
        JsonNode node = mapper.valueToTree(articleCreate);

//        Send request and get response
        Response response = RestAssured.given().headers("Content-Type", "application/json",
                        "Authorization", "Token " + token)
                .when()
                .body(node)
                .request("POST", "/articles");

//        Assert
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: field required")
                .contains("field required");
        Assertions.assertThat(responseString).as("Expected type: value_error.missing")
                .contains("value_error.missing");
        Assertions.assertThat(response.statusCode()).as("Expected code 422")
                .isEqualTo(422);
    }

    @Test(description = """
            Testcase can not create article with existing name
            Send post to endpoint: /articles
            Expected code and msg""", dataProvider = "valid_params", groups = {"create_article"})
    public void TC5_CreateArticleFail_ExistingTitle(String title, String description, String body,
                                                    List<String> tagList) {
//        Init MArticle
        MArticle article = new MArticle();
        article.setTitle(title);
        article.setDescription(description);
        article.setBody(body);
        article.setTagList(tagList);

//        Init MrticleCreate
        MArticleCreate articleCreate = new MArticleCreate();
        articleCreate.setArticle(article);

//        Prepare data for body
        JsonNode node = mapper.valueToTree(articleCreate);

//        Send request and get response
        Response response = RestAssured.given().headers("Content-Type", "application/json",
                        "Authorization", "Token " + token)
                .when()
                .body(node)
                .request("POST", "/articles");

//        Assert
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: article already exists")
                .contains("article already exists");
        Assertions.assertThat(response.statusCode()).as("Expected code 400")
                .isEqualTo(400);
    }

}
