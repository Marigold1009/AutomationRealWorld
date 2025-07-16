package testsuite.api.tags;

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
import testsuite.model.article.MArticle;
import testsuite.model.article.MArticlesResponse;
import testsuite.model.tag.MTags;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;
import testsuite.utils.ApiLogFactory;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.with;

public class TestAllTags {
    private final ObjectMapper mapper = new ObjectMapper();
    private StringWriter requestResponseLog;
    private PrintStream requestResponseCaaptureStream;
    private String token;
    private List<String> tagListCompare = new ArrayList<>();

    @BeforeClass
    public void BeforeClass() throws JsonProcessingException {
        RestAssured.baseURI = "https://realworld-api.ap.ngrok.io/api";
        requestResponseLog = ApiLogFactory.getWriter();
        requestResponseCaaptureStream = ApiLogFactory.getStream();
        RestAssured.requestSpecification =
                RestAssured.given().filters(new RequestLoggingFilter(requestResponseCaaptureStream)
                        , new ResponseLoggingFilter(requestResponseCaaptureStream));

//        Login and get token
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail("nguyenthucuc996@gmail.com");
        userDetail.setPassword("Cucvantho09");
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = with().header("Content-Type", "application/json")
                .when().body(node).request("POST", "/users/login");
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        userDetail = user.getUser();
        token = userDetail.getToken();

//        Get some tags and store in the list
        Response response1 = with().header("Content-Type", "application/json")
                .when().request("GET", "articles");
        MArticlesResponse articlesResponse = mapper.readValue(response1.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList = articlesResponse.getArticles();
        List<String> articleTag = new ArrayList<>();
        for (MArticle article : articleList) {
            articleTag = article.getTagList();
            for (String tag : articleTag) {
                if (!tagListCompare.contains(tag)) {
                    tagListCompare.add(tag);
                }
            }
        }
    }

    @Test(description = """
            Test case: Get all tags""")
    public void TC1_GetAllTagsSuccess_GuestMode() throws JsonProcessingException {
        Response response = with().header("Content-Type", "application/json")
                .when().request("GET", "/tags");
        MTags tagListResponse = mapper.readValue(response.getBody().prettyPrint(), MTags.class);
        List<String> tagList = new ArrayList<>();
        tagList = tagListResponse.getTags();
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        Assert.assertTrue(tagList.containsAll(tagListCompare));
    }

    @Test(description = """
            Testcase: Get all tags Logged in mode""")
    public void TC2_GetAllTags_Success_LoginMode() throws JsonProcessingException {
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().request("GET", "/tags");
        MTags tagListResponse = mapper.readValue(response.getBody().prettyPrint(), MTags.class);
        List<String> tagList = new ArrayList<>();
        tagList = tagListResponse.getTags();
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        Assert.assertTrue(tagList.containsAll(tagListCompare));
    }
}
