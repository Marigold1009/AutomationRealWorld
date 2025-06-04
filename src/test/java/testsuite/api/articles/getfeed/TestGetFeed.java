package testsuite.api.articles.getfeed;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestGetFeed {
    private final String ENDPOINT = "https://realworld-api.ap.ngrok.io/api/articles/feeds";

    String token = "";

    @BeforeClass
    public void beforeClass(){
//        login --> set token
    }
    @Test(description = """
            Test case default params
            Send get to endpoint: api/articles/feeds
            Expect total articles equals or less than 20, start at latest article""")
    public void TC1_GetFeedsWithourParam(){
        String endPoint = ENDPOINT;
//      Set token into header --> Call API
//      Verify total item <=20
//      Verify first item = latest item
    }

    @Test(description = """
            Test case with valid param
            Send get to endpoint: api/articles/feeds
            Expect: total articles equal <=limit, end first item at offset article""")
    public void TC2_GetFeedsWithValidParam(String limit, String offset){
        String endPoint = ENDPOINT + String.format("?limit=%&offset=%s",limit,offset);
//        Set token into header --> Call API
//        Verify total item<=limit
//        Verify first items = item at offset position
    }

    @Test(description = """
            Test case with valid limit
            Send get to endpoint: api/articles/feeds
            Expect: total items <= limit,first item = latest item""")
    public void TC3_GetFeedsWithValidLimit(String limit){
        String endPoint = ENDPOINT + String.format("?limit=%s",limit);
//        Set token into header --> Call API
//        Verify total item <=limit, and Start at latest item
    }

    @Test(description = """
            Testcase with valid  offest
            Send get to endpoint:api/articles/feeds
            Expect: total item<=20, start at offset""")
    public void TC4_GetFeedWithValidOffset(String offset){
        String endPoint = ENDPOINT + String.format("?offset=%s",offset);
//        Set token into header --> Call API
//        Verify total item <=20, start at offset
    }

    @Test(description = """
            Testcase with invalid params
            Send get to endPoint: api/articles/feed
            Expect: Show msg""")
    public void TC5_GetFeedWithInvalidParam(String limit, String offset){
        String endPoint = ENDPOINT + String.format("?limit=%s&offset=%s",limit,offset);
//        Set token into header --> Call API
//        Verify msg
    }

    @Test(description = """
            Testcase with invalid limit, valid offset
            Send get to endPoint: api/articles/feed
            Expect: Show msg""")
    public void TC6_GetFeedWithInvalidLimitValidOffset(String limit, String offset){
        String endPoint = ENDPOINT + String.format("?limit=%s&offset=%s",limit,offset);
//        Set token into header-->Call API and verify code and msg
    }

    @Test(description = """
            Testcase with invalid offset, valid limit
            Send get to endPoint: api/articles/feed
            Expect: Show msg""")
    public void TC7_GetFeedWithValidLimitInvalidOffset(String limit, String offset){
        String endPoint = ENDPOINT + String.format("?limit=%s&offset=%s",limit,offset);
//        Set token into header-->Call API and verify code and msg
    }

    @Test(description = """
            Testcase with invalid limit
            Send get to endPoint: api/articles/feed
            Expect: Show msg""")
    public void TC8_GetFeedWithInvalidPLimit(String limit){
        String endPoint = ENDPOINT + String.format("?limit=%s",limit);
//        Set token into header-->Call API and verify code and msg
    }

    @Test(description = """
            Testcase with invalid offset
            Send get to endPoint: api/articles/feed
            Expect: Show msg""")
    public void TC9_GetFeedWithInvalidOffset(String offset){
        String endPoint = ENDPOINT + String.format("offset=%s",offset);
//        Set token into header-->Call API and verify code and msg
    }

    @Test(description = """
            Testcase: offset>number of total article
            Send get to endpoint: api/articles/feeds
            Expect: Show msg""")
    public void TC10_GetFeedsOffsetLargerTotal(String offset){
        String endpoint = ENDPOINT + String.format("?offset=%s",offset);
//        Set token into header-->Call API and verify code and msg
    }

    @Test(description = """
            Testcase: No set token into header
            Send get to endpoint: api/articles/feeds
            Expect: Show msg""")
    public void TC11_GetFeedsWithoutAuthorize(){
        String endpoint = ENDPOINT;
//        Call API and verify msg
    }

    @Test(description = """
            Testcase: Input invalid token
            Send get to endpoint: api/articles/feeds
            Expect: Show msg""")
    public void TC12_GetFeedsWithInvalidToken(String token){
        String endpoint = ENDPOINT;
//        Set token into header and call API
//        Verify msg
    }

    @Test(description = """
            Testcase: Input expired token
            Send get to endpoint: api/articles/feeds
            Expect: Show msg""")
    public void TC12_GetFeedsWithExpiredToken(){
        String endpoint = ENDPOINT;
//        Set token into header and call API
//        Verify code and msg
    }



}
