package testsuite.api.comment;

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
import testsuite.config.ApiDataFactory;
import testsuite.model.article.MAuthor;
import testsuite.model.comments.MComment;
import testsuite.model.comments.MCommentDetail;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;
import testsuite.utils.ApiLogFactory;

import java.io.PrintStream;
import java.io.StringWriter;

import static io.restassured.RestAssured.with;
import static testsuite.config.ApiDataFactory.*;

public class TestPostComment {
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
    }
    @Test(description = """
            Test post comment
            Send Post to end point: /articles/{slug}/comments
            Expected receive response""")
    public void TC1_CreateCommentSuccess() throws JsonProcessingException {
        MCommentDetail commentDetail = new MCommentDetail();
        commentDetail.setBody("This is a comment");
        MComment comment= new MComment();
        comment.setComment(commentDetail);
        JsonNode node = mapper.valueToTree(comment);
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().body(node).request("POST","/articles/"+slugTest+"/comments");
        Assertions.assertThat(response.statusCode()).isEqualTo(201);
        comment = mapper.readValue(response.getBody().prettyPrint(), MComment.class);
        commentDetail = comment.getComment();
        Assertions.assertThat(commentDetail.getBody()).contains("This is a comment");
        MAuthor author = commentDetail.getAuthor();
        Assertions.assertThat(author.getUsername()).contains(username);
    }

    @Test(description = """
            Create comment without token""")
    public void TC2_CreateCommentWithoutToken(){
        MCommentDetail commentDetail = new MCommentDetail();
        commentDetail.setBody("This is a comment");
        MComment comment= new MComment();
        comment.setComment(commentDetail);
        JsonNode node = mapper.valueToTree(comment);
        Response response = with().header("Content-Type","application/json")
                .when().body(node).request("POST","/articles/"+slugTest+"/comments");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("authentication required");
    }

    @Test(description = """
            Create Comment with expired token""")
    public void TC3_CreateCommentExpiredToken(){
        MCommentDetail commentDetail = new MCommentDetail();
        commentDetail.setBody("This is a comment");
        MComment comment= new MComment();
        comment.setComment(commentDetail);
        JsonNode node = mapper.valueToTree(comment);
        Response response = with().headers("Content-Type","application/json", "Authorization", "Token "+ expired_token)
                .when().body(node).request("POST","/articles/"+slugTest+"/comments");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Create Comment with invalid token""")
    public void TC4_CreateCommentInvalidToken(){
        MCommentDetail commentDetail = new MCommentDetail();
        commentDetail.setBody("This is a comment");
        MComment comment= new MComment();
        comment.setComment(commentDetail);
        JsonNode node = mapper.valueToTree(comment);
        Response response = with().headers("Content-Type","application/json", "Authorization", "Token "+invalid_token)
                .when().body(node).request("POST","/articles/"+slugTest+"/comments");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Create Comment with wrong format token""")
    public void TC5_CreateCommentWrongFormatToken(){
        MCommentDetail commentDetail = new MCommentDetail();
        commentDetail.setBody("This is a comment");
        MComment comment= new MComment();
        comment.setComment(commentDetail);
        JsonNode node = mapper.valueToTree(comment);
        Response response = with().headers("Content-Type","application/json", "Authorization", "Token"+ token)
                .when().body(node).request("POST","/articles/"+slugTest+"/comments");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("unsupported authorization type");
    }

    @Test(description = """
            Create Comment with non existing token""")
    public void TC6_CreateCommentNonExistingSlug(){
        String non_existingSlug = slugTest + System.currentTimeMillis();
        MCommentDetail commentDetail = new MCommentDetail();
        commentDetail.setBody("This is a comment");
        MComment comment= new MComment();
        comment.setComment(commentDetail);
        JsonNode node = mapper.valueToTree(comment);
        Response response = with().headers("Content-Type","application/json", "Authorization", "Token "+ token)
                .when().body(node).request("POST","/articles/"+non_existingSlug+"/comments");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
        Assertions.assertThat(responseString).contains("article does not exist");
    }

    @Test(description = """
            Create Comment without slug""")
    public void TC7_CreateCommentWithoutSlug(){
        MCommentDetail commentDetail = new MCommentDetail();
        commentDetail.setBody("This is a comment");
        MComment comment= new MComment();
        comment.setComment(commentDetail);
        JsonNode node = mapper.valueToTree(comment);
        Response response = with().headers("Content-Type","application/json", "Authorization", "Token "+ token)
                .when().body(node).request("POST","/articles/comments");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(405);
        Assertions.assertThat(responseString).contains("Method Not Allowed");
    }

}
