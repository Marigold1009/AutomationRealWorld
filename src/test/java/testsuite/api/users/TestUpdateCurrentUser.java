package testsuite.api.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testsuite.config.ApiDataFactory;
import testsuite.model.article.MArticle;
import testsuite.model.article.MArticlesResponse;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;
import testsuite.utils.ApiLogFactory;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.with;
import static testsuite.config.ApiDataFactory.expired_token;

public class TestUpdateCurrentUser {
    private final ObjectMapper mapper = new ObjectMapper();
    private StringWriter requestResponseLog;
    private PrintStream requestResponseCaaptureStream;
    private String token;
    private MUser userUpdate = new MUser();
    private MUserDetail userDetailUpdate = new MUserDetail();
    private String existingUserName;

    @BeforeClass
    public void BeforeClass() throws JsonProcessingException {
        RestAssured.baseURI = ApiDataFactory.API_URL;
        requestResponseLog = ApiLogFactory.getWriter();
        requestResponseCaaptureStream = ApiLogFactory.getStream();
        RestAssured.requestSpecification =
                RestAssured.given().filters(new RequestLoggingFilter(requestResponseCaaptureStream)
                        , new ResponseLoggingFilter(requestResponseCaaptureStream));

//        Create an account and store token to test update acc
        MUserDetail userDetail = new MUserDetail();
        userDetail.setUsername("cuctestupdate");
        userDetail.setEmail("cuctestupdate@gmail.com");
        userDetail.setPassword("1234567890");
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = with().header("Content-Type", "application/json")
                .when().body(node).request("POST", "/users");
        if (response.statusCode() == 200) {
            user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
            userDetail = user.getUser();
            token = userDetail.getToken();
        }else {
            Response response1 = with().header("Content-Type", "application/json")
                    .when().body(node).request("POST", "/users/login");
            user = mapper.readValue(response1.getBody().prettyPrint(), MUser.class);
            userDetail = user.getUser();
            token = userDetail.getToken();
        }

//        Store 1 existing user
        Response response1 = with().when().request("GET", "/articles");
        MArticlesResponse mArticlesResponse = mapper.readValue(response1.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList = mArticlesResponse.getArticles();
        List<String> authors = new ArrayList<>();
        String author = "";
        for (MArticle article : articleList) {
            if (!authors.contains(author)) {
                authors.add(article.getAuthor().getUsername());
            }
        }
        existingUserName = authors.get(0);
    }

    @AfterClass
    public void AfterClass() {
        MUserDetail userDetailFinal = new MUserDetail();
        userDetailFinal = userDetailUpdate;
        userDetailFinal.setUsername("cuctestupdate");
        userDetailFinal.setEmail("cuctestupdate@gmail.com");
        userDetailFinal.setPassword("1234567890");
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(userDetailFinal).request("PUT", "/user");
    }

    @BeforeMethod
    public void BeforeMethod() throws JsonProcessingException {
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().request("GET", "/user");
        userUpdate = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        userDetailUpdate = userUpdate.getUser();
    }

    @Test(description = """
            Testcase update url successfully
            Send put to endpoint api/user
            Expect code and response data""", priority = 0)
    public void TC1_UpdateURLSuccess() throws JsonProcessingException {
        userDetailUpdate.setImage("https://images.ctfassets.net/hrltx12pl8hq/3Z1N8LpxtXNQhBD5EnIg8X/975e2497dc598bb64fde390592ae1133/spring-images-min.jpg");
        JsonNode node = mapper.valueToTree(userUpdate);
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(node).request("PUT", "/user");
        MUser user = new MUser();
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        MUserDetail userDetail = new MUserDetail();
        userDetail = user.getUser();
        token = userDetail.getToken();
        Assertions.assertThat(userDetail).usingRecursiveComparison().ignoringFields("token").isEqualTo(userDetailUpdate);
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        userDetailUpdate = userDetail;
        userUpdate = user;
    }

    @Test(description = """
            Testcase update userName successfully""", priority = 0)
    public void TC2_UpdateUserNameSuccess() throws JsonProcessingException {
        userDetailUpdate.setUsername(userDetailUpdate.getUsername() + System.currentTimeMillis());
        JsonNode node = mapper.valueToTree(userUpdate);
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(node).request("PUT", "/user");
        MUser user = new MUser();
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        MUserDetail userDetail = new MUserDetail();
        userDetail = user.getUser();
        token = userDetail.getToken();
        Assertions.assertThat(userDetail).usingRecursiveComparison().ignoringFields("token").isEqualTo(userDetailUpdate);
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        userDetailUpdate = userDetail;
        userUpdate = user;
    }

    @Test(description = """
            Testcase update Description successfully""", priority = 1)
    public void TC3_UpdateDescriptionSuccess() throws JsonProcessingException {
        userDetailUpdate.setBio(userDetailUpdate.getBio() + System.currentTimeMillis());
        JsonNode node = mapper.valueToTree(userUpdate);
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(node).request("PUT", "/user");
        MUser user = new MUser();
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        MUserDetail userDetail = new MUserDetail();
        userDetail = user.getUser();
        token = userDetail.getToken();
        Assertions.assertThat(userDetail).usingRecursiveComparison().ignoringFields("token").isEqualTo(userDetailUpdate);
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        userDetailUpdate = userDetail;
        userUpdate = user;
    }

    @Test(description = """
            Testcase update email successfully""", priority = 1)
    public void TC4_UpdateEmailSuccess() throws JsonProcessingException {
        String email = userDetailUpdate.getEmail();
        String[] parts = email.split("@");
        String part01 = parts[0];
        String part02 = parts[1];
        String emailUpdate = part01 + System.currentTimeMillis() + "@" + part02;
        userDetailUpdate.setEmail(emailUpdate);
        JsonNode node = mapper.valueToTree(userUpdate);
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(node).request("PUT", "/user");
        MUser user = new MUser();
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        MUserDetail userDetail = new MUserDetail();
        userDetail = user.getUser();
        token = userDetail.getToken();
        Assertions.assertThat(userDetail).usingRecursiveComparison().ignoringFields("token").isEqualTo(userDetailUpdate);
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        userDetailUpdate = userDetail;
        userUpdate = user;
    }

    @Test(description = """
            Testcase update password successfully""", priority = 1)
    public void TC5_UpdatePasswordSuccess() throws JsonProcessingException {
        userDetailUpdate.setPassword("123456");
        JsonNode node = mapper.valueToTree(userUpdate);
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(node).request("PUT", "/user");
        MUser user = new MUser();
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        MUserDetail userDetail = new MUserDetail();
        userDetail = user.getUser();
        token = userDetail.getToken();
        Assertions.assertThat(userDetail).usingRecursiveComparison().ignoringFields("token").ignoringFields("password").isEqualTo(userDetailUpdate);
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        userDetailUpdate = userDetail;
        userUpdate = user;
    }

    @Test(description = """
            Testcase update userName successfully""", priority = 1)
    public void TC6_UpdateUsernameSuccess() throws JsonProcessingException {
        userDetailUpdate.setUsername(userDetailUpdate.getUsername() + System.currentTimeMillis());
        JsonNode node = mapper.valueToTree(userUpdate);
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(node).request("PUT", "/user");
        MUser user = new MUser();
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        MUserDetail userDetail = new MUserDetail();
        userDetail = user.getUser();
        token = userDetail.getToken();
        Assertions.assertThat(userDetail).usingRecursiveComparison().ignoringFields("token").isEqualTo(userDetailUpdate);
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        userDetailUpdate = userDetail;
        userUpdate = user;
    }

    @Test(description = """
            Testcase update all fields""", priority = 1)
    public void TC6_UpdateAllFieldsSuccess() throws JsonProcessingException {
        String email = userDetailUpdate.getEmail();
        String[] parts = email.split("@");
        String part01 = parts[0];
        String part02 = parts[1];
        String emailUpdate = part01 + System.currentTimeMillis() + "@" + part02;
        userDetailUpdate.setEmail(emailUpdate);
        userDetailUpdate.setPassword(userDetailUpdate.getPassword() + System.currentTimeMillis());
        userDetailUpdate.setBio(userDetailUpdate.getBio() + System.currentTimeMillis());
        userDetailUpdate.setImage(userDetailUpdate.getImage() + System.currentTimeMillis());
        userDetailUpdate.setUsername(userDetailUpdate.getUsername() + System.currentTimeMillis());
        JsonNode node = mapper.valueToTree(userUpdate);
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(node).request("PUT", "/user");
        MUser user = new MUser();
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        MUserDetail userDetail = new MUserDetail();
        userDetail = user.getUser();
        token = userDetail.getToken();
        Assertions.assertThat(userDetail).usingRecursiveComparison().ignoringFields("token","password").isEqualTo(userDetailUpdate);
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        userDetailUpdate = userDetail;
        userUpdate = user;
    }

    @Test(description = """
            Testcase update fail with empty userName""", priority = 2)
    public void TC8_UpdateSuccessWithEmptyUserName() throws JsonProcessingException {
        MUserDetail userDetailemptyUserName = new MUserDetail();
        userDetailemptyUserName = userDetailUpdate;
        userDetailemptyUserName.setUsername("");
        MUser userEmptyUserName = new MUser();
        userEmptyUserName.setUser(userDetailemptyUserName);
        JsonNode node = mapper.valueToTree(userEmptyUserName);
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(node).request("PUT", "/user");
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        MUser user = new MUser();
        user = mapper.readValue(response.getBody().prettyPrint(),MUser.class);
        MUserDetail userDetail = new MUserDetail();
        userDetail = user.getUser();
        Assertions.assertThat(userDetail).usingRecursiveComparison().ignoringFields("token","password")
                .isEqualTo(userDetailUpdate);
        token = userDetail.getToken();
    }

    @Test(description = """
            Testcase update fail with existing userName""", priority = 2)
    public void TC9_UpdateFailWithExistingUserName() {
        userDetailUpdate.setUsername(existingUserName);
        JsonNode node = mapper.valueToTree(userUpdate);
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(node).request("PUT", "/user");
        Assertions.assertThat(response.statusCode()).isEqualTo(400);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("user with this username already exists");
    }

    @Test(description = """
            Testcase update fail with empty email""", priority = 2)
    public void TC10_UpdateFailWithEmptyEmail() throws JsonProcessingException {
        MUserDetail userDetailEmptyEmail = new MUserDetail();
        MUser useremptyEmail = new MUser();
        userDetailEmptyEmail = userDetailUpdate;
        userDetailEmptyEmail.setEmail("");
        useremptyEmail.setUser(userDetailEmptyEmail);
        JsonNode node = mapper.valueToTree(useremptyEmail);
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(node).request("PUT", "/user");
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        MUser user = new MUser();
        user = mapper.readValue(response.getBody().prettyPrint(),MUser.class);
        MUserDetail userDetail = new MUserDetail();
        userDetail = user.getUser();
        Assertions.assertThat(userDetail).usingRecursiveComparison().ignoringFields("token")
                .isEqualTo(userDetailUpdate);
        userDetailUpdate = userDetail;

    }

    @Test(description = """
            Testcase update fail with empty password""", priority = 2)
    public void TC11_UpdatePassWithEmptyPassword() throws JsonProcessingException {
        MUserDetail userDetailemptyPassword = new MUserDetail();
        userDetailemptyPassword = userDetailUpdate;
        userDetailemptyPassword.setUsername("");
        MUser userEmptyPassword = new MUser();
        userEmptyPassword.setUser(userDetailemptyPassword);
        JsonNode node = mapper.valueToTree(userEmptyPassword);
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + token)
                .when().body(node).request("PUT", "/user");
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        MUser user = mapper.readValue(response.getBody().prettyPrint(),MUser.class);
        MUserDetail userDetail = user.getUser();
        token = userDetail.getToken();
        Assertions.assertThat(userDetail).usingRecursiveComparison().ignoringFields("token","password")
                .isEqualTo(userDetailUpdate);
    }

