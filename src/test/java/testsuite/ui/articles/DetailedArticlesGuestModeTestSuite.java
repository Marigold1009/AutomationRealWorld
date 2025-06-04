package testsuite.ui.articles;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import java.util.Arrays;

public class DetailedArticlesGuestModeTestSuite {

    @BeforeGroups({"login_mode"})
    public void beforeGroupLoginMode() {
//        Create an account --> Home page
    }

    @BeforeGroups({"loginMode_global"})
    public void beforeGroup_LoginMode_Global() {
//        Create an acc
//        Home page --> Global tab
    }

    @BeforeGroups({"loginMode_yourFeed"})
    public void beforeGroup_LoginMode_YourFeed() {
//        Create an acc
//        Home page --> Follow some author
//        Your Feed tab
    }

    @BeforeGroups({"loginMode_myArticles"})
    public void beforeGroup_LoginMode_MyArticles() {
//        Create an account --> Login
//        Post an article
//        Home --> My Articles
    }

    @BeforeGroups({"loginMode_myFavorite"})
    public void beforeGroup_LoginMode_MyFavorite() {
//        Create an acc
//        Submit favorite some articles
//        Home Page --> Favorites articles
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        String groupName[] = result.getMethod().getGroups();
        if (Arrays.asList(groupName).contains("guest_mode")) {
//            Close Browser
        }
//        Delete Account --> Close Browser
    }

    @Test(description = "Verify Article Title")
    public void TC1_VerifyArticleTitle() {
//        Base on API response
    }

    @Test(description = "Verify Author")
    public void TC2_VerifyAuthor() {
//        Base on API response
    }

    @Test(description = "Redirect author page")
    public void TC3_GotoAuthorPage() {
//        Click author Name
    }

    @Test(description = "Verify created date")
    public void TC4_VerifyCreatedDate() {
//        Base on API response
    }

    @Test(description = "Verify Content of Article")
    public void TC5_VerifyArticleContent() {
//        Base on API response
    }

    @Test(description = "Verify Article TagList")
    public void TC6_VerifyArticleTagList() {
//        Base on API response
    }

    @Test(description = "Verify Article comments list")
    public void TC7_VerifyArticleComments() {
//        Base on API response
    }

    @Test(description = "Verify author of comment")
    public void TC8_VerifyAuthorofComment() {
//        Base on API response
    }

    @Test(description = "Verify Created Date of comment")
    public void TC9_VerifyCommentCreatedDate() {
//        Base on API response
    }

    @Test(description = "Verify content of Comment")
    public void TC10_VerifyCommentContent() {
//        Base on API response
    }

    @Test(description = "Redirect to Login Page")
    public void TC11_GotoLoginPage() {
//        Click Sign in button
    }

    @Test(description = "Redirect to SIgn up page")
    public void TC12_GotoSignupPage() {
//        Click Sign up button
    }
}
