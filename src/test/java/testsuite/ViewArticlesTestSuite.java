package testsuite;

import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.Arrays;

public class ViewArticlesTestSuite {

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
        if (Arrays.asList(groupName).contains("guest_mode")){
//            Close Browser
        }
//        Delete Account --> Close Browser
    }

    @Test(description = "Articles_Global", groups = {"guest_mode"})
    public void TC1_Articles_Global() {
//        Go to public feed tab
//        Assert all articles
    }

    @Test(description = "Articles_TagList", groups = {"guest_mode"})
    public void TC2_ArticlesBy_TagList(String tagName) {
//        Click tagName --> Assert
    }

    @Test(description = "DetailedArticle_Global_Title", groups = {"guest_mode"})
    public void TC3_DetailedArticle_Global_Title() {
//        Go public feed --> Click detailed titile article
    }

    @Test(description = "DetailedArticle_Global_ReadMore", groups = {"guest_mode"})
    public void TC4_DetailedArticle_Global_Remore() {
//        Go public feed --> Click Read more article
    }

    @Test(description = "DetailedArticle_Global_TagName", groups = {"guest_mode"})
    public void TC5_DetailedArticle_Global_TagName(String tagName) {
//        Go public feed --> Click tagName of detailed article
    }

    @Test(description = "Articles_Global_Login", groups = {"loginMode_global"})
    public void TC6_Articles_Global_Login() {
//        Go Global tab --> Assert
    }

    @Test(description = "Articles_YourFeed_Login", groups = {"loginMode_yourFeed"})
    public void TC7_Articles_YourFeed_Login() {
//        Go Your Feed --> Assert
    }

    @Test(description = "Articles_MyArticles_Login", groups = "loginMode_myArticles")
    public void TC8_Articles_MyArticles_login() {
//        Go My articles --> Assert
    }

    @Test(description = "Articles_MyFavorite_Login", groups = "loginMode_myFavorite")
    public void TC9_Articles_MyFavorite_login() {
//        Go My Favorite --> Assert
    }

    @Test(description = "Detailed Article_Login_Global_Title", groups = "loginMode_global")
    public void TC10_Detaile_Article_Login_Global_Title() {
//        Click detailed article title
    }

    @Test(description = "Detailed Article_Login_Global_ReadMore", groups = "loginMode_global")
    public void TC11_Detaile_Article_Login_Global_ReadMore() {
//        Click detailed article title
    }

    @Test(description = "Detailed Article_Login_Global_TagName", groups = "loginMode_global")
    public void TC12_Detaile_Article_Login_Global_TagName(String tagName) {
//        Click detailed article title
    }

    @Test(description = "Detailed Article_Login_YourFeed_Title", groups = "loginMode_yourFeed")
    public void TC13_Detailed_Article_Login_Global_Title() {
//        Click detailed article title
    }

    @Test(description = "Detailed Article_Login_YourFeed_ReadMore", groups = "loginMode_yourFeed")
    public void TC14_Detailed_Article_Login_Global_ReadMore() {
//        Click Readmore btn
    }

    @Test(description = "Detailed Article_Login_YourFeed_TagName", groups = "loginMode_yourFeed")
    public void TC15_Detailed_Article_Login_YourFeed_TagName(String tagName) {
//        Click detailed tagName
    }

    @Test(description = "Detailed Articles_Login_MyArticles_Title", groups = {"loginMode_myArticles"})
    public void TC16_Detailed_Article_Login_MyArticles_Title() {
//        Click detailed article title
    }

    @Test(description = "Detailed Article_Login_MyArticles_ReadMore", groups = "loginMode_myArticles")
    public void TC17_Detailed_Article_Login_MyArticles_ReadMore() {
//        Click Readmore btn
    }

    @Test(description = "Detailed Articles_Login_MyFavorite_Title", groups = {"loginMode_myArticles"})
    public void TC18_Detailed_Article_Login_MyFavorite_Title() {
//        Click detailed article title
    }

    @Test(description = "Detailed Article_Login_MyFavorite_ReadMore", groups = "loginMode_myFavorite")
    public void TC19_Detailed_Article_Login_MyFavorite_ReadMore() {
//        Click Readmore btn
    }

    @Test(description = "Detailed Article_Login_MyFavorite_TagName", groups = "loginMode_myFavorite")
    public void TC20_Detailed_Article_Login_MyFavorite_TagName(String tagName) {
//        Click detailed tagName
    }
}