    @Test(description = """
            Testcase update fail_missing token""", priority = 2)
    public void TC12_UpdateFailMissingToken() {
        userDetailUpdate.setBio(userDetailUpdate.getBio() + System.currentTimeMillis());
        JsonNode node = mapper.valueToTree(userUpdate);
        Response response = with()
                .when().body(node).request("PUT", "/user");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("authentication required");
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
    }

    @Test(description = """
            Testcase update fail_invalid token""", priority = 2)
    public void TC13_UpdateFailWithInvalidToken() {
        userDetailUpdate.setBio(userDetailUpdate.getBio() + System.currentTimeMillis());
        JsonNode node = mapper.valueToTree(userUpdate);
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token" + token)
                .when().body(node).request("PUT", "/user");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("unsupported authorization type");
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
    }

    @Test(description = """
            Testcase update fail_expired token""", priority = 2)
    public void TC14_UpdateFailWithExpiredToken() {
        userDetailUpdate.setBio(userDetailUpdate.getBio() + System.currentTimeMillis());
        JsonNode node = mapper.valueToTree(userUpdate);
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token " + expired_token)
                .when().body(node).request("PUT", "/user");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("could not validate credentials");
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
    }

    @Test(description = """
            Testcase update fail_wrong format token""", priority = 2)
    public void TC15_UpdateFailWithWrongFormatToken() {
        String invalidToken = "adgsgjsdsfagdsfgdsgdsjg";
        userDetailUpdate.setBio(userDetailUpdate.getBio() + System.currentTimeMillis());
        JsonNode node = mapper.valueToTree(userUpdate);
        Response response = with().headers("Content-Type", "application/json", "Authorization", "Token" + invalidToken)
                .when().body(node).request("PUT", "/user");
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("unsupported authorization type");
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
    }
}
