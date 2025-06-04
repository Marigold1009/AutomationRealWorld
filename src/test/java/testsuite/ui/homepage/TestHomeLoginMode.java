package testsuite.ui.homepage;

import org.testng.annotations.Test;

public class TestHomeLoginMode {
    // Navigation test case (create article page, setting page, author, view by tag, view your feed, view global article e.t.c...)
    @Test(description = "Redirecct to Create article page by cliking New Post")
    public void TC1_GotoCreateArticlePage(){
//        Click New Post button
    }

    @Test(description = "Redirect to Setting page by clicking Setting button")
    public void TC2_GotoSettingPage(){
//        Click Setting button
    }

    @Test(description = "Redirect to My account page")
    public void TC3_GotoMyaccPage(){
//        Click userName button
    }

    @Test(description = "View articles in Your Feed tab without no article")
    public void TC4_ViewYourFeedwithoutArticle(){
//        Verify no item
    }

    @Test(description = "View articles in Your Feed tab has article")
    public void TC5_ViewYourFeedHasArticles(){
//        Base on API response
    }

    @Test(description = "Favorite article by click submit button")
    public void TC6_FavoriteArticle(){
//        Click Favorite buttn
    }

    @Test(description = "Unfavorite article")
    public void TC7_UnfavoriteArticle(){
//        Click Unfavorite btn
    }

    @Test(description = "Verify Article list on Global tab")
    public void TC8_VeriArticleListOnGlobalTab(){
//        Base on API response
    }

}
