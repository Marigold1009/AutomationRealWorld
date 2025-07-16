package testsuite.api.users;

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
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;
import testsuite.utils.ApiLogFactory;

import java.io.PrintStream;
import java.io.StringWriter;

import static io.restassured.RestAssured.with;
import static testsuite.config.ApiDataFactory.USER_001;
import static testsuite.config.ApiDataFactory.expired_token;

public class TestGetCurrentUser {
    private final ObjectMapper mapper = new ObjectMapper();
    private StringWriter requestResponseLog;
    private PrintStream requestResponseCaaptureStream;
    private String token;
    private MUser userExpected = new MUser();
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
                .when().body(node).request("POST","/users/login");
        user = mapper.readValue(response.getBody().prettyPrint(),MUser.class);
        userExpected = user;
        userDetail = user.getUser();
        token = userDetail.getToken();
    }

    @Test(description = """
            Testcase Test get current user with valid token
            Send get to endpoint: api/user
            Expect """)
    public void TC1_GetCurrentUserSuccess() throws JsonProcessingException {
        Response response = with().headers("Content-Type","application/json","Authorization"," Token "+token)
                .when().request("GET","/user");
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        MUser user = mapper.readValue(response.getBody().prettyPrint(),MUser.class);
        Assertions.assertThat(user).usingRecursiveComparison().isEqualTo(userExpected);
    }

    @Test(description = """
            Testcase get current user unsuccessfully without token""")
    public void TC2_GetUserFailWithoutToken(){
        Response response = with().request("GET","/user");
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("authentication required");
    }

    @Test(description = """
            Testcase get user fail with invalid token
            Send get to endpoint: api/user
            Expect code and msg""")
    public void TC3_GetUserFailWithInvalidToken( ){
        Response response = with().headers("Content-Type","application/json", "Authorization", "Token "+"absnsbsbs")
                .when().request("GET","/user");
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Get user fail with expired token
            Send get to endpoint: api/user
            Expect code and msg
            """)
    public void TC4_GetUserFailWithExpiredToken(){
        Response response = with().headers("Content-Type", "application/json", "Authorization","Token "+expired_token)
        .when().request("GET","/user");
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Testcase Get user fail with wrong format""")
    public void TC5_GetUserFailWithWrongFormatToken(){
        Response response = with().headers("Content-Type", "application/json", "Authorization","Token"+token)
                .when().request("GET","/user");
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("unsupported authorization type");
    }

    @Test(description = """
            Testcase Get User Fail with right format but token is not existing""")
    public void TC6_GetUserFailNoExistingToken(){
        Response response = with().headers("Content-Type", "application/json", "Authorization","Token "+"angdsnhjjjjjsjsjsjsjsj")
                .when().request("GET","/user");
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }
}
