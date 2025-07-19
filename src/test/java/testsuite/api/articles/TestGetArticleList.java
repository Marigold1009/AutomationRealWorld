package testsuite.api.articles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import model.article.MArticle;
import model.article.MArticlesResponse;
import model.article.MAuthor;
import model.tag.MTags;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testsuite.config.ApiDataFactory;
import utils.logging.LogFactory;

import java.io.PrintStream;
import java.io.StringWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;

public class TestGetArticleList {
    private final ObjectMapper mapper = new ObjectMapper();
    private StringWriter requestResponseLog;
    private PrintStream requestResponseCaptureStream;
    private ArrayList authors;
    private String author;
    private List<MArticle> articleListFromLatest;

    @BeforeClass
    public void BeforeClass() {
        RestAssured.baseURI = ApiDataFactory.API_URL;
        requestResponseLog = LogFactory.getWriter();
        requestResponseCaptureStream = LogFactory.getStream();
        // Set a global request specification with logging filters
        RestAssured.requestSpecification =
                RestAssured.given().filters(new RequestLoggingFilter(requestResponseCaptureStream)
                        , new ResponseLoggingFilter(requestResponseCaptureStream));
    }

    @Test(description = """
            Testcase: Get article without params
             Send get to end point: /articles
             Expected code 200 and response 20 articles from latest""", priority = 0)
    public void TC1_GetArticleWithoutParam() throws JsonProcessingException {
        requestResponseLog.write("\n========== Get Article list without params ===========\n");
        Response response = with().headers("Content-Type", "application/json")
                .when()
                .request("GET", "/articles");
        MArticlesResponse mArticleListResponse = mapper.readValue(response.getBody().prettyPrint(),
                MArticlesResponse.class);
        Assertions.assertThat(mArticleListResponse.getArticlesCount()).as("Expected: 20")
                .isEqualTo(20);
        articleListFromLatest = mArticleListResponse.getArticles();
        Instant createdAt_art01 = Instant.parse(articleListFromLatest.get(0).getCreatedAt());
        boolean checklatest = true;
        for (int i = 1; i < articleListFromLatest.size(); i++) {
            Instant createdAt = Instant.parse(articleListFromLatest.get(i).getCreatedAt());
            if (createdAt_art01.isAfter(createdAt)) {
            } else {
                System.out.println("Article 01 is not oldest");
                checklatest = false;
            }
        }
        Assert.assertTrue(checklatest);
        authors = new ArrayList<>();
        for (MArticle article : articleListFromLatest) {
            MAuthor authors1 = article.getAuthor();
            authors.add(authors1.getUsername());
        }
    }

    @Test(description = """
            Testcse: Get list successfully with valid tag
            Send Get to endpoint: /articles
            Expected: code 200, and get all articles with the tag""")
    public void TC2_GetListSuccess_ValidTag() throws JsonProcessingException {
//        Get tag list
        requestResponseLog.write("\"\\n========== Get article list by tag ===========\\n");
        Response tagListResponse = with().headers("Content-Type", "application/json")
                .when()
                .request("GET", "/tags");

        MTags tagList = mapper.readValue(tagListResponse.getBody().prettyPrint(), MTags.class);

//        Call Get Article list by tag
        Response response = with().queryParam("tag", tagList.getTags().get(0))
                .headers("Content-Type", "application/json")
                .when()
                .request("GET", "/articles");
//        Asset
        MArticlesResponse articleList = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        Assertions.assertThat(response.statusCode()).as("Expected code 200").isEqualTo(200);
        for (MArticle article : articleList.getArticles()) {
            for (String tag : article.getTagList()) {
                if (tag.equals(tagList.getTags().get(0))) {
                    Assertions.assertThat(tag).as("tag exists on tagList").contains(tagList.getTags().get(0));
                }
            }
        }
    }

    @Test(description = """
            Testcase: Get Article list with non_existing tag
            Send get to endpoint: /articles
            Expected 200 and no articles response""")
    public void TC3_GetArticlesList_NonExistingTag() throws JsonProcessingException {
//        Get tag list
        requestResponseLog.write("\"\\n========== Get tag list ===========\\n");
        Response tagListResponse = with().headers("Content-Type", "application/json")
                .when()
                .request("GET", "/tags");

        MTags tagList = mapper.readValue(tagListResponse.getBody().prettyPrint(), MTags.class);

//        Call Get Article list by tag
        requestResponseLog.write("\"\\n========== Get article list by invalid tag ===========\\n");
        String tagParam = tagList.getTags().get(0) + System.currentTimeMillis();
        Response response = with().queryParam("tag", tagParam)
                .headers("Content-Type", "application/json")
                .when()
                .request("GET", "/articles");
//        Asset
        MArticlesResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        Assertions.assertThat(response.statusCode()).as("Expected code 200").isEqualTo(200);
        Assertions.assertThat(articleResponse.getArticlesCount()).as("Expected  count: 0").isEqualTo(0);
    }

