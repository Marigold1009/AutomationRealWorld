package testsuite.api.profiles;

import org.testng.annotations.Test;

public class TestFollowUser {
    private final String ENDPOINT = "https://realworld-api.ap.ngrok.io/api/profiles/";

    @Test(description = """
            Testcase Follow user successfully
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC1_FollowUserSuccess(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}/follow";
//        Set token into header --> Call API --> Verify data
    }

    @Test(description = """
            Testcase Follow non existing user unsuccessfully
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC2_FollowUserFail_NonExistingUserName(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}follow";
//        Set token into header --> Call API --> Verify data
    }

    @Test(description = """
            Testcase Follow Fail, no input userName
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC3_FollowUserFail_EmptyUserName(String userName) {
        String endpoint = ENDPOINT + "/follow";
//        Set token into header --> Call API --> Verify data
    }

    @Test(description = """
            Testcase Follow user Fail with empty token
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC4_FollowUserFail_EmptyToken(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}follow";
//           Call API --> Verify data
    }

    @Test(description = """
            Testcase Follow Fail with invalid token
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC5_FollowUserFail_InvalidToken(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}/follow";
//        Set token into header --> Call API --> Verify data
    }

    @Test(description = """
            Testcase Follow user Fail with expired token
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC6_FollowUserFail_ExpiredToken(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}/follow";
//        Set token into header --> Call API --> Verify data
    }

    @Test(description = """
            Testcase Follow user fail with wrong format token 
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC7_FollowUserFail_WrongFormatToken(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}/follow";
//        Set token into header --> Call API --> Verify data
    }

    @Test(description = """
            Testcase Follow user Fail Non Existing Token
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC8_FollowUserFail_NonExistingToken(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}/follow";
//        Set token into header --> Call API --> Verify data
    }

    @Test(description = """
            Testcase Follow user Fail Followed user
            Send post to endpoint api/profiles/{userName}/follow
            Expect code and data response""")
    public void TC9_FollowUserFail_FollowedUser(String userName) {
        String endpoint = ENDPOINT + "{" + userName + "}/follow";
//        Set token into header --> Call API --> Verify data
    }
}
