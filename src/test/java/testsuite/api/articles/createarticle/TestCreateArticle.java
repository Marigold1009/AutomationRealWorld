package testsuite.api.articles.createarticle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;

import static io.restassured.RestAssured.with;

public class TestCreateArticle {
    private final ObjectMapper MAPPER = new ObjectMapper();
    private String token;

    @BeforeClass
    public void beforeClass() throws JsonProcessingException {
        RestAssured.baseURI = "https://realworld-api.ap.ngrok.io/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        // Init user details
        MUserDetail userDetail = new MUserDetail();
        String email = "thang@gmail.com";
        String password= "123456";
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
        this.token = user.getUser().getToken();
    }

    // Case create article success --> Before method must delete article

    // Case cannot create existing article --> Before method, must create article


}
