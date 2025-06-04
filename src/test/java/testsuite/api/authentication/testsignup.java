package testsuite.api.authentication;

import org.testng.annotations.Test;

public class testsignup {
    private final String ENDPOINT = "https://realworld-api.ap.ngrok.io/api/articles/feeds";

    @Test(description = """
            Testcase: Sign up acc successfully
            Send post api to endpoint: api/users
            Expect: code 200, return acc info and token""")
    public void TC1_SignupSuccess(String userName,String email, String password){
        String endpoint = ENDPOINT;
//        Set body and call API --> Verify response
    }

    @Test(description = """
            Testcase: Sign up fail with empty email
            Send post to endpoint: api/users
            Expect: code and msg""")
    public void TC2_SignupFailEmptyEmail(String userName, String password){
        String endpoint = ENDPOINT;
//        Set body and call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Sign up fail with empty password
            Send post to endpoint: api/users
            Expect: code and msg""")
    public void TC3_SignupFailEmptyPassword(String userName, String email){
        String endpoint = ENDPOINT;
//        Set body and call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Sign up fail with empty userName
            Send post to endpoint: api/users
            Expect: code and msg""")
    public void TC4_SignupFailEmptyUserName(String email, String password){
        String endpoint = ENDPOINT;
//        Set body and call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Sign up fail with empty all
            Send post to endpoint: api/users
            Expect: code and msg""")
    public void TC5_SignupFailEmptyAll(){
        String endpoint = ENDPOINT;
//        Set body and call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Sign up fail with existing email
            Send post to endpoint: api/users
            Expect: code and msg""")
    public void TC6_SignupFailExistingEmail(String userName, String email, String password){
        String endpoint = ENDPOINT;
//        Set body and call API --> Verify code and msg
    }

    @Test(description = """
            Testcase: Sign up fail with existing userName
            Send post to endpoint: api/users
            Expect: code and msg""")
    public void TC7_SignupFailExistingUserName(String userName, String email, String password){
        String endpoint = ENDPOINT;
//        Set body and call API --> Verify code and msg
    }
    @Test(description = """
            Testcase: Sign up fail with invalid email
            Send post to endpoint: api/users
            Expect: code and msg""")
    public void TC8_SignupFailInvalidyEmail(String userName, String email, String password){
        String endpoint = ENDPOINT;
//        Set body and call API --> Verify code and msg
    }
}
