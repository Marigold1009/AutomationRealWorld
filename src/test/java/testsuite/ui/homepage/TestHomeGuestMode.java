package testsuite.ui.homepage;

import org.testng.annotations.Test;

public class TestHomeGuestMode {
    // Navigation test case (signup page, sign in page, author, view by tag e.t.c...)
    @Test(description = "Redirect to Signup page")
    public void TC1_GotoSignupPage() {
//        Click Sign up button
    }

    @Test(description = "Redirect to Signin Page")
    public void TC2_GotoSigninPage() {
//        Click Signin button
    }

    @Test(description = "Verify tagName list")
    public void TC3_VerifyTagList() {
//        Base on API response
    }

    //    View Article list
    @Test(description = "Verify number of article")
    public void TC4_VerifyNumberOfArticle() {
//        Base on API response
    }

    //         Detail articles info (number of likes, author e.t.c...)
    @Test(description = "Verify author of article")
    public void TC5_VerifyAuthorOfArticle() {
//        Base on API respose
    }

    @Test(description = "Verify CreatedDate of Article")
    public void TC6_VerifyCreatedDateOfArticle() {
//        Base on API response
    }

    @Test(description = "Verify favourite number of Article")
    public void TC7_VerifyLikeNumber() {
//        Base on API response
    }

    @Test(description = "Verify Title of Article")
    public void TC8_VerifyTitleOfArticle() {
//        Base on API response
    }

    @Test(description = "Verify Brief of Article")
    public void TC9_VerifyBriefArticle() {
//        Base on API response
    }

    @Test(description = "Verify Article tagName")
    public void TC10_VerifyArticleTagName() {
//        Base on API response
    }

    @Test(description = "Redirect to author page by clicking author name")
    public void T11_GotoAuthorPage() {
//        Click Author Name
    }

    @Test(description = "Can not Favorite Article by Guest Mode")
    public void TC12_FavouriteByGuestMode() {
//        Click Favourite button
    }

    //       View article
    @Test(description = "Redirect to detail article by clicking title")
    public void TC13_GotoDetailedArticleByClickTitle() {
//        Click to Article Title
    }

    @Test(description = "Redirect to detail article by click Brief content")
    public void TC14_GotoDetailedArticleByClickContent() {
//        Click Body
    }

    @Test(description = "Redirect detailed article by clicking Read More button")
    public void TC15_GotoDetailedArticlebyClickReadMoreBtn() {
//        Click Read more button
    }

    @Test(description = "Redirect to detailed article by clicking tagName")
    public void TC16_GotoDetailedArticlebyClickTagName() {
//        Click tagName
    }

    //    Verify article list by tag
    @Test(description = "Verify Article list by TagName")
    public void TC17_VerifyTagNameOfArticle() {
//        Click tagName
//        Base on API response
    }

}
