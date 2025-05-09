package testsuite;


import org.testng.Assert;
import org.testng.annotations.*;

public class LoginTestSuite {
    private String token;
    @BeforeClass
    public void beforeClass() {
        // Call login request to get token
        this.token = "abc";
    }

    @DataProvider (name = "token")
    public Object[][] getToken() {
        return new Object [][] {new String[]{token}};
    }

    @AfterClass
    public void afterClass() {

    }

    @BeforeSuite
    public void beforeSuite() {
        // Connect google drive
        // find suite same
        // Copy to a new file

    }

    @AfterSuite
    public void afterSuite() {
        // Mark pass false on file

    }

    @BeforeTest
    public void beforeTest() {

    }

    @AfterTest
    public void afterTest() {

    }

    @BeforeGroups(groups = {"locked_user"})
    public void beforeGroupLockedUser() {
        System.out.println("BeforeGroup------------------Setup a locked user");
    }

    @AfterGroups(groups = {"locked_user"})
    public void afterGroupLockedUser() {
        System.out.println("AfterGroup------------------Tear down a locked user");
    }

    @BeforeGroups(groups = {"valid_user"})
    public void beforeGroupValidUser() {
        System.out.println("BeforeGroup------------------Setup a valid user");
    }

    @AfterGroups(groups = {"valid_user"})
    public void afterGroupValidUser() {
        System.out.println("AfterGroup------------------Tear down a valid user");
    }

    @BeforeMethod
    public void beforeMethod() {
        System.out.println("BeforeMethod------------------Setup env: Start web browser");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("AfterMethod------------------Tear down env: Stop web browser");
    }

    @Test(description = """
            User can login to the system using valid credential
            """, groups = "valid_user")
    public void Test_Case_Login_Success() {
        System.out.println("This is test case login success");
        // TODO: Open web url
        // Enter valid user name, password
        // Click login button
        // Verify homepage and username after login success
    }

    @Test(description = """
            User will login fail when enter wrong user name or password
            """, groups = "valid_user")
    public void Test_case_login_failed_wrong_username_password() {
        System.out.println("This is test case login failed");
        Assert.assertTrue(false);
        System.out.println("This won't print out because of the test case is failed");
    }

    @Test(description = """
            Locked user cannot login to the system. He will see message 'User is locked'
            """, groups = "locked_user")
    public void Test_case_login_failed_with_locked_user() {
        System.out.println("Test case to test locked user");
    }
}
