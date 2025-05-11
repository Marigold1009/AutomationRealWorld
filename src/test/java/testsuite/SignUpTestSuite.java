package testsuite;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.Arrays;

public class SignUpTestSuite {

    @BeforeGroups(groups = {"existing_account"})
    public void beforGroupExistingAccount() {
        System.out.println("Before Group Existing Acc");
//        Create an account
    }

    @AfterGroups(groups = {"existing_account"})
    public void afterGroupExistingAccount() {
        System.out.println("After Group Existing Acc");
//        Delete the account
    }

    @BeforeGroups(groups = {"from_login_page"})
    public void beforeGrouFromLoginPage() {
        System.out.println("Before Group From Login Page");
//        Go to Login Page --> SignUp page
    }

    @AfterGroups(groups = {"from_login_page"})
    public void afterGroupFromLoginPage() {
        System.out.println("Afetr Group From Login Page");
//        Delete the account
    }

    @BeforeGroups(groups = {"from_detailed_article_page"})
    public void beforeGrouFromDetailedArticlePage() {
        System.out.println("Before Group Detailed Article");
//        Go to detailed article Page --> SignUp page
    }

    @AfterGroups(groups = {"from_detailed_article_page"})
    public void afterGroupFromDetailedArticlePage() {
        System.out.println("After Group Detailed Article");
//        Delete the account
    }

    @BeforeMethod
    public void beforeMethod(ITestResult result) {
        String groupName[] = result.getMethod().getGroups();
        if (Arrays.asList(groupName).contains("existing_account") || Arrays.asList(groupName).contains("from_login_page") || Arrays.asList(groupName).contains("from_detailed_article_page")) {
            System.out.println("Before group" + Arrays.asList(groupName));
        } else {
            System.out.println("No belong group, run before method");
//      Go to Sign up page
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        String groupName[] = result.getMethod().getGroups();
        if (Arrays.asList(groupName).contains("existing_account") || Arrays.asList(groupName).contains("from_login_page") || Arrays.asList(groupName).contains("from_detailed_article_page")) {
            System.out.println("After group "+Arrays.asList(groupName));
        } else {
            System.out.println("No belong group, run after method");
//      Delete the account
        }
    }

    @Test(description = "Sign up successfully")
    public void TC1_SignUp_Success() {
        System.out.println("TC1");
        Assert.assertTrue(true);
    }

    @Test(description = "Sign up fail with empty email")
    public void TC2_SignUp_Fail_EmptyEmail() {
        System.out.println("TC2");
        Assert.assertTrue(true);
    }

    @Test(description = "Sign up fail with empty PassWord")
    public void TC3_SignUp_Fail_EmptyPass() {
        System.out.println("TC3");
        Assert.assertTrue(true);
    }

    @Test(description = "Sign up fail with empty userName")
    public void TC4_SignUp_Fail_EmptyUserName() {
        System.out.println("TC4");
        Assert.assertTrue(true);
    }

    @Test(description = "Sign up fail with empty all fileds")
    public void TC5_SignUp_Fail_EmptyAllFields() {
        System.out.println("TC5");
        Assert.assertTrue(true);
    }

    @Test(description = "Sign up fail with invalid email")
    public void TC6_SignUp_Fail_InvalidEmail() {
        System.out.println("TC6");
        Assert.assertTrue(true);
    }

    @Test(description = "Sign up fail with existing email", groups = {"existing_account"})
    public void TC7_SignUp_Fail_ExistingEmail() {
        System.out.println("TC7");
        Assert.assertTrue(true);
    }

    @Test(description = "Sign up fail with existing userName", groups = {"existing_account"})
    public void TC8_SignUp_Fail_ExistingUserName() {
        System.out.println("TC8");
        Assert.assertTrue(true);
    }

    @Test(description = "Sign up Success from Login Page", groups = {"from_login_page"})
    public void TC9_SignUp_Success_FromLoginPage() {
        System.out.println("TC9");
        Assert.assertTrue(true);
    }

    @Test(description = "Sign up Success from Detailed Article", groups = {"from_detailed_article_page"})
    public void TC10_SignUp_Success_FromDetailArticle() {
        System.out.println("TC10");
        Assert.assertTrue(true);
    }
}
