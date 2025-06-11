package testsuite.api.articles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testsuite.model.article.MArticle;
import testsuite.model.article.MArticleResponse;

import java.time.Instant;
import java.util.List;

import static io.restassured.RestAssured.with;

public class TestGetArticleList {
    private final ObjectMapper mapper = new ObjectMapper();
    @BeforeClass
    public void BeforeClass(){
        RestAssured.baseURI = "https://realworld-api.ap.ngrok.io/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
    @Test(description = """
            Testcase: Get article without params
             Send get to end point: /articles
             Expected code 200 and response 20 articles from latest""")
    public void TC1_GetArticleWithoutParam() throws JsonProcessingException {
        Response response = with().headers("Content-Type", "application/json")
                .when()
                .request("GET","/articles");
        MArticleResponse mArticleListResponse = mapper.readValue(response.getBody().prettyPrint(),
                MArticleResponse.class);
        Assertions.assertThat(mArticleListResponse.getArticlesCount()).as("Expected: 20")
                .isEqualTo(20);
        List<MArticle> articleList = mArticleListResponse.getArticles();
        Instant createdAt_art01 = Instant.parse(articleList.get(0).getCreatedAt());
        boolean checkoldest = true;
        for(int i =1; i<articleList.size(); i++){
            Instant createdAt = Instant.parse(articleList.get(i).getCreatedAt());
            if(createdAt_art01.isBefore(createdAt)){
            }else {
                System.out.println("Article 01 is not oldest");
                checkoldest =false;
            }
        }
        Assert.assertTrue(checkoldest);
    }

}
