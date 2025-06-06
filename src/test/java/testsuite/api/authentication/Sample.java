package testsuite.api.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;

import static io.restassured.RestAssured.with;

public class Sample {
    public static void main(String[] args) throws JsonProcessingException {
        // Init user
        MUserDetail user = new MUserDetail();
        user.setEmail("thang@gmail.com");
        user.setPassword("123456");

        MUser loginRequestBody = new MUser();
        loginRequestBody.setUser(user);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.valueToTree(loginRequestBody);

        System.out.println(node.toPrettyString());

        // Generate body request
        ObjectNode userNode = mapper.createObjectNode();
        userNode.set("user", node);

        //         Send POST request to log-in
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        RestAssured.baseURI = "https://realworld-api.ap.ngrok.io/api";

        // Send request and get response
        Response response = with().header("Content-Type", "application/json").body(node)
                .when()
                .request("POST", "/users/login");

        // Convert response body to String
        String strResponse = response.getBody().asPrettyString();

        // Convert response string to Json Node
        JsonNode responseNode = mapper.readTree(strResponse);

        // Access to user node and convert to MUser class
        loginRequestBody = mapper.readValue(strResponse, MUser.class);

        System.out.println(loginRequestBody.getUser().getToken());

        // Use token to post a comment
//        with().header("Content-Type", "application/json")
//                .header("Authorization", "Token " + user.getToken())
//                .body("{\"comment\":{\"body\":\"say hello\"}}")
//                .when()
//                .request("POST", "/articles/see-film-word-beyond-hundred/comments");
    }
}
