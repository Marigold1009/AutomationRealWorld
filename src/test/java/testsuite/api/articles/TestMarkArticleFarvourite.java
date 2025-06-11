package testsuite.api.articles;

import org.testng.annotations.Test;

public class TestMarkArticleFarvourite {
    private final String ENDPOINT = "https://realworld-api.ap.ngrok.io/api/articles/";

    @Test(description = """
            Testcase Mark article favourite successfully
            Send post to endpoint api/articles/{slug}/favorite
            Expect code  and response""")
    public void TC1_MarkFarvouriteSuccess(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase Mark article favourite unsuccessfully with empty token
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC2_MarkFarvouriteFailEmptyToken(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Call API --> Verify
    }

    @Test(description = """
            Testcase Mark article favourite unsuccessfully with invalid token
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC3_MarkFarvouriteFailInvalidToken(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase Mark article favourite unsuccessfully with expired token
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC4_MarkFarvouriteFailExpiredToken(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase Mark article favourite unsuccessfully with wrong format token
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC5_MarkFarvouriteFailWrongFormatToken(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase Mark article favourite unsuccessfully with non existing token
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC6_MarkFarvouriteFailNonExistingToken(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase Mark article favourite unsuccessfully with empty slug
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC7_MarkFarvouriteFailInvalidToken() {
        String endpoint = ENDPOINT + "{" + "}/favourite";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase Mark article favourit unsuccessfully with Non existing slug
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC8_MarkFarvouriteFailInvalidToken(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase Mark article favourit unsuccessfully with marked slug
            Send post to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC9_MarkFarvouriteFailMarkedSlug(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Set token --> Call API --> Verify
    }
}