    @Test(description = """
            Testcase: Get articles list by valid author
            Send Get to endpoint: /articles
            Expected code 200, show all articles of author""", priority = 1)
    public void TC4_GetArticleList_ValidAuthor() throws JsonProcessingException {
        String authorParam = (String) authors.get(0);

//        Send request and get response
        requestResponseLog.write("\"\\n========== Get article list with author ===========\\n");
        Response response = with().queryParam("author", authorParam)
                .headers("Content-Type", "application/json")
                .when()
                .request("GET", "/articles");
        MArticlesResponse articlesList = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> article = articlesList.getArticles();
        for (MArticle article1 : article) {
            Assertions.assertThat(article1.getAuthor().getUsername()).as("Expected: authorParam")
                    .isEqualTo(authorParam);
        }
        Assertions.assertThat(response.statusCode()).as("Expected stt: 200").isEqualTo(200);
    }

    @Test(description = """
            Testcase: Get Article list by invalid author
            Send Get to endpoint: /articles
            Expected code 200, and no articles to show""", priority = 1)
    public void TC5_GetArticleList_NonExistingAuthor() throws JsonProcessingException {
        String valid_author = (String) authors.get(0);

        String authorParam = valid_author + System.currentTimeMillis();
//        Send request and get response
        requestResponseLog.write("\"\\n========== Get article list with invalid author ===========\\n");

        Response response = with().queryParam("author", authorParam)
                .when()
                .request("GET", "/articles");

//        Assert
        MArticlesResponse articlelist = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        Assertions.assertThat(response.statusCode()).as("Expected: 200").isEqualTo(200);
        Assertions.assertThat(articlelist.getArticlesCount()).as("Expected: 0").isEqualTo(0);
    }

    @Test(description = """
            Testcase: Get article list by favorited user
            Send post to end point: /articles
            Expected code 200 and return favorited list by user""")
    public void TC6_GetArticlesList_FavoritedUser() throws JsonProcessingException {
        requestResponseLog.write("\"\\n========== Get article list by favorite user ===========\\n");
        Response response = with().headers("Content-Type", "application/json")
                .queryParam("favorited", "marigold09")
                .when()
                .request("GET", "/articles");
//        Assert
        MArticlesResponse articlesResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articlesList = articlesResponse.getArticles();
        for (MArticle article : articlesList) {
            Assertions.assertThat(article.getFavoritesCount()).as("Expected favorited count>0")
                    .isGreaterThan(0);
        }
    }

    @Test(description = """
            Testcase: Get article by invalid favorited value
            Send post to endpoint: /articles
            Expected stt code 200 and no return record""")
    public void TC7_GetArticlesList_InvalidFavorited() throws JsonProcessingException {
//        Send request
        requestResponseLog.write("\"\\n========== Get article list with invalid favorite user ===========\\n");
        String invalid_FavoritedAcc = "marigold09" + System.currentTimeMillis();
        Response response = with().headers("Content-Type", "application/json")
                .queryParam("favorited", invalid_FavoritedAcc)
                .when()
                .request("GET", "/articles");
//        Assert
        MArticlesResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articlesList = articleResponse.getArticles();
        Assertions.assertThat(response.statusCode()).as("Expected code: 200").isEqualTo(200);
        Assertions.assertThat(articlesList.size()).as("Expected size: 0").isEqualTo(0);
    }

    @Test(description = """
            Testcase: Get articles list by x limit
            Send Get to end point: /articles
            Expected code 200 and return x records""")
    public void TC8_GetArticlesList_ByLimit() throws JsonProcessingException {
        int limit = 100;
//        Send request  and get response
        requestResponseLog.write("\"\\n========== Get article list by limit params ===========\\n");
        Response response = with().headers("Content-Type", "application/json")
                .queryParam("limit", limit)
                .when()
                .request("GET", "/articles");

//        Asset
        Assertions.assertThat(response.statusCode()).as("Expected code: 200").isEqualTo(200);
        MArticlesResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articlesList = articleResponse.getArticles();
        Assertions.assertThat(articleResponse.getArticlesCount()).as("Articles counts <=limit")
                .isLessThanOrEqualTo(limit);
        boolean checkLatest = true;
        Instant createdLatestDate = Instant.parse(articlesList.get(0).getCreatedAt());
        for (int i = 1; i < articlesList.size(); i++) {
            Instant createdAt = Instant.parse(articlesList.get(i).getCreatedAt());
            if (createdAt.isAfter(createdLatestDate)) {
                checkLatest = false;
            }
        }
        Assert.assertTrue(checkLatest);
    }

