package testsuite.ui.articles;

import org.testng.annotations.*;

public class UpdateArticlesTestSuite {

    @BeforeGroups({"existing_data"})
    public void beforeGroup_ExistingData(){
//        Create an acc and create an articles
//        Go to Create Article
    }

    @AfterGroups({"existing_data"})
    public void afterGroup_ExistingData(){
//        Delete the article
//        Delete the acc
    }

    @BeforeMethod
    public void beforeMethod(){
//        Create an article
//        Go to update the articles page
    }

    @AfterMethod
    public void afterMethod(){
//        Delete the article
//        Delete the acc
    }

    @Test(description = "Update article successfully")
    public void TC1_UpdateArticle_Success_FullFields(){

    }

    @Test(description = "Update article successfully with empty description")
    public void TC2_UpdateArticle_Success_EmptyDes(){

    }

    @Test(description = "Update article successfully with empty Body")
    public void TC3_UpdateArticle_Success_EmptyBody(){

    }

    @Test(description = "Update article successfully with empty tag")
    public void TC4_UpdateArticle_Success_EmptyTag(){

    }

    @Test(description = "Update article successfully with existing description", groups = {"existing_data"})
    public void TC5_UpdateArticle_Success_ExistingDes(){

    }

    @Test(description = "Update article successfully with existing Body", groups = {"existing_data"})
    public void TC6_UpdateArticle_Success_ExistingBody(){

    }

    @Test(description = "Update article successfully with existing tag", groups = {"existing_data"})
    public void TC7_UpdateArticle_Success_ExistingTag(){

    }

    @Test(description = "Update article fail with empty title")
    public void TC8_UpdateArticle_Fail_EmptyTitle(){

    }

    @Test(description = "Update article successfully with existing title", groups = {"existinf_data"})
    public void TC9_UpdateArticle_Fail_ExistingTitle(){

    }
}
