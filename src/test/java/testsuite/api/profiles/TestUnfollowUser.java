package testsuite.api.profiles;

import org.testng.annotations.Test;

public class TestUnfollowUser {
    private final String ENDPOINT = "https://realworld-api.ap.ngrok.io/api/profile/";

    @Test(description = """
            Testcase Unfollow user successfully
            Send delete to endpoint /api/profiles/{userName}/follow
            Expect verify code and response""")
    public void TC1_UnfollowSuccess(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}/follow";
//        Set token into header
//        Verify code and resposne
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with empty token
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC2_UnfollowFailWithEmptyToken(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}/follow";
//        Call API --> Verify
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with invalid token
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC3_UnfollowFailWithInvalidToken(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}/follow";
//        Set token into header --> Call API --> Verify
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with wrong format token
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC4_UnfollowFailWithWrongFormatToken(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}/follow";
//       Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with non existing token
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC5_UnfollowFailWithNonExistingToken(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}/follow";
//        Set token into header -->Call API --> Verify
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with empty token
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC6_UnfollowFailWithExpiredToken(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}/follow";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with non existing user
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC7_UnfollowFailWithNonExistingUser(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}/follow";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with empty userName
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC8_UnfollowFailWithEmptyUser() {
        String endpoint = ENDPOINT + "{" + "}/follow";
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase Unfollow user unsuccessfully with unfollow User
            Send delete to endpoint /api/profile/{userName}/follow
            Expect verify code and msg""")
    public void TC9_UnfollowFailWithUnfollowUser(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}/follow";
//        Set token --> Call API --> Verify
    }
}
