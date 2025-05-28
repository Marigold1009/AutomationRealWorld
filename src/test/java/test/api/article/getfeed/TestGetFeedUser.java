package test.api.article.getfeed;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestGetFeedUser {
    private final String END_POINT = "https://realworld-api.ap.ngrok.io/api/articles/feed";
    String token = "";
    @BeforeClass
    public void beforeClass(){
        // Login --> set token
    }

    @Test
    public void testName() {
    }

    // Default params (limit =20, offset =0) --> expect return feeds <= 20 and latest feed
    @Test(description =
            """
                    Test case default params:
                    Send get to end point /api/articles/feed
                    Expect total feeds equals or less than 20 items and start at latest feed
                    """)
    public void test_case_without_params() {
        // Set token into header to call the api
    }

    @DataProvider(name = "valid_params")
    public Object[][] valid_params() {
        return new Object[][]{{"10", "10"}};
    }

    @DataProvider(name = "invalid_limit")
    public Object[][] invalid_limit() {
        return new Object[][]{{"0"}, {"-1"}, {"-"}, {"a"}};
    }

    @Test(description =
            """
                    Test case with params:
                    Send get to end point /api/articles/feed?limit={limit}&offset={offset}
                    Expect total feeds equals or less than {limit} items and start at {offset} feed
                    """, dataProvider = "valid_params")
    public void test_case_with_params(String limit, String offset) {
        String endPoint = END_POINT + String.format("?limit=%s&offset=%s", limit, offset);
        System.out.printf(endPoint);
    }

    @Test(description =
            """
                    Test case validate limit param:
                    Send get to end point /api/articles/feed?limit={limit} with invalid limit
                    Expect return error of invalid parameter
                    """, dataProvider = "invalid_limit")
    public void test_case_validate_limit_param(String limit) {
        String endPoint = END_POINT + String.format("?limit=%s", limit);
        System.out.printf(endPoint);
    }


    // Verify functional (verify number of articles)

    // Edge case (offset > total feeds)

    // content type
}
