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

public class TestUnfollowUser {
    private final ObjectMapper mapper = new ObjectMapper();
    private StringWriter requestResponseLog;
    private PrintStream requestResponseCaaptureStream;
    private String token;
    private String userNameTestUnfollow;

    @BeforeClass
    public void BeforeClass() throws JsonProcessingException {
        RestAssured.baseURI = ApiDataFactory.API_URL;
        requestResponseLog = ApiLogFactory.getWriter();
        requestResponseCaaptureStream = ApiLogFactory.getStream();
        RestAssured.requestSpecification =
                RestAssured.given().filters(new RequestLoggingFilter(requestResponseCaaptureStream)
                        , new ResponseLoggingFilter(requestResponseCaaptureStream));

//        Login and store token
        MUserDetail userDetail = ApiDataFactory.getUserByEmail(USER_001);
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = with().header("Content-Type", "application/json")
                .when().body(node).request("POST", "users/login");
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        userDetail = user.getUser();
        token = userDetail.getToken();
        String userName;
        userName = userDetail.getUsername();

//        Store 1 userName to unfollow
        Response response1 = with().when().request("GET", "/articles");
        MArticlesResponse mArticlesResponse = mapper.readValue(response1.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList = mArticlesResponse.getArticles();
        MAuthor author = new MAuthor();
        for (MArticle article : articleList) {
            author = article.getAuthor();
            userNameTestUnfollow = author.getUsername();
            if (!userNameTestUnfollow.equals(userName)) {
                break;
            }
        }
    }

    @BeforeMethod
    public void BeforeMethod(){
        with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("POST","profiles/"+userNameTestUnfollow+"/follow");
    }

    @Test(description = """
            Testcase Unfollow user successfully
            Send delete to endpoint /api/profiles/{userName}/follow
            Expect verify code and response""")
    public void TC1_UnfollowSuccess() throws JsonProcessingException {
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("DELETE","/profiles/"+userNameTestUnfollow+"/follow");
        MProfile profile = mapper.readValue(response.getBody().prettyPrint(),MProfile.class);
        MProfilesDetail profilesDetail = profile.getProfilesDetail();
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        Assertions.assertThat(profilesDetail.getUsername()).contains(userNameTestUnfollow);
        Assertions.assertThat(profilesDetail.isFollowing()).isEqualTo(false);
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with empty token
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC2_UnfollowFailWithEmptyToken() {
        Response response = with().when().request("DELETE","/profiles/"+userNameTestUnfollow+"/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("authentication required");
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with invalid token
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC3_UnfollowFailWithInvalidToken() {
        String invalid_token = "aghdgdhdh";
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+invalid_token)
                .when().request("DELETE","/profiles/"+userNameTestUnfollow+"/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with wrong format token
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC4_UnfollowFailWithWrongFormatToken() {
        Response response = with().headers("Content-Type","application/json","Authorization", "Token"+token)
                .when().request("DELETE","/profiles/"+userNameTestUnfollow+"/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("unsupported authorization type");
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with non existing token
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC5_UnfollowFailWithNonExistingToken() {
        String non_existingToken = "dfdhdgd";
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+non_existingToken)
                .when().request("DELETE","/profiles/"+userNameTestUnfollow+"/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with expired Token
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC6_UnfollowFailWithExpiredToken() {
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+expired_token)
                .when().request("DELETE","/profiles/"+userNameTestUnfollow+"/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with non existing user
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC7_UnfollowFailWithNonExistingUser() {
        String nonUserName = userNameTestUnfollow + System.currentTimeMillis();
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("DELETE","/profiles/"+nonUserName+"/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
        Assertions.assertThat(responseString).contains("user does not exist");
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with empty userName
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC8_UnfollowFailWithEmptyUser() {
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("DELETE","/profiles/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(405);
        Assertions.assertThat(responseString).contains("Method Not Allowed");
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with unfollow User
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC9_UnfollowFailWithUnfollowUser() {
        with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("DELETE","/profiles/"+userNameTestUnfollow+"/follow");
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("DELETE","/profiles/"+userNameTestUnfollow+"/follow");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(400);
        Assertions.assertThat(responseString).contains("you don't follow this user");
    }
}
