package testsuite.api.comment;

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
import testsuite.model.comments.MComment;
import testsuite.model.comments.MCommentDetail;
import testsuite.model.comments.MComments;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;
import testsuite.utils.ApiLogFactory;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.List;

import static io.restassured.RestAssured.with;
import static testsuite.config.ApiDataFactory.*;

public class TestGetComment {
    private final ObjectMapper mapper = new ObjectMapper();
    private StringWriter requestResponseLog;
    private PrintStream requestResponseCaptureStream;
    private String token;
    private String slugTest = "artist-just-majority-field";
    private String username;

    @BeforeClass
    public void BeforeClass() throws JsonProcessingException {
        RestAssured.baseURI = ApiDataFactory.API_URL;
        ApiLogFactory.init();
        requestResponseLog = ApiLogFactory.getWriter();
        requestResponseCaptureStream = ApiLogFactory.getStream();
        RestAssured.requestSpecification =
                RestAssured.given().filters(new RequestLoggingFilter(requestResponseCaptureStream)
                        , new ResponseLoggingFilter(requestResponseCaptureStream));

//        Login and store token
        MUserDetail userDetail = ApiDataFactory.getUserByEmail(USER_001);
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = with().header("Content-Type","application/json")
                .when().body(node).request("POST","/users/login");
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        userDetail = user.getUser();
        token = userDetail.getToken();
        username = userDetail.getUsername();

//        Create a comment
        MCommentDetail commentDetail = new MCommentDetail();
        commentDetail.setBody("This is a comment for test get comment");
        MComment comment= new MComment();
        comment.setComment(commentDetail);
        JsonNode node1 = mapper.valueToTree(comment);
        Response response1 = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().body(node1).request("POST","/articles/"+slugTest+"/comments");
    }

    @Test(description = """
            Guest get all comments""")
    public void TC1_GuestGetsComment() throws JsonProcessingException {
        Response response = with().when().request("GET","/articles/"+slugTest+"/comments");
        MComments comments = mapper.readValue(response.getBody().prettyPrint(),MComments.class);
        List<MCommentDetail> commentList = comments.getComments();
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        boolean checkcomment = true;
        for (MCommentDetail comment: commentList){
            checkcomment = true;
            if(comment.getBody().equals("This is a comment for test get comment")) {
                break;
            }else {
                checkcomment = false;
            }
        }
        Assert.assertTrue(checkcomment);
    }

    @Test(description = """
            User get all comments""")
    public void TC2_UserGetsComment() throws JsonProcessingException {
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("GET","/articles/"+slugTest+"/comments");
        MComments comments = mapper.readValue(response.getBody().prettyPrint(),MComments.class);
        List<MCommentDetail> commentList = comments.getComments();
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        boolean checkcomment = true;
        for (MCommentDetail comment: commentList){
            checkcomment = true;
            if(comment.getBody().equals("This is a comment for test get comment")) {
                break;
            }else {
                checkcomment = false;
            }
        }
        Assert.assertTrue(checkcomment);
    }

    @Test(description = """
            Testcase Get comment with empty slug""")
    public void TC3_GetCommentWithoutSlug(){
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("GET","/articles/comments");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
        Assertions.assertThat(responseString).contains("article does not exist");
    }

    @Test(description = """
            Testcase Get comment with non existing slug""")
    public void TC4_GetCommentWithNonExistingSlug(){
        String nonExistingSlug = slugTest + System.currentTimeMillis();
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("GET","/articles/"+nonExistingSlug+"/comments");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
        Assertions.assertThat(responseString).contains("article does not exist");
    }

    @Test(description = """
            Testcase Get comment with expired token""")
    public void TC5_GetCommentWithExpiredToken(){
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+expired_token)
                .when().request("GET","/articles/"+slugTest+"/comments");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Get comment with non existing token""")
    public void TC6_GetCommentWithNonExistingToken(){
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+invalid_token)
                .when().request("GET","/articles/"+slugTest+"/comments");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Get comment with wrong format token""")
    public void TC7_GetCommentWithWrongFormatToken(){
        Response response = with().headers("Content-Type","application/json","Authorization", "Token"+ token)
                .when().request("GET","/articles/"+slugTest+"/comments");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("unsupported authorization type");
    }
}
