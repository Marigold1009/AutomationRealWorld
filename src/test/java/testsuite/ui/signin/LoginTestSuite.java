package testsuite.ui.signin;


import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import testsuite.base.BaseTest;
import testsuite.ui.steps.LoginSteps;
import testsuite.utils.ConfigLoader;

import java.util.Arrays;

public class LoginTestSuite extends BaseTest {

    @BeforeGroups(groups = {"valid_user"})
    public void beforeGroupValidUser() {
        System.out.println("before group valid_user");
//        Create an user
    }

    @AfterGroups(groups = {"valid_user"})
    public void afterGroupValidUser() {
        System.out.println("after group valid user");
//        Delete an user
    }

    @BeforeGroups(groups = {"locked_user"})
    public void beforeGroupLockedUser() {
        System.out.println("Before group locked user");
//    Create an user --> Lock the user
    }

    @AfterGroups(groups = {"locked_user"})
    public void afterGroupLockedUser() {
        System.out.println("After group lockd user");
//    Delete the user
    }

    @BeforeGroups(groups = {"from_singup_page"})
    public void beforeGroupFromSingUpPage() {
        System.out.println("Before group from singup page");
//        Create an user
//        Go to Sign up page --> Go to Signin Page
    }

    @AfterGroups(groups = {"from_singup_page"})
    public void afterGroupFromSingUpPage() {
        System.out.println("after group signup page");
//    Delete the user
    }

    @BeforeGroups(groups = {"from_detaied_article"})
    public void beforeGroupFromDetailedArticle() {
        System.out.println("Before group from detailed article");
//        Create an user
//        Go detailed article --> Signin page
    }

    @AfterGroups(groups = {"from_detaied_article"})
    public void afterGroupFromDetailedArticle() {
        System.out.println("After group from detailed article");
//        Create an user
//        Go detailed article --> Signin page
    }

    @BeforeMethod
    public void beforeMethod(ITestResult result) {
        String groupName[] = result.getMethod().getGroups();
        if (Arrays.asList(groupName).contains("from_singup_page") || Arrays.asList(groupName).contains("from_detaied_article")) {
            return;
        } else {
            System.out.println("this is group" + Arrays.asList(groupName));
//            Create an user
//            Go to sign in page
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        String groupName[] = result.getMethod().getGroups();
        if (Arrays.asList(groupName).contains("from_singup_page") || Arrays.asList(groupName).contains("from_detaied_article")) {
            System.out.println("this is group" + Arrays.asList(groupName));
        } else {
            System.out.println("After method");
//        Delete the account
        }
    }

    @Test(description = "Login success", groups = {"valid_user"})
    public void TC1_Login_Success() {
        LoginSteps loginSteps = new LoginSteps(driver);
        loginSteps.loginWith(ConfigLoader.getValue("test.email"),ConfigLoader.getValue("test.password"));
        Assert.assertTrue(true);
    }

    @Test(description = "Login Fail_Wrong Email", groups = {"valid_user"})
    public void TC2_Login_Fail_WrongEmail() {
        System.out.println("TC2");
        Assert.assertTrue(true);
    }

    @Test(description = "Login Fail_Wrong Paasword", groups = {"valid_user"})
    public void TC3_Login_Fail_WrongPassword() {
        System.out.println("TC3");
        Assert.assertTrue(true);
    }

    @Test(description = "Login_Fail_Wrong Email and Password", groups = {"valid_user"})
    public void TC4_Login_Fail_WrongEmailAndPassword() {
        System.out.println("TC4");
        Assert.assertTrue(true);
    }

    @Test(description = "Login Fail_Empty Password", groups = {"valid user"})
    public void TC5_Login_Fail_EmptyPassword() {
        System.out.println("TC5");
        Assert.assertTrue(true);
    }

    @Test(description = "Login Fail_Empty Email", groups = {"valid_user"})
    public void TC6_Login_Fail_EmptyEmail() {
        System.out.println("TC6");
        Assert.assertTrue(true);
    }

    @Test(description = "Login Fail_Empty Email and Password", groups = {"valid_user"})
    public void TC7_Login_Fail_EmptyEmailAndPassword() {
        System.out.println("TC7");
        Assert.assertTrue(true);
    }

    @Test(description = "Login Fail_No Existing User")
    public void TC8_Login_Fail_NoExistingUser() {
        System.out.println("TC8");
        Assert.assertTrue(true);
    }

    @Test(description = "Redirect to Signup page")
    public void TC11_GotoSignupPage(){
//        Click Need an account button
    }
}
