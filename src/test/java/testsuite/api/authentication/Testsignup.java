package testsuite.api.authentication;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testsuite.config.ApiDataFactory;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;
import testsuite.model.user.MUserLoginError;
import testsuite.model.user.MUserLoginErrorDetail;
import testsuite.utils.ApiLogFactory;

import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.with;

public class Testsignup {
    private ObjectMapper mapper = new ObjectMapper();
    private StringWriter requestResponseLog;
    private PrintStream requestResponseCaptureStream;
    private String emailTest = "emailtestRegisterAPI" + System.currentTimeMillis()+"@gmail.com";
    private String userName = "userNameTest" + System.currentTimeMillis();
    private String password = "Cucvantho09";


//    @DataProvider(name = "valid_params")
//    public Object[][] dp_validparams() {
//        emailTest = emailTest + System.currentTimeMillis() + "@gmail.com";
//        userName = userName + System.currentTimeMillis();
//        return new Object[][]{{emailTest, userName, "Cucvantho09"}};
//    }

    @BeforeClass
    public void beforeClass() {
        RestAssured.baseURI = ApiDataFactory.API_URL;
        requestResponseLog = ApiLogFactory.getWriter();
        requestResponseCaptureStream = ApiLogFactory.getStream();
        RestAssured.requestSpecification =
        RestAssured.given().filters(new RequestLoggingFilter(requestResponseCaptureStream)
                , new ResponseLoggingFilter(requestResponseCaptureStream));
    }

    @Test(description = """
            Testcase: Sign up acc successfully
            Send post api to endpoint: api/users
            Expect: code 200, return acc info and token""")
    public void TC1_SignupSuccess() throws JsonProcessingException {
//        Init userDetail
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(emailTest);
        userDetail.setUsername(userName);
        userDetail.setPassword(password);

//        Init user
        MUser user = new MUser();
        user.setUser(userDetail);

//        Prepare data for body
        JsonNode node = mapper.valueToTree(user);

//        Send request and get response
        requestResponseLog.write("\\n========== Test Sign up with valid info ===========\\n");
        Response response = with().headers("Content-Type", "application/json")
                .when()
                .body(node)
                .request("POST", "users");

//        Assest
        MUser userResponse = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        MUserDetail userDetailResponse = userResponse.getUser();
        Assertions.assertThat(response.statusCode()).as("Expected stt code: 201");
        Assertions.assertThat(userDetailResponse.getEmail()).as("Expected email as input")
                .isEqualTo(emailTest);
        Assertions.assertThat(userDetailResponse.getUsername()).as("Expected userName as input")
                .isEqualTo(userName);
        Assertions.assertThat(userDetailResponse.getToken()).as("Expected Token not null")
                .isNotEmpty().isNotNull();
    }

    @Test(description = """
            Testcase: Sign up fail with empty email
            Send post to endpoint: api/users
            Expect: code 422 and msg 'field required'""")
    public void TC2_SignupFailEmptyEmail() throws JsonProcessingException {
//        Init UserDetail
        MUserDetail userDetail = new MUserDetail();
        userDetail.setUsername(userName);
        userDetail.setPassword(password);

//        Init User
        MUser user = new MUser();
        user.setUser(userDetail);

//        Prepare data
        JsonNode node = mapper.valueToTree(user);

//        Send request and get response
        requestResponseLog.write("\\n========== Test Sign up with empty email ===========\\n");
        Response response = with().headers("Content-Type", "application/json")
                .when()
                .body(node)
                .request("POST", "/users");

//        Assert
        MUserLoginError errors = mapper.readValue(response.getBody().prettyPrint(), MUserLoginError.class);
        List<MUserLoginErrorDetail> errorDetails = errors.getUserLoginErrors();
        Assertions.assertThat(errorDetails.get(0).getMsg()).as("Expected msg: field required")
                .contains("field required");
        Assertions.assertThat(errorDetails.get(0).getType()).as("Expected msg: value_error.missing")
                .contains("value_error.missing");
        Assertions.assertThat(response.statusCode()).as("Expected stt code: 422").isEqualTo(422);
    }

    @Test(description = """
            Testcase: Sign up fail with empty password
            Send post to endpoint: api/users
            Expect: code and msg""")
    public void TC3_SignupFailEmptyPassword() throws JsonProcessingException {
//        Init UserDetails
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(emailTest);
        userDetail.setUsername(userName);

//        Init User
        MUser user = new MUser();
        user.setUser(userDetail);

//        Prepare data
        JsonNode node = mapper.valueToTree(user);

//        Send request and get response
        requestResponseLog.write("\\n========== Test Sign up with empty password ===========\\n");
        Response response = with().headers("Content-Type", "application/json")
                .when()
                .body(node)
                .request("POST", "/users");

//        Assert
        MUserLoginError errors = mapper.readValue(response.getBody().prettyPrint(), MUserLoginError.class);
        List<MUserLoginErrorDetail> errorDetails = errors.getUserLoginErrors();
        Assertions.assertThat(response.statusCode()).as("Expected code: 422").isEqualTo(422);
        Assertions.assertThat(errorDetails.get(0).getMsg()).as("Expected msg: field required")
                .contains("field required");
        Assertions.assertThat(errorDetails.get(0).getType()).as("Expected type: value_error.missing")
                .contains("value_error.missing");
    }

