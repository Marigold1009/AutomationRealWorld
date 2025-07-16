package testsuite.api.profiles;

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
import testsuite.model.article.MArticlesResponse;
import testsuite.model.article.MAuthor;
import testsuite.model.profiles.MProfile;
import testsuite.model.profiles.MProfilesDetail;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;
import testsuite.utils.ApiLogFactory;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.List;

import static io.restassured.RestAssured.with;
import static testsuite.config.ApiDataFactory.USER_001;
import static testsuite.config.ApiDataFactory.expired_token;

public class TestFollowUser {
    private final ObjectMapper mapper = new ObjectMapper();
    private StringWriter requestResponseLog;
    private PrintStream requestResponseCaptureStream;
    private String token;
    private String userName;
    private String userNameTestFollow;
    @BeforeClass
    public void Beforelass() throws JsonProcessingException {
        RestAssured.baseURI = ApiDataFactory.API_URL;
        requestResponseLog = ApiLogFactory.getWriter();
        requestResponseCaptureStream = ApiLogFactory.getStream();
        RestAssured.requestSpecification =
        RestAssured.given().filters(new RequestLoggingFilter(requestResponseCaptureStream)
                , new ResponseLoggingFilter(requestResponseCaptureStream));

//        Create account and store token
        MUserDetail userDetail = ApiDataFactory.getUserByEmail(USER_001);
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = with().header("Content-Type","application/json")
                .when().body(node).request("POST","/users/login");
        user = mapper.readValue(response.getBody().prettyPrint(),MUser.class);
        userDetail = user.getUser();
        token = userDetail.getToken();
        userName = userDetail.getUsername();

//        Get 1 username
        Response response1 = with().when().request("GET","/articles");
        MArticlesResponse mArticlesResponse = mapper.readValue(response1.getBody().prettyPrint(),MArticlesResponse.class);
        List<MArticle> articleList = mArticlesResponse.getArticles();
        MAuthor author = new MAuthor();
        for(MArticle article: articleList){
            author = article.getAuthor();
            userNameTestFollow = author.getUsername();
            if(!userNameTestFollow.equals(userName)){
                break;
            }
        }
    }

    @BeforeMethod
    public void BeforeMethod(){
           Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                   .when().request("DELETE", "/profiles/"+userNameTestFollow+"/follow");
    }

    @Test(description = """
            Testcase Follow user successfully
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC1_FollowUserSuccess() throws JsonProcessingException {
        requestResponseLog.write("\\n========== Test follow user ===========\\n");
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("POST","/profiles/"+userNameTestFollow+"/follow");
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        MProfile profileResponse = new MProfile();
        profileResponse = mapper.readValue(response.getBody().prettyPrint(), MProfile.class);
        MProfilesDetail profileDetail = new MProfilesDetail();
        profileDetail = profileResponse.getProfilesDetail();
        Assertions.assertThat(profileDetail.getUsername()).contains(userNameTestFollow);
        Assertions.assertThat(profileDetail.isFollowing()).isEqualTo(true);
    }

    @Test(description = """
            Testcase Follow non existing user unsuccessfully
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC2_FollowUserFail_NonExistingUserName() {
        String non_ExistingUserName = userNameTestFollow + System.currentTimeMillis();
        requestResponseLog.write("\\n========== Test follow non existing user ===========\\n");
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("POST","/profiles/"+non_ExistingUserName+"/follow");
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("user does not exist");
    }

    @Test(description = """
            Testcase Follow Fail, no input userName
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC3_FollowUserFail_EmptyUserName() {
        requestResponseLog.write("\\n========== Test follow user not input username ===========\\n");
        Response response = with().headers("Content-Type","application/json", "Authorization", "Token "+token)
                .when().request("POST","/profile/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
        Assertions.assertThat(responseString).contains("Not Found");
    }

    @Test(description = """
            Testcase Follow user Fail with empty token
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC4_FollowUserFail_EmptyToken() {
        requestResponseLog.write("\\n========== Test follow user with empty token ===========\\n");
        Response response = with().when().request("POST","/profiles/"+userNameTestFollow+"/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("authentication required");
    }

    @Test(description = """
            Testcase Follow Fail with invalid token
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC5_FollowUserFail_InvalidToken() {
        String invalid_token = token + System.currentTimeMillis();
        requestResponseLog.write("\\n========== Test follow user with invalid token ===========\\n");
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+invalid_token)
                .when().request("POST","/profiles/"+userNameTestFollow+"/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Follow user Fail with expired token
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC6_FollowUserFail_ExpiredToken() {
        requestResponseLog.write("\\n========== Test follow user with expired token ===========\\n");
        Response response = with().headers("Content-Type", "application/json","Authorization", "Token " + expired_token)
                .when().request("POST","/profiles/"+userNameTestFollow+"/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Follow user fail with wrong format token 
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC7_FollowUserFail_WrongFormatToken() {
        requestResponseLog.write("\\n========== Test follow user with wrong format token ===========\\n");
        Response response = with().headers("Content-Type", "application/json","Authorization", "Token"+token)
                .when().request("POST","/profiles/"+userNameTestFollow+"/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("unsupported authorization type");
    }

    @Test(description = """
            Testcase Follow user Fail Non Existing Token
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC8_FollowUserFail_NonExistingToken() {
        String non_ExistingToken = "abddnbdb";
        requestResponseLog.write("\\n========== Test follow user with non existing token ===========\\n");
        Response response = with().headers("Content-Type","application/json", "Authorization","Token "+non_ExistingToken)
                .when().request("POST","/profiles/"+userNameTestFollow+"/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Follow user Fail Followed user
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC9_FollowUserFail_FollowedUser() {
        with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("POST","/profiles/"+userNameTestFollow+"/follow");
        requestResponseLog.write("\\n========== Test follow user which already followed ===========\\n");
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("POST","/profiles/"+userNameTestFollow+"/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(400);
        Assertions.assertThat(responseString).contains("you follow this user already");
    }
}
