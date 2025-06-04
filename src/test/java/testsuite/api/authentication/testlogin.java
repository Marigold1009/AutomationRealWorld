package testsuite.api.authentication;

import org.testng.annotations.Test;

public class testlogin {
    private final String ENDPOINT = "https://realworld-api.ap.ngrok.io/api/articles/feeds";

    @Test(description = """
            Testcase: Login success with credetial
            Send post to endpoint: api/user/login
            Expect: Return token""")
    public void TC1_Loginsuccess(String email, String password){
        String endpoint = ENDPOINT;
//        Set body
//        Call API and verify response and code
    }

    @Test(description = """
            Testcase: Login fail with empty email""")
    public void TC2_LoginFailWithEmptyEmail(String password){
        String endpoint = ENDPOINT;
//        Set body --> Call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Login fail with empty password""")
    public void TC3_LoginFailWithEmptyPassword(String email){
        String endpoint = ENDPOINT;
//        Set body --> Call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Login fail with empty require field""")
    public void TC4_LoginFailWithEmptyAll(){
        String endpoint = ENDPOINT;
//        Call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Login fail with wrong email""")
    public void TC5_LoginFailWithWrongEmail(String email, String password){
        String endpoint = ENDPOINT;
//        Set body --> Call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Login fail with wrong password""")
    public void TC6_LoginFailWithWrongEmail(String email, String password){
        String endpoint = ENDPOINT;
//        Set body --> Call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Login fail with wrond email and password""")
    public void TC7_LoginFailWithWrongAll(String email, String password){
        String endpoint = ENDPOINT;
//        Set body --> Call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Login fail with no existing email""")
    public void TC8_LoginFailWithNonExistingEmail(String email, String password){
        String endpoint = ENDPOINT;
//        Set body --> Call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Login fail with locked account""")
    public void TC9_LoginFailWithLockedAccount(String password){
        String endpoint = ENDPOINT;
//        Set body --> Call API --> Verify code and msg
    }
}