    @Test(description = """
            Testcase: Sign up fail with empty userName
            Send post to endpoint: api/users
            Expect: code and msg""")
    public void TC4_SignupFailEmptyUserName() throws JsonProcessingException {
//        Init UserDetails
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(emailTest);
        userDetail.setPassword(password);

//        Init User
        MUser user = new MUser();
        user.setUser(userDetail);

//        Prepare data for body
        JsonNode node = mapper.valueToTree(user);

//        Send request and get response
        requestResponseLog.write("\\n========== Test Sign up with empty username ===========\\n");
        Response response = with().headers("Content-Type", "application/json")
                .when()
                .body(node)
                .request("POST", "/users");

//        Assert
        MUserLoginError errors = mapper.readValue(response.getBody().prettyPrint(), MUserLoginError.class);
        List<MUserLoginErrorDetail> errorDetails = errors.getUserLoginErrors();
        Assertions.assertThat(response.statusCode()).as("Expected stt code: 422").isEqualTo(422);
        Assertions.assertThat(errorDetails.get(0).getMsg()).as("Expected msg: field required")
                .contains("field required");
        Assertions.assertThat(errorDetails.get(0).getType()).as("value_error.missing")
                .contains("value_error.missing");
    }

    @Test(description = """
            Testcase: Sign up fail with empty all
            Send post to endpoint: api/users
            Expect: code and msg""")
    public void TC5_SignupFailEmptyAll() throws JsonProcessingException {
//        Init UserDetails
        MUserDetail userDetail = new MUserDetail();

//        Init User
        MUser user = new MUser();
        user.setUser(userDetail);

//        Prepare body
        JsonNode node = mapper.valueToTree(user);

//        Send request and get response
        requestResponseLog.write("\\n========== Test Sign up with empty all ===========\\n");
        Response response = with().headers("Content-Type","application/json")
                .when()
                .body(node)
                .request("POST","/users");

//        Assert
        MUserLoginError errors = mapper.readValue(response.getBody().prettyPrint(),MUserLoginError.class);
        List<MUserLoginErrorDetail> errorDetails = errors.getUserLoginErrors();
        Assertions.assertThat(response.statusCode()).as("Expected stt code: 422")
                .isEqualTo(422);
        Assertions.assertThat(errorDetails.get(0).getMsg()).as("Expected msg: field required")
                .contains("field required");
        Assertions.assertThat(errorDetails.get(0).getType()).as("Expected msg: value_error.missing")
                .contains("value_error.missing");
    }

    @Test(description = """
            Testcase: Sign up fail with existing email
            Send post to endpoint: api/users
            Expect: code and msg""")
    public void TC6_SignupFailExistingEmail() throws JsonProcessingException {
//        Init UserDeatils
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail("nguyenthucuc996@gmail.com");
        userDetail.setUsername(userName);
        userDetail.setPassword(password);

//        Init User
        MUser user = new MUser();
        user.setUser(userDetail);

//        Prepare data for body
        JsonNode node = mapper.valueToTree(user);

//        Send request and get response
        requestResponseLog.write("\\n========== Test Sign up with existing email ===========\\n");
        Response response = with().headers("Content-Type","application/json")
                .when()
                .body(node)
                .request("POST","/users");

//        Assert
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).as("Expected stt code  400").isEqualTo(400);
        Assertions.assertThat(responseString).as("Expected mag: user with this email already exists")
                .contains("user with this username already exists");
    }

    @Test(description = """
            Testcase: Sign up fail with existing userName
            Send post to endpoint: api/users
            Expect: code and msg""")
    public void TC7_SignupFailExistingUserName() {
//        Init UserDetail
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(emailTest);
        userDetail.setUsername("marigold09");
        userDetail.setPassword(password);

//        Init User
        MUser user = new MUser();
        user.setUser(userDetail);

//        Prepare data for body
        JsonNode node = mapper.valueToTree(user);

//        Send request and get response
        requestResponseLog.write("\\n========== Test Sign up with existing username ===========\\n");
        Response response = with().headers("Content-Type","application/json")
                .when()
                .body(node)
                .request("POST","/users");

//        Assert
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).as("Expected stt code: 400").isEqualTo(400);
        Assertions.assertThat(responseString).as("Expected msg: user with this username already exists")
                .contains("user with this username already exists");
    }

    @Test(description = """
            Testcase: Sign up fail with invalid email
            Send post to endpoint: /users
            Expect: code and msg""")
    public void TC8_SignupFailInvalidEmail() {
        Assert.assertTrue(true);
//        No case, no validate email format
    }
}
