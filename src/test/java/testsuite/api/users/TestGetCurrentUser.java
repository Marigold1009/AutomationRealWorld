package testsuite.api.users;

import org.testng.annotations.Test;

public class TestGetCurrentUser {
    private final String ENDPOINT = "https://realworld-api.ap.ngrok.io/api/user";

    @Test(description = """
            Testcase Test get current user with valid token
            Send get to endpoint: api/user
            Expect """)
    public void TC1_GetCurrentUserSuccess(String token){
        String endpoint =  ENDPOINT;
//        Set token into header
//        Call API and verify response
    }

    @Test(description = """
            Testcase get current user unsuccessfully without token""")
    public void TC2_GetUserFailWithoutToken(){
        String endpoint = ENDPOINT;
//        Call API and verify response
    }

    @Test(description = """
            Testcase get user fail with invalid token
            Send get to endpoint: api/user
            Expect code and msg""")
    public void TC3_GetUserFailWithInvalidToken(String token){
        String endpoint = ENDPOINT;
//        Set token into headser
//        Call API and verify response
    }

    @Test(description = """
            Testcase Get user fail with expired token
            Send get to endpoint: api/user
            Expect code and msg
            """)
    public void TC4_GetUserFailWithExpiredToken(String token){
        String endpoint = ENDPOINT;
//        Set token into header
//        Call API anf verify response
    }

    @Test(description = """
            Testcase Get user fail with wrong format""")
    public void TC5_GetUserFailWithWrongFormatToken(String token){
        String endpoint = ENDPOINT;
//        Set token into header --> Call API and verify response
    }

    @Test(description = """
            Testcase Get User Fail with right format but token is not existing""")
    public void TC6_GetUserFailNoExistingToken(String token){
        String eendpoint = ENDPOINT;
//        Set token into header --> Call API and verify response
    }
}
