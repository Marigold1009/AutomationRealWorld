package testsuite.api.articles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testsuite.config.ApiDataFactory;
import testsuite.model.article.MArticle;
import testsuite.model.article.MArticleCreate;
import testsuite.model.article.MArticleResponse;
import testsuite.model.article.MArticlesResponse;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;

import java.util.List;

import static io.restassured.RestAssured.with;

public class TestDeleteArticle {
    private final ObjectMapper mapper = new ObjectMapper();
    private String token;
    private String validSlug;
    private String invalidSlug;
    private String notauthorSlug;
    private String userName;

    @BeforeClass
    public void BeforeClass() throws JsonProcessingException {
        RestAssured.baseURI = ApiDataFactory.API_URL;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());


        MUserDetail userDetail = new MUserDetail();
        userDetail.setEmail("nguyenthucuc996+1009@gmail.com");
        userDetail.setPassword("123456");
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = with().header("Content-Type","application/json")
                .when().body(node).request("POST","/users/login");
        user = mapper.readValue(response.getBody().prettyPrint(),MUser.class);
        userDetail = user.getUser();
        token = userDetail.getToken();
        userName = userDetail.getUsername();

        Response response2 = with().request("GET", "/articles");
        MArticlesResponse articlesResponse1 = mapper.readValue(response2.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList = articlesResponse1.getArticles();
        for (MArticle article : articleList) {
            if (!article.getAuthor().getUsername().equals(userName)) {
                notauthorSlug = article.getSlug();
                break;
            }
        }
    }

    @BeforeMethod
    public void BeforeMethod() throws JsonProcessingException {
//        Create article and get slug
        MArticle article = new MArticle();
        article.setTitle("Test delete article"+System.currentTimeMillis());
        article.setBody("Test delete article_Body");
        article.setDescription("Test delete article_Description");
        MArticleCreate articleCreate = new MArticleCreate();
        articleCreate.setArticle(article);
        JsonNode node = mapper.valueToTree(articleCreate);
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().body(node).request("POST","/articles");
        MArticleResponse mArticleResponse = mapper.readValue(response.getBody().prettyPrint(),MArticleResponse.class);
        MArticle article1 = mArticleResponse.getArticle();
        validSlug = article1.getSlug();
        invalidSlug = article1.getSlug() + System.currentTimeMillis();
    }

    @AfterMethod
    public void AfterMethod(){
//        Delete article
        with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+validSlug);
    }

    @Test(description = """
            Testcase: Delete article sucessfully
            Send DELETE to endpoint: /articles/{slug}
            Expected code 204""")
    public void TC1_DeleteArticle_Success(){
        Response response = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("DELETE","/articles/"+validSlug);
        Assertions.assertThat(response.statusCode()).isEqualTo(204);
    }

    @Test(description = """
            Testcase: Delete article fail, no header
            Send Delete to endpoint /articles/{slug}
            Expected code and msg""")
    public void TC2_DeleteArticle_Unsuccess_Noheader(){
        Response response = with().when().request("DELETE","/articles/"+validSlug);
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("authentication required");
    }

    @Test(description = """
            Testcase: Delete article fail, wrong header""")
    public void TC3_DeleteArticle_Unsuccess_WrongFormatToken(){
        Response response = with().headers("Content-Type","application/json","Authorization","Token"+token)
        .when().request("DELETE","/articles/"+validSlug);
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("unsupported authorization type");
    }

    @Test(description = """
            Testcase: Delete article fail, expired token""")
    public void TC4_DeleteArticle_Fail_ExpiredToken(){
        String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Im1hcmlnb2xkMDkiLCJleHAiOjE3NDY5NzMzNjYsInN1YiI6ImFjY2VzcyJ9.w3-XXsq1yZX6ItP3aBOFBjiYWPeTQ72pADYH504hV-Y";
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+expiredToken)
                .when().request("DELETE","/articles/"+validSlug);
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("could not validate credentials");
    }

    @Test(description = """
            Testcase: Delete fail article non existing""")
    public void TC5_DeleteArticle_Fail_NonExisting(){
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+invalidSlug);
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("article does not exist");
    }

    @Test(description = """
            Testcase: Delete fail article is an other author""")
    public void TC6_DeleteArticle_Fail_AnOtherAuthor(){
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles/"+notauthorSlug);
        Assertions.assertThat(response.statusCode()).isEqualTo(403);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("you are not an author of this article");
    }

    @Test(description = """
            Testcase: Delete fail without slug""")
    public void TC7_Delete_fail_NoSlug(){
        Response response = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("DELETE","/articles");
        Assertions.assertThat(response.statusCode()).isEqualTo(405);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).contains("Method Not Allowed");
    }
}