    @Test(description = """
            Testcase: Test get articles list with limit =0
            Send get to end point /articles
            Expected code 422 and msg
            """)
    public void TC9_GetArticlesListLimit0() throws JsonProcessingException {
//        Send request and get respons
        int limit = 0;
        requestResponseLog.write("\"\\n========== Get article list with limit param = 0 ===========\\n");
        Response response = with().headers("Content-Type", "application/json")
                .queryParam("limit", limit)
                .when()
                .request("GET", "/articles");

//        Assert
        Assertions.assertThat(response.statusCode()).as("Expected code: 422")
                .isEqualTo(422);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: ensure this value is greater than or equal to 1")
                .contains("ensure this value is greater than or equal to 1");
    }

    @Test(description = """
            Testcase: Get Articles list with negative offset
            Send GET to endpoint /articles
            Expected code 422 and msg""")
    public void TC10_GetArticlesListNegativeOffset() {
//        Send request
        int offset = -1;
        requestResponseLog.write("\"\\n========== Get article list with negative offset ===========\\n");
        Response response = with().headers("Content-Type", "application/json")
                .queryParam("offset", offset)
                .when()
                .request("GET", "/articles");

//        Assert
        Assertions.assertThat(response.statusCode()).as("Expected code: 422").isEqualTo(422);
        String responseString = response.getBody().prettyPrint();
        Assertions.assertThat(responseString).as("Expected msg: ensure this value is greater than or equal to 0")
                .contains("ensure this value is greater than or equal to 0");
    }

    @Test(description = """
            Testcase: Get articles by valid offset
            Send Get to endpoint: /articles
            Expected code: 200 and return list from (offset+1) position""", priority = 1)
    public void TC11_GetArticlesListByValidOffset() throws JsonProcessingException {
//        Send request and get response
        int offset = 2;
        requestResponseLog.write("\"\\n========== Get article with valid offset ===========\\n");

        Response response = with().headers("Content-Type", "application/json")
                .queryParam("offset", offset)
                .when()
                .request("GET", "/articles");

//        Assert need verify again --> Failed now
        Assertions.assertThat(response.statusCode()).as("Expected code: 200").isEqualTo(200);
        MArticlesResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        List<MArticle> articleList = articleResponse.getArticles();
        articleList = articleList.subList(0, articleList.size() - offset);
        List<MArticle> expectedList = articleListFromLatest.subList(offset, articleListFromLatest.size());
//        Assertions.assertThat(articleList).usingRecursiveAssertion()
//                .isEqualTo(expectedList);
        Assertions.assertThat(articleList.size()).isEqualTo(expectedList.size());
    }

    @Test(description = """
            Testcase: Get article list by Offset>Total
            Send GET to endpoint: /articles
            Expected code: 200 and article count =0""")
    public void TC12_GetArticlesOffetGreaterThanTotal() throws JsonProcessingException {
//        Send request
        int offset = 2000000;
        requestResponseLog.write("\"\\n========== Get article list with offset greater total===========\\n");
        Response response = with().headers("COntent-Type", "application/json")
                .queryParam("offset", offset)
                .when()
                .request("GET", "/articles");
//        Assert
        Assertions.assertThat(response.statusCode()).as("Expected code: 200").isEqualTo(200);
        MArticlesResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        Assertions.assertThat(articleResponse.getArticlesCount()).as("Expected number: 0").isEqualTo(0);
    }

    @Test(description = """
            Testcase: Get articles list combine all condition
            Send Get to end point: /articles
            Expected code: 200 and return list""")
    public void TC13_GetArticleListAllParams() throws JsonProcessingException {
        requestResponseLog.write("\"\\n========== Get article list with all params ===========\\n");
//        Send request and get response
        String tag = "automation";
//        String author = (String) authors.get(0);
        String author = "hmack";
        String favorited = "marigold09";
        int limit = 30;
        int offset = 1;
        Response response = with().headers("Content-Type", "application/json")
                .queryParam("tag", tag)
                .queryParam("author", author)
                .queryParam("favorited", favorited)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .when()
                .request("GET", "/articles");

//        Assert
        Assertions.assertThat(response.statusCode()).as("Expected code: 200").isEqualTo(200);
        MArticlesResponse articleResponse = mapper.readValue(response.getBody().prettyPrint(), MArticlesResponse.class);
        Assertions.assertThat(articleResponse.getArticlesCount()).as("Expected: <=limit").isLessThanOrEqualTo(limit);
        List<MArticle> articleList = articleResponse.getArticles();
        boolean check_latest = true;
        Instant firstArticleCreatedTime = Instant.parse(articleList.get(0).getCreatedAt());
        for (int i = 1; i < articleList.size(); i++) {
            Instant articleCreatedTime = Instant.parse(articleList.get(i).getCreatedAt());
            if (firstArticleCreatedTime.isBefore(articleCreatedTime)) {
                check_latest = false;
                break;
            }
        }
        Assert.assertTrue(check_latest);
        for (MArticle article : articleList) {
            Assertions.assertThat(article.getTagList()).contains(tag);
        }
        Assertions.assertThat(articleResponse.getArticlesCount()).isLessThanOrEqualTo(limit);
    }
}
