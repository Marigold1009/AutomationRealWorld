package testsuite.api.authentication;


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
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;
import testsuite.model.user.MUserLoginError;
import testsuite.model.user.MUserLoginErrorDetail;

import java.util.List;

import static io.restassured.RestAssured.with;

public class Testlogin {
    private ObjectMapper MAPPER = new ObjectMapper();

    @BeforeClass
    public void BeforeClass() {
        RestAssured.baseURI = "https://realworld-api.ap.ngrok.io/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @DataProvider(name = "valid_credentials")
    public Object[][] dpMethod_validCredentials() {
        return new Object[][]{{"nguyenthucuc996@gmail.com", "Cucvantho09"}};
    }

    @DataProvider(name = "wrong_email")
    public Object[][] dpMethod_wronfEmail() {
        return new Object[][]{{"abc.com", "Cuvantho09"}};
    }

    @DataProvider(name = "wrong_password")
    public Object[][] dpMethod_WrongPassword() {
        return new Object[][]{{"nguyenthucuc996@gmail.com", "abc"}};
    }

    @DataProvider(name = "wrong_allFields")
    public Object[][] dpMethod_WrongAllFields() {
        return new Object[][]{{"abc.com", "abc"}};
    }

    @Test(description = """
            Testcase: Login success with valid credetial
            Send post to endpoint: api/user/login
            Expect: Return token""", dataProvider = "valid_credentials")
    public void TC1_Loginsuccess(String email, String password) throws JsonProcessingException {
//        Init Userdetail
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(email);
        userDetail.setPassword(password);

//        Init User
        MUser user = new MUser();
        user.setUser(userDetail);

//        Prepare requset for body
        JsonNode node = MAPPER.valueToTree(user);

//        Send request and get response
        Response response = with().header("Content-Type", "application/json").body(node)
                .when()
                .request("POST", "users/login");

//        Assert code status and response
        Assertions.assertThat(response.statusCode()).as("Expected response status is 200")
                .isEqualTo(200);
        user = MAPPER.readValue(response.getBody().prettyPrint(), MUser.class);

        Assertions.assertThat(user.getUser().getToken()).as("Expected Token is not null or empty")
                .isNotNull().isNotEmpty();
    }

    @Test(description = """
            Testcase: Login fail with empty email
            Send post to end point: api/users/login
            Expected show code 422 and message""", dataProvider = "valid_credentials")
    public void TC2_LoginFailWithEmptyEmail(String email, String password) throws JsonProcessingException {
//       Init UserDetail
        MUserDetail userDetail = new MUserDetail();
        userDetail.setPassword(password);

//        Init User
        MUser user = new MUser();
        user.setUser(userDetail);

//        Prepare data for body
        JsonNode node = MAPPER.valueToTree(user);

//        Send request and get response
        Response response = with().header("Content-Type", "application/json")
                .body(node)
                .when()
                .request("POST", "/users/login");

//        Assert code and response
        MUserLoginError errors = MAPPER.readValue(response.getBody().prettyPrint(), MUserLoginError.class);
        List<MUserLoginErrorDetail> errorDetails = errors.getUserLoginErrors();
        Assertions.assertThat(errorDetails.get(0).getMsg()).as("Expected msg 'field required'")
                .contains("field required");
        Assertions.assertThat(errorDetails.get(0).getType()).as("Expected type 'value_error.missing'")
                .contains("value_error.missing");
    }

    @Test(description = """
            Testcase: Login fail with empty password
            Send request to endpoint: api/users/login
            Expected code 422  and msg 'field required'""", dataProvider = "valid_credentials")
    public void TC3_LoginFailWithEmptyPassword(String email, String password) throws JsonProcessingException {
//        Init UserDetail
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(email);

//        Init User
        MUser user = new MUser();
        user.setUser(userDetail);

//        Prepare Body
        JsonNode node = MAPPER.valueToTree(user);

//        Send request and get response
        Response response = with().header("Content-Type", "application/json")
                .when()
                .body(node)
                .request("POST", "users/login");

//        Assert
        MUserLoginError errors = MAPPER.readValue(response.getBody().prettyPrint(), MUserLoginError.class);
        List<MUserLoginErrorDetail> errorDetails = errors.getUserLoginErrors();
        Assertions.assertThat(errorDetails.get(0).getMsg()).as("Expected msg: 'field required")
                .contains("field required");
        Assertions.assertThat(errorDetails.get(0).getType()).as("Expected type: 'value_error.missing'")
                .contains("value_error.missing");
        Assertions.assertThat(response.statusCode()).as("Expected Status code is 422")
                .isEqualTo(422);
    }

    @Test(description = """
            Testcase: Login fail with empty require field
            Send Post to end point: users/login
            Expected code 422 and msg 'value is not a valid email address'""", dataProvider = "valid_credentials")
    public void TC4_LoginFailWithEmptyAll(String email, String password) throws JsonProcessingException {
//        Init UserDetails
        MUserDetail userDetail = new MUserDetail();

//        Init User
        MUser user = new MUser();
        user.setUser(userDetail);

//        Prepare data for body
        JsonNode node = MAPPER.valueToTree(user);

//        Send request
        Response response = with().headers("Content-Type", "application/json")
                .when()
                .body(node)
                .request("POST", "users/login");

//        Assert
        MUserLoginError errors = MAPPER.readValue(response.getBody().prettyPrint(), MUserLoginError.class);
        List<MUserLoginErrorDetail> errorDetail = errors.getUserLoginErrors();
        Assertions.assertThat(response.statusCode()).as("Expected code is 422").isEqualTo(422);
        Assertions.assertThat(errorDetail.get(0).getMsg()).as("Expected msg 'field required'")
                .contains("field required");
        Assertions.assertThat(errorDetail.get(0).getType()).as("Expected type is 'value_error.missing'")
                .contains("value_error.missing");
    }

    @Test(description = """
            Testcase: Login fail with wrong email
            Send post to end point: users/login
            Expected msg and code""", dataProvider = "wrong_email")
    public void TC5_LoginFailWithWrongEmail(String email, String password) throws JsonProcessingException {
//        Init UserDetails
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(email);
        userDetail.setPassword(password);

//        Init User
        MUser user = new MUser();
        user.setUser(userDetail);

//        Prepare data for body
        JsonNode node = MAPPER.valueToTree(user);

//        Send request and get response
        Response response = with().headers("Content-Type", "application/json")
                .when()
                .body(node)
                .request("POST", "users/login");

//        Assert
        MUserLoginError errors = MAPPER.readValue(response.getBody().prettyPrint(), MUserLoginError.class);
        List<MUserLoginErrorDetail> errorDetails = errors.getUserLoginErrors();
        Assertions.assertThat(response.statusCode()).as("Expected stt code is 422")
                .isEqualTo(422);
        Assertions.assertThat(errorDetails.get(0).getMsg()).as("Expected msg is 'value is not a valid email address'")
                .contains("value is not a valid email address");
        Assertions.assertThat(errorDetails.get(0).getType()).as("Expected type: value_error.email")
                .contains("value_error.email");
    }

    @Test(description = """
            Testcase: Login fail with wrong password
            Send post to end point: users/login
            Expected code 400 and msg""", dataProvider = "wrong_password"
    )
    public void TC6_LoginFailWithWrongPassword(String email, String password) throws JsonProcessingException {
//        Init UserDetail
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(email);
        userDetail.setPassword(password);

//        Init User
        MUser user = new MUser();
        user.setUser(userDetail);

//        Prepare data for body
        JsonNode node = MAPPER.valueToTree(user);

//        Send request and get response
        Response response = with().headers("Content-Type", "application/json")
                .when()
                .body(node)
                .request("POST", "users/login");

//        Assert
        String stringResponse = response.getBody().prettyPrint();
        Assertions.assertThat(response.statusCode()).as("Expected stt code: 422").isEqualTo(400);
        Assertions.assertThat(stringResponse).as("Expected msg: incorrect email or password")
                .contains("incorrect email or password");
    }

    @Test(description = """
            Testcase: Login fail with wrond email and password
            Send Post to end point: users/login
            Expected code 400 and msg""", dataProvider = "wrong_allFields")
    public void TC7_LoginFailWithWrongAll(String email, String password) throws JsonProcessingException {
//        Init UserDetail
        MUserDetail userDetail =  new MUserDetail();
        userDetail.setEmail(email);
        userDetail.setPassword(password);

//        Init User
        MUser user = new MUser();
        user.setUser(userDetail);

//        Prepare data
        JsonNode node = MAPPER.valueToTree(user);

//        Send request and get response
        Response response = with().headers("Content-Type", "application/json")
                .when()
                .body(node)
                .request("POST","users/login");

//        Assert
        MUserLoginError errors = MAPPER.readValue(response.getBody().prettyPrint(),MUserLoginError.class);
        List<MUserLoginErrorDetail> errorDetails = errors.getUserLoginErrors();
        Assertions.assertThat(response.statusCode()).as("Expected stt code: 422").isEqualTo(422);
        Assertions.assertThat(errorDetails.get(0).getMsg()).as("Expected msg: value is not a valid email address")
                .contains("value is not a valid email address");
        Assertions.assertThat(errorDetails.get(0).getType()).as("Expected type: value_error.email")
                .contains("value_error.email");
    }

    @Test(description = """
            Testcase: Login fail with no existing email
            Send post to  endpoint: users/login
            Expected stt 422, and msg""", dataProvider = "wrong_email")
    public void TC8_LoginFailWithNonExistingEmail(String email, String password) throws JsonProcessingException {
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(email);
        userDetail.setPassword(password);

        MUser user = new MUser();
        user.setUser(userDetail);

        JsonNode node = MAPPER.valueToTree(user);
        Response resposne = with().headers("COntent-Type","application/json")
                .when()
                .body(node)
                .request("POST","users/login");

        MUserLoginError errors = MAPPER.readValue(resposne.getBody().prettyPrint(),MUserLoginError.class);
        List<MUserLoginErrorDetail> errorDetails = errors.getUserLoginErrors();
        Assertions.assertThat(resposne.statusCode()).as("Expected stt code: 422").isEqualTo(422);
        Assertions.assertThat(errorDetails.get(0).getMsg()).as("Expected msg: value is not a valid email address")
                .contains("value is not a valid email address");
        Assertions.assertThat(errorDetails.get(0).getType()).as("Expected Type: value_error.email")
                .contains("value_error.email");
    }

    @Test(description = """
            Testcase: Login fail with locked account""")
    public void TC9_LoginFailWithLockedAccount() {
            Assert.assertEquals("true","true");
//        Set body --> Call API --> Verify code and msg
    }
}
