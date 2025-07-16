package testsuite.api.fullflow;

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
import org.testng.annotations.Test;
import testsuite.config.ApiDataFactory;
import testsuite.model.article.*;
import testsuite.model.comments.MComment;
import testsuite.model.comments.MCommentDetail;
import testsuite.model.comments.MComments;
import testsuite.model.profiles.MProfile;
import testsuite.model.profiles.MProfilesDetail;
import testsuite.model.user.MUser;
import testsuite.model.user.MUserDetail;
import testsuite.utils.ApiLogFactory;

import java.io.PrintStream;
import java.io.StringWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.with;

public class FullFlow {
    private final ObjectMapper mapper = new ObjectMapper();
    private StringWriter requestResponseLog;
    private PrintStream requestResponseCaptureStream;

    @BeforeClass
    public void BeforeClass(){
        RestAssured.baseURI = ApiDataFactory.API_URL;
//        requestResponseLog = ApiLogFactory.getWriter();
//        requestResponseCaptureStream = ApiLogFactory.getStream();
                RestAssured.filters(new RequestLoggingFilter()
                ,new ResponseLoggingFilter());
    }

    @Test(description = """
            Get article list by Guest
            Send Get to end point: /articles
            Expected code 200 and received list""")
    public void TC1_GuestFlow() throws JsonProcessingException {
//        requestResponseLog.write("This is guest flow");
//        requestResponseLog.write("Guest get article list");
        Response response = with().when().request("GET","/articles");
        MArticlesResponse articlesResponse = mapper.readValue(response.getBody().prettyPrint(),
                MArticlesResponse.class);
        List<MArticle> articleList = articlesResponse.getArticles();
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        Assertions.assertThat(articlesResponse.getArticlesCount()).isLessThanOrEqualTo(20);
        Instant latestDate = Instant.parse(articleList.get(0).getCreatedAt());
        boolean checkLatest = true;
        List<MAuthor> authors = new ArrayList<>();
        MAuthor author;
        for(MArticle article: articleList){
            Instant createdAt = Instant.parse(article.getCreatedAt());
            author = article.getAuthor();
            authors.add(author);
            if (latestDate.isBefore(createdAt)){
                checkLatest = false;
                break;
            }
        }
        Assert.assertTrue(checkLatest);

//        Get view Detail article
        String slugDetail = articleList.get(0).getSlug();
        Response response1 = with().when().request("GET","articles/"+slugDetail);
        Assertions.assertThat(response1.statusCode()).isEqualTo(200);
        MArticleResponse articleResponse = mapper.readValue(response1.getBody().prettyPrint(),
                MArticleResponse.class);
        MArticle article =  articleResponse.getArticle();
        Assertions.assertThat(article.getSlug()).contains(slugDetail);

//        View detail author
        String authorTest = authors.get(0).getUsername();
        Response response2 = with().header("Content-Type","application/json").when().request("GET","profiles/"+authorTest);
        MProfile profile = mapper.readValue(response2.getBody().prettyPrint(),
                MProfile.class);
        MProfilesDetail profilesDetail = profile.getProfilesDetail();
        Assertions.assertThat(response2.statusCode()).isEqualTo(200);
        Assertions.assertThat(profilesDetail.getUsername()).contains(authorTest);
        Assertions.assertThat(profilesDetail.isFollowing()).isEqualTo(false);
    }

