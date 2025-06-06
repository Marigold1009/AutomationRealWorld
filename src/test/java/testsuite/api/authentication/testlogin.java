package testsuite.api.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;

import static io.restassured.RestAssured.with;

public class TestLogin {
    private final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeClass
    public void beforeClass() {
        RestAssured.baseURI = "https://realworld-api.ap.ngrok.io/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }


    @DataProvider(name = "valid_credentials")
    public Object[][] dpMethod() {
        return new Object[][]{{"thang@gmail.com", "123456"}};
    }

    @Test(description = """
            Testcase: Login success with credetial
            Send post to endpoint: api/user/login
            Expect: Return token""", dataProvider = "valid_credentials")
    public void TC1_LoginSuccess(String email, String password) throws JsonProcessingException {
        // Init user details
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail(email);
        userDetail.setPassword(password);
        // Init user
        MUser user = new MUser();
        user.setUser(userDetail);

        // Prepare request body
        JsonNode node = MAPPER.valueToTree(user);

        // Send request and get response
        Response response = with().header("Content-Type", "application/json").body(node)
                .when()
                .request("POST", "/users/login");

        Assertions.assertThat(response.statusCode()).as("Expected response status is 200").isEqualTo(200);
        user = MAPPER.readValue(response.getBody().prettyPrint(), MUser.class);

        Assertions.assertThat(user.getUser().getToken()).as("Expected token is not null or empty").isNotNull().isNotEmpty();
    }

    @Test(description = """
            Testcase: Login fail with empty email""",
            dataProvider = "valid_credentials")
    public void TC2_LoginFailWithEmptyEmail(String email, String password) throws JsonProcessingException {
        // Init user details
        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail("");
        userDetail.setPassword(password);
        // Init user
        MUser user = new MUser();
        user.setUser(userDetail);

        // Prepare request body
        JsonNode node = MAPPER.valueToTree(user);

        // Send request and get response
        Response response = with().header("Content-Type", "application/json").body(node)
                .when()
                .request("POST", "/users/login");

        Assertions.assertThat(response.statusCode()).as("Expected response status is 422").isEqualTo(422);
        String stringResponse = response.getBody().prettyPrint();

        Assertions.assertThat(stringResponse).as("Expected error message").contains("value is not a valid email address")
                .contains("value_error.email");
    }

    @Test(description = """
            Testcase: Login fail with empty password""")
    public void TC3_LoginFailWithEmptyPassword(String email) {

//        Set body --> Call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Login fail with empty require field""")
    public void TC4_LoginFailWithEmptyAll() {

//        Call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Login fail with wrong email""")
    public void TC5_LoginFailWithWrongEmail(String email, String password) {

//        Set body --> Call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Login fail with wrong password""")
    public void TC6_LoginFailWithWrongEmail(String email, String password) {

//        Set body --> Call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Login fail with wrong email and password""")
    public void TC7_LoginFailWithWrongAll(String email, String password) {

//        Set body --> Call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Login fail with no existing email""")
    public void TC8_LoginFailWithNonExistingEmail(String email, String password) {

//        Set body --> Call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Login fail with locked account""")
    public void TC9_LoginFailWithLockedAccount(String password) {

//        Set body --> Call API --> Verify code and msg
    }

    // Need more case 
    // Login with params /api/user/login?request=true
    // Login upper case /api/user/Login?request=true
    // Login with Content-Type is not application/json
}
