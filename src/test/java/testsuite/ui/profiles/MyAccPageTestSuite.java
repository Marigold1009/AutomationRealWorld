package testsuite.ui.profiles;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MyAccPageTestSuite {
    @BeforeMethod
    public void beforeMethod(){
//        Create acc successfully
//        Login --> Go to Home Page
    }

    @AfterMethod
    public void afterMethod(){
//        Delete the acc
    }

    @Test(description = "Verify UserName")
    public void TC1_VerifyUserName(){
//        Base on API response
    }

    @Test(description = "Verify bio Profile")
    public void TC2_VerifyBioProfile(){
//        Base on Bio Profile
    }

    @Test(description = "Verify url Profile")
    public void TC3_VerifyURLprofile(){
//        Base on URL Profile
    }

    @Test(description = "Redirect to Update profile Page")
    public void TC4_RedirectToUpdateProfile(){
//        Click Edit Profile Settings
    }

    @Test(description = "Verify my articles list")
    public void TC5_VerifyMyArticleList(){
//        Base on API response
    }

    @Test(description = "Verify author Name")
    public void TC6_VerifyAuthorName(){
//        Base on API response
    }

    @Test(description = "Verify createdDate")
    public void TC7_VerifyCreatedDate(){
//        Base on API response
    }

    @Test(description = "Verify FavouriteCount")
    public void TC8_VerifyFavoriteCount(){
//        Base on API response
    }

    @Test(description = "Verify title of Article")
    public void TC9_VerifyArticleTitle(){
//        Base on API response
    }

    @Test(description = "Verify brief content")
    public void TC10_VerifyBriefContent(){
//        Base on response API
    }

    @Test(description = "Verify tagName of article")
    public void TC11_VerifyArticleTag(){
//        Base on API response
    }

    @Test(description = "Favorite article")
    public void TC12_FavouriteArticle(){
//        Click favourit button
    }

    @Test(description = "Unfavourite article")
    public void TC13_UnfavouriteArticle(){
//        Click unfarvourite button
    }

    @Test(description = "Verify Favourite articles list without article")
    public void TC14_VerifyFavouriteArticlesListWithoutArticle(){
//        Base on API response
    }

    @Test(description = "Verify Favourite articles list with article")
    public void TC15_VerifyFavouritertclesListWithArticle(){
//        Base on API response
    }
}