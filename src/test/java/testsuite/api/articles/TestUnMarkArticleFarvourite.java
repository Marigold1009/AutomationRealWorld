package testsuite.api.articles;

import org.testng.annotations.Test;

public class TestUnMarkArticleFarvourite {
    private final String ENDPOINT = "https://realworld-api.ap.ngrok.io/api/articles/";

    @Test(description = """
            Testcase UnMark article favourite successfully
            Send delete to endpoint api/articles/{slug}/favorite
            Expect code  and response""")
    public void TC1_UnMarkFarvouriteSuccess(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase UnMark article favourite unsuccessfully with empty token
            Send delete to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC2_UnMarkFarvouriteFailEmptyToken(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Call API --> Verify
    }

    @Test(description = """
            Testcase UnMark article favourite unsuccessfully with invalid token
            Send delete to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC3_UnMarkFarvouriteFailInvalidToken(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase Mark article favourite unsuccessfully with expired token
            Send delete to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC4_UnMarkFarvouriteFailExpiredToken(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase UnMark article favourite unsuccessfully with wrong format token
            Send delete to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC5_UnMarkFarvouriteFailWrongFormatToken(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase UnMark article favourite unsuccessfully with non existing token
            Send delete to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC6_UnMarkFarvouriteFailNonExistingToken(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase UnMark article favourite unsuccessfully with empty slug
            Send delete to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC7_UnMarkFarvouriteFailInvalidToken() {
        String endpoint = ENDPOINT + "{" + "}/favourite";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase UnMark article favourite unsuccessfully with Non existing slug
            Send delete to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC8_UnMarkFarvouriteFailInvalidToken(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase UnMark article favourite unsuccessfully with unfavourite slug
            Send delete to endpoint api/articles/{slug}/favourite
            Expect code and msg""")
    public void TC9_MarkFarvouriteFailUnMarkedSlug(String slug) {
        String endpoint = ENDPOINT + "{" + slug + "}/favourite";
//        Set token --> Call API --> Verify
    }
}
