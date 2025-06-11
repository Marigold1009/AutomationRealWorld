package testsuite.api.users;

import org.testng.annotations.Test;

public class TestUpdateCurrentUser {
    private final String endpoint = "https://realworld-api.ap.ngrok.io/api/user";

    @Test(description = """
            Testcase update url successfully
            Send put to endpoint api/user
            Expect code and response data""")
    public void TC1_UpdateURLSuccess(String url) {
//        Set token into header
//        Call API and verify response
    }

    @Test(description = """
            Testcase update userName successfully""")
    public void TC2_UpdateUserNameSuccess(String userName) {
//        Set token into header
//        Call API and verify response
    }

    @Test(description = """
            Testcase update Description successfully""")
    public void TC3_UpdateDescriptionSuccess(String description) {
//        Set token into header
//        Call API and verify response
    }

    @Test(description = """
            Testcase update email successfully""")
    public void TC4_UpdateEmailSuccess(String email) {
//        Set token into header --> Call  API and verify
    }

    @Test(description = """
            Testcase update password successfully""")
    public void TC5_UpdatePasswordSuccess(String password) {
//        Set token --> Call API --> Verify response
    }

    @Test(description = """
            Testcase update all fields""")
    public void TC6_UpdateAllFieldsSuccess(String url, String userName, String email, String password) {
//        Set token into header --> Call API --> Verify response
    }

    @Test(description = """
            Testcase update fail with empty userName""")
    public void TC7_UpdateFailWithEmptyUserName() {
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase update fail with existing userName""")
    public void TC7_UpdateFailWithExistingUserName(String userName) {
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase update fail with empty email""")
    public void TC8_UpdateFailWithEmptyEmail() {
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase update fail with existing userName""")
    public void TC9_UpdateFailWithExistingEmail(String userName) {
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase update fail with empty password""")
    public void TC10_UpdateFailWithEmptyPassword(String password) {
//        Set token --> Call API --> Verify
    }

    @Test(description = """
            Testcase update fail_missing token""")
    public void TC11_UpdateFailMissingToken() {
//        Call API and verify response
    }

    @Test(description = """
            Testcase update fail_invalid token""")
    public void TC12_UpdateFailWithInvalidToken() {
//        Set token to header and call API --> verify response
    }

    @Test(description = """
            Testcase update fail_expired token""")
    public void TC13_UpdateFailWithExpiredToken() {
//        Set token to header and call API --> verify response
    }

    @Test(description = """
            Testcase update fail_wrong format token""")
    public void TC14_UpdateFailWithWrongFormatToken() {
//        Set token to header and call API --> verify response
    }

    @Test(description = """
            Testcase update fail_expired token""")
    public void TC15_UpdateFailWithNoExistingToken() {
//        Set token to header and call API --> verify response
    }
}
