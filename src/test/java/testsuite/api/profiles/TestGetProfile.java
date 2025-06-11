package testsuite.api.profiles;

import org.testng.annotations.Test;

public class TestGetProfile {
    private String ENDPOINT = "https://realworld-api.ap.ngrok.io/api/profiles/";

    @Test(description = """
            Testcase Get user profile by userName successfully
            Send get to endpoint api/profiles/{userName}
            Expected code vaf response""")
    public void TC1_GetUserProfileSuccess(String userName){
        String endpoint = ENDPOINT + "{" + userName + "}";
//        Call API and verify response
    }

    @Test(description = """
            Testcase Get no existing user
            Send get to endpoint api/profiles/{userName}
            Expect code and msg""")
    public void TC2_GetNoExistingUser(String userName){
        String endpoint = ENDPOINT + "{" +userName + "}";
//        Call API and verify response
    }

    @Test(description = """
            Testcase Get with Empty user
            Send get to endpoint api/profiles
            Expect code and msg""")
    public void TC3_GetNoExistingUser(){
        String endpoint = ENDPOINT;
//        Call API and verify response
    }

    @Test(description = """
            Testcase Get profile success with valid userName and token
            Send get to endpoint api/profiles
            Expect response""")
    public void TC4_UserGetProfileSuccess(String token, String userName){
        String endpoint = ENDPOINT + "{" + userName + "}";
//        Set token into header --> call API and verify data
    }

    @Test(description = """
            Testcase Get Profile fail with invalid token""")
    public void TC5_UserGetProfileFailInvalidToken(String userName){
        String endpoint = ENDPOINT + "{" + userName + "}";
//        Set token into header --> call API and verify data
    }

    @Test(description = """
            Testcase Get Profile fail with expired token""")
    public void TC6_UserGetProfileFailExpiredToken(String userName){
        String endpoint = ENDPOINT + "{" + userName + "}";
//        Set token into header --> call API and verify data
    }

    @Test(description = """
            Testcase Get Profile fail with wrong format token""")
    public void TC7_UserGetProfileFailWrongFormatToken(String userName){
        String endpoint = ENDPOINT + "{" + userName + "}";
//        Set token into header --> call API and verify data
    }

    @Test(description = """
            Testcase Get Profile fail with no existing token""")
    public void TC8_UserGetProfileFailNoExistingToken(String userName){
        String endpoint = ENDPOINT + "{" + userName + "}";
//        Set token into header --> call API and verify data
    }
}
