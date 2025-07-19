package testsuite.api.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import model.comments.MComment;
import model.comments.MCommentDetail;
import model.comments.MComments;
import model.user.MUser;
import model.user.MUserDetail;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testsuite.config.ApiDataFactory;
import utils.logging.LogFactory;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.List;

import static io.restassured.RestAssured.with;
import static testsuite.config.ApiDataFactory.*;

public class TestDeleteComment {
    private final ObjectMapper mapper = new ObjectMapper();
    private StringWriter requestResponseLog;
    private PrintStream requestResponseCaptureStream;
    private String token;
    private String slugTest = "artist-just-majority-field";
    int validId;
    int inValidId = 32;

    @BeforeClass
    public void BeforeClass() throws JsonProcessingException {
        RestAssured.baseURI = ApiDataFactory.API_URL;
        LogFactory.init();
        requestResponseLog = LogFactory.getWriter();
        requestResponseCaptureStream = LogFactory.getStream();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

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
    }

    @BeforeMethod
    public void BeforeMethod() throws JsonProcessingException {
//        Create a comment and get id
        MCommentDetail commentDetail = new MCommentDetail();
        commentDetail.setBody("This is a comment for test delete comment");
        MComment comment= new MComment();
        comment.setComment(commentDetail);
        JsonNode node1 = mapper.valueToTree(comment);
        Response response1 = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().body(node1).request("POST","/articles/"+slugTest+"/comments");
//      Get id
        Response response = with().when().request("GET","/articles/"+slugTest+"/comments");
        MComments comments = mapper.readValue(response.getBody().prettyPrint(),MComments.class);
        List<MCommentDetail> commentList = comments.getComments();
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        for (MCommentDetail comment01: commentList){
            if(comment01.getBody().equals("This is a comment for test delete comment")) {
                validId = comment01.getId();
            }else {
//                inValidId = comment01.getId();
            }
        }
    }

    @Test(description = """
            Delete comment successfully""")
    public void TC1_DeleteComment(){
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("DELETE","/articles/"+slugTest+"/comments/"+validId);
        Assertions.assertThat(response.statusCode()).isEqualTo(204);
    }

    @Test(description = """
            Delete comment without token""")
    public void TC2_DeleteCommentWithoutToken(){
        Response response = with().header("Content-Type","application/json")
                .when().request("DELETE","/articles/"+slugTest+"/comments/"+validId);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("required");
    }
    @Test(description = """
            Delete comment with expired token""")
    public void TC3_DeleteCommentWithExpiredToken(){
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+ expired_token)
                .when().request("DELETE","/articles/"+slugTest+"/comments/"+validId);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Delete comment with invalid token""")
    public void TC4_DeleteCommentWithInvalidToken(){
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+ invalid_token)
                .when().request("DELETE","/articles/"+slugTest+"/comments/"+validId);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Delete comment with wrong format token""")
    public void T5_DeleteCommentWithWrongFormatToken(){
        Response response = with().headers("Content-Type","application/json","Authorization", "Token"+ token)
                .when().request("DELETE","/articles/"+slugTest+"/comments/"+validId);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("unsupported authorization type");
    }

    @Test(description = """
            Delete comment without slug""")
    public void TC6_DeleteCommentWithoutSlug(){
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+ token)
                .when().request("DELETE","/articles/comments/"+validId);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
        Assertions.assertThat(responseString).contains("Not Found");
    }

    @Test(description = """
            Delete comment with non existing slug""")
    public void TC7_DeleteCommentNonExistingSlug(){
        String nonExistingSlug = slugTest + System.currentTimeMillis();
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+ token)
                .when().request("DELETE","/articles/"+nonExistingSlug+"/comments/"+validId);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
        Assertions.assertThat(responseString).contains("article does not exist");
    }
    @Test(description = """
            Delete comment without Id""")
    public void TC8_DeleteCommentwithoutId(){
        String nonExistingSlug = slugTest + System.currentTimeMillis();
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+ token)
                .when().request("DELETE","/articles/"+nonExistingSlug+"/comments/");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(307);
//        Assertions.assertThat(responseString).contains("required");
    }

    @Test(description = """
            Delete comment without Id and Slug""")
    public void TC9_DeleteCommentwithoutIdAndSlug(){
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+ token)
                .when().request("DELETE","/articles/comments/");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(307);
//        Assertions.assertThat(responseString).contains("required");
    }

    @Test(description = """
            Delete comment without Id and Slug""")
    public void TC10_DeleteCommentwithoutInvalidId(){
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+ token)
                .when().request("DELETE","/articles/"+slugTest+"/comments/"+inValidId);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("you are not an author of this article");
    }

    @Test(description = """
            Delete comment without negative Id""")
    public void TC11_DeleteCommentWithNegativeId(){
        int negativeId = -1;
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+ token)
                .when().request("DELETE","/articles/"+slugTest+"/comments/"+negativeId);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(422);
        Assertions.assertThat(responseString).contains("ensure this value is greater than or equal to 1");
    }
}
