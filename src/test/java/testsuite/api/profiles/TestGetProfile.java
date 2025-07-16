package testsuite.api.profiles;

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

public class TestGetProfile {
    private final ObjectMapper mapper = new ObjectMapper();
    private StringWriter requestResponseLog;
    private PrintStream requestResponseCaaptureStream;
    private String token;
    private String userNameTestProfile;


    @BeforeClass
    public void BeforeClass() throws JsonProcessingException {
        RestAssured.baseURI = ApiDataFactory.API_URL;
        requestResponseLog = ApiLogFactory.getWriter();
        requestResponseCaaptureStream = ApiLogFactory.getStream();
        RestAssured.requestSpecification =
        RestAssured.given().filters(new RequestLoggingFilter(requestResponseCaaptureStream)
                , new ResponseLoggingFilter(requestResponseCaaptureStream));
//        Login and Store token
        MUserDetail userDetail = ApiDataFactory.getUserByEmail(USER_001);
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = with().header("Content-Type","application/json")
                .when().body(node).request("POST","/users/login");
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        userDetail = user.getUser();
        token = userDetail.getToken();
        String userName = userDetail.getUsername();

//      Get 1 userName to view profile
        Response response1 = with().when().request("GET","/articles");
        MArticlesResponse mArticlesResponse = mapper.readValue(response1.getBody().prettyPrint(),MArticlesResponse.class);
        List<MArticle> articleList = mArticlesResponse.getArticles();
        MAuthor author;
        for(MArticle article: articleList){
            author = article.getAuthor();
            userNameTestProfile = author.getUsername();
            if(!userNameTestProfile.equals(userName)){
                break;
            }
        }
    }

    @Test(description = """
            Testcase Get user profile by userName successfully
            Send get to endpoint api/profiles/{userName}
            Expected code vaf response""")
    public void TC1_GetUserProfileSuccess() throws JsonProcessingException {
        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/profiles/"+userNameTestProfile+"/follow");
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("GET","/profiles/"+ userNameTestProfile);
        MProfile profiles = mapper.readValue(response.getBody().prettyPrint(),MProfile.class);
        MProfilesDetail profilesDetail = profiles.getProfilesDetail();
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        Assertions.assertThat(profilesDetail.getUsername()).contains(userNameTestProfile);
        Assertions.assertThat(profilesDetail.isFollowing()).isEqualTo(false);
    }

    @Test(description = """
            Testcase Get no existing user
            Send get to endpoint api/profiles/{userName}
            Expect code and msg""")
    public void TC2_GetNonExistingUser(){
        String non_ExistingUser = userNameTestProfile + System.currentTimeMillis();
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("GET","/profiles/"+non_ExistingUser);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
        Assertions.assertThat(responseString).contains("user does not exist");
    }

    @Test(description = """
            Testcase Get with Empty user
            Send get to endpoint api/profiles
            Expect code and msg""")
    public void TC3_GetNoUser(){
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("GET","/profiles");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
        Assertions.assertThat(responseString).contains("Not Found");
    }

    @Test(description = """
            Testcase Get profile success with valid token and invalid username
            Send get to endpoint api/profiles
            Expect response""")
    public void TC4_UserGetProfileSuccess(){
        String invalidToken = "andgdndgd";
        String invalid_userName = userNameTestProfile + System.currentTimeMillis();
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+invalidToken)
                .when().request("GET","/profiles/"+invalid_userName);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Get Profile fail with invalid token""")
    public void TC5_UserGetProfileFailInvalidToken(){
        String invalid_token = "angdbgdd";
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+invalid_token)
                .when().request("GET","/profiles/"+userNameTestProfile);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Get Profile fail with expired token""")
    public void TC6_UserGetProfileFailExpiredToken(){
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+expired_token)
                .when().request("GET","/profiles/"+userNameTestProfile);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Get Profile fail with wrong format token""")
    public void TC7_UserGetProfileFailWrongFormatToken(){
        Response response = with().headers("Content-Type","application/json","Authorization","Token"+token)
                .when().request("GET","/profiles/"+userNameTestProfile);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        Assertions.assertThat(responseString).contains("unsupported authorization type");
    }

    @Test(description = """
            Testcase Get Profile success without token""")
    public void TC8_UserGetProfileSuccessWithoutToken() throws JsonProcessingException {
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("GET","/profiles/"+userNameTestProfile);
        MProfile profile = mapper.readValue(response.getBody().prettyPrint(),MProfile.class);
        MProfilesDetail profilesDetail = profile.getProfilesDetail();
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        Assertions.assertThat(profilesDetail.getUsername()).contains(userNameTestProfile);
        Assertions.assertThat(profilesDetail.isFollowing()).isEqualTo(false);
    }

    @Test(description = """
            Testcase Get Profile success login mode with subcribe the author""")
    public void TC9_UserGetProfileSuccess_FollowedAuthor() throws JsonProcessingException {

        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("POST","/profiles/"+userNameTestProfile+"/follow");
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("GET","/profiles/"+userNameTestProfile);
        MProfile profile = mapper.readValue(response.getBody().prettyPrint(),MProfile.class);
        MProfilesDetail profilesDetail = profile.getProfilesDetail();
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        Assertions.assertThat(profilesDetail.getUsername()).contains(userNameTestProfile);
        Assertions.assertThat(profilesDetail.isFollowing()).isEqualTo(true);
    }
}