    @Test(description = "Uer Flow")
    public void  TC2_UserFlow() throws JsonProcessingException {
//        Sign  up acc
//        requestResponseLog.write("This is user flow");
//        requestResponseLog.write("Create an account");
        MUserDetail userDetail = new MUserDetail();
        String email = "userflowEmail"+System.currentTimeMillis()+"@gmail.com";
        String username = "userflowEmail"+System.currentTimeMillis();
        userDetail.setEmail(email);
        userDetail.setPassword("12345");
        userDetail.setUsername(username);
        MUser user = new MUser();
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = with().header("Content-Type","application/json").when().body(node).request("POST","/users");
        Assertions.assertThat(response.statusCode()).isEqualTo(201);
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        userDetail = user.getUser();
        Assertions.assertThat(userDetail.getUsername()).contains(username);
        Assertions.assertThat(userDetail.getEmail()).contains(email);
        String userName = userDetail.getUsername();

//        Login by acc
        Response response1 = with().when().header("Content-Type","application/json")
                .body(node).request("POST","/users/login");
        user = mapper.readValue(response1.getBody().prettyPrint(), MUser.class);
        userDetail = user.getUser();
        String token = userDetail.getToken();
        Assertions.assertThat(response1.statusCode()).isEqualTo(200);
        Assertions.assertThat(userDetail.getUsername()).contains(username);
        Assertions.assertThat(userDetail.getEmail()).contains(email);

//        Create an account
//        requestResponseLog.write("Create an article");
        String title = "testCreate" + System.currentTimeMillis();
        String boby = "testCreate_Body";
        String description = "testCreat_Description";
        List<String> tags = Arrays.asList("tag01", "tag02");
        MArticle articleCreate = new MArticle();
        articleCreate.setTitle(title);
        articleCreate.setBody(boby);
        articleCreate.setTagList(tags);
        articleCreate.setDescription(description);
        MArticleCreate articleCreate1 = new MArticleCreate();
        articleCreate1.setArticle(articleCreate);
        JsonNode node1 = mapper.valueToTree(articleCreate1);
        Response response2 = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().body(node1).request("POST","/articles");
        Assertions.assertThat(response2.statusCode()).isEqualTo(201);
        MArticleResponse articleResponse = mapper.readValue(response2.getBody().prettyPrint(),
                MArticleResponse.class);
        MArticle articleActual = articleResponse.getArticle();
        String articleSlug = articleActual.getSlug();
        Assertions.assertThat(articleActual).usingRecursiveComparison()
                .ignoringFields("createdAt","updatedAt",
                        "id","slug","author","favorited","favoritesCount").isEqualTo(articleCreate);

//        User get detail Article
//        requestResponseLog.write("Get Article Detail");
        Response response3 = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("GET","/articles/"+articleSlug);
        Assertions.assertThat(response3.statusCode()).isEqualTo(200);
        MArticleResponse articleResponse1 = mapper.readValue(response3.getBody().prettyPrint(), MArticleResponse.class);
        MArticle articleDetail = articleResponse1.getArticle();
        Assertions.assertThat(articleDetail).usingRecursiveComparison().isEqualTo(articleActual);

//        Update article
//        String titleUpdate = articleDetail.getTitle() + "update";
//        String bodyUpdate = articleDetail.getBody() + "update";
//        String desUpdate = articleDetail.getDescription() + "update";
//        MArticle articleUpdate = articleDetail;
//        articleUpdate.setTitle(titleUpdate);
//        articleUpdate.setDescription(desUpdate);
//        articleUpdate.setBody(bodyUpdate);
//        JsonNode node2 = mapper.readValue(articleUpdate);
//        Response responseUpdate =;

//      Delete article
//        requestResponseLog.write("Delete article");
        Response responseDelete = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("DELETE","/articles/"+articleSlug);
        Assertions.assertThat(responseDelete.statusCode()).isEqualTo(204);

//        Get article list
//        requestResponseLog.write("User get article list");
        Response response4 = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().request("GET","/articles");
        MArticlesResponse articlesResponse = mapper.readValue(response4.getBody().prettyPrint(),
                MArticlesResponse.class);
        List<MArticle> articleList = articlesResponse.getArticles();
        Assertions.assertThat(response4.statusCode()).isEqualTo(200);
        Assertions.assertThat(articlesResponse.getArticlesCount()).isLessThanOrEqualTo(20);
        Instant latestDate = Instant.parse(articleList.get(0).getCreatedAt());
        boolean checkLatest = true;
        List<MAuthor> authors = new ArrayList<>();
        MAuthor author;
        for(MArticle article: articleList){
            Instant createdAt = Instant.parse(article.getCreatedAt());
            author = article.getAuthor();
            authors.add(author);
            if (latestDate.isBefore(createdAt)){
                checkLatest = false;
                break;
            }
        }
        Assert.assertTrue(checkLatest);

//      View detail 1 author
        String authorTest = "";
        for (MAuthor author01: authors){
            if(!author01.getUsername().equals(userName)){
                authorTest = author01.getUsername();
                break;
            }
        }
        Response responseViewAuthor = with().headers("Content-Type","application/json","Authorization","Token "+token).
                when().request("GET","profiles/"+authorTest);
        MProfile profile = mapper.readValue(responseViewAuthor.getBody().prettyPrint(),
                MProfile.class);
        MProfilesDetail profilesDetail = profile.getProfilesDetail();
        Assertions.assertThat(responseViewAuthor.statusCode()).isEqualTo(200);
        Assertions.assertThat(profilesDetail.getUsername()).contains(authorTest);
        Assertions.assertThat(profilesDetail.isFollowing()).isEqualTo(false);

//      Follow an author
        Response responseFollowUser = with().headers("Content-Type","application/json","Authorization","Token "+token).
                when().request("POST","profiles/"+authorTest+"/follow");
        Assertions.assertThat(responseFollowUser.statusCode()).isEqualTo(200);

//        User Get Feed
        Response responseGetFeed = with().headers("Content-Type","application/json","Authorization","Token "+token).
                when().request("GET","articles/feed");
        MArticlesResponse mArticlesResponseFeed = mapper.readValue(responseGetFeed.getBody().prettyPrint(),
                MArticlesResponse.class);
        Assertions.assertThat(responseGetFeed.statusCode()).isEqualTo(200);
        Assertions.assertThat(mArticlesResponseFeed.getArticlesCount()).isLessThanOrEqualTo(20);
        List<MArticle> articleFeedList = mArticlesResponseFeed.getArticles();
        boolean checkAuthor = true;
        for(MArticle article: articleFeedList){
            if(!article.getAuthor().getUsername().equals(authorTest)){
                checkAuthor = false;
                break;
            }
        }
        Assert.assertTrue(checkAuthor);

//        Unfollow an author
        Response responseUnFollowUser = with().headers("Content-Type","application/json","Authorization","Token "+token).
                when().request("DELETE","profiles/"+authorTest+"/follow");
        Assertions.assertThat(responseFollowUser.statusCode()).isEqualTo(200);

//        Favorite an article
        String articleSlugFavorite = articleList.get(0).getSlug();
        Response responseMarkFavorite = with().headers("Content-Type","application/json","Authorization","Token "+token).
                when().request("POST","articles/"+articleSlugFavorite+"/favorite");
        Assertions.assertThat(responseMarkFavorite.statusCode()).isEqualTo(200);
        MArticleResponse articlesFavorite =mapper.readValue(responseMarkFavorite.getBody().prettyPrint(),
                MArticleResponse.class);
        MArticle articlesFavoriteDetail = articlesFavorite.getArticle();
        Assertions.assertThat(articlesFavoriteDetail.isFavorited()).isEqualTo(true);

//        Unfavorite an article
        String articleSlugUnFavorite = articleSlugFavorite;
        Response responseUnMarkFavorite = with().headers("Content-Type","application/json","Authorization","Token "+token).
                when().request("DELETE","articles/"+articleSlugUnFavorite+"/favorite");
        Assertions.assertThat(responseMarkFavorite.statusCode()).isEqualTo(200);
        MArticleResponse articlesUnFavorite =mapper.readValue(responseUnMarkFavorite.getBody().prettyPrint(),
                MArticleResponse.class);
        MArticle articlesUnFavoriteDetail = articlesUnFavorite.getArticle();
        Assertions.assertThat(articlesUnFavoriteDetail.isFavorited()).isEqualTo(false);

//        Create a comment
        String slugTestComment = "artist-just-majority-field";
        MCommentDetail commentDetail = new MCommentDetail();
        String bodyComment = "This is full flow, user create a comment "+System.currentTimeMillis();
        commentDetail.setBody(bodyComment);
        MComment comment = new MComment();
        comment.setComment(commentDetail);
        JsonNode node2 = mapper.valueToTree(comment);
        Response responseTestComment = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().body(node2).request("POST","/articles/"+slugTestComment+"/comments");
        Assertions.assertThat(responseTestComment.statusCode()).isEqualTo(201);

//      Get all comment
        Response responseGetComments = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("GET","/articles/"+slugTestComment+"/comments");
        MComments comments = mapper.readValue(responseGetComments.getBody().prettyPrint(),MComments.class);
        List<MCommentDetail> commentList = comments.getComments();
        Assertions.assertThat(responseGetComments.statusCode()).isEqualTo(200);
        boolean checkcomment = true;
        int validId = 0;
        for (MCommentDetail comment01: commentList){
            checkcomment = true;
            if(comment01.getBody().equals(bodyComment)) {
                validId = comment01.getId();
                break;
            }else {
                checkcomment = false;
            }
        }
        Assert.assertTrue(checkcomment);

//        Delete a comment
        Response responseDeleteComment = with().headers("Content-Type","application/json","Authorization", "Token "+token)
                .when().request("DELETE","/articles/"+slugTestComment+"/comments/"+validId);
        Assertions.assertThat(responseDeleteComment.statusCode()).isEqualTo(204);
    }

}
