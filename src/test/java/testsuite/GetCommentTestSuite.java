package testsuite;

import org.testng.annotations.*;

public class GetCommentTestSuite {
    @BeforeClass
    public void beforeClass_CreateArticle(){
//        Create an acc
//        Create an article
//        Logout
    }
    @BeforeGroups({"login_mode"})
    public void beforeGroup_LoginMode(){
//        Create an acc01
//        Login
    }

    @AfterClass
    public void afterClass(){
//        Delete all accounts
    }


    @Test(description = "Get comment for new article has no comment", priority = 1)
    public void TC1_GetComment_Success_NoComment(){
//        Go to detailed article
//        Asert
    }

    @Test(description = "Get comments for detailed article has comments", priority = 4)
    public void TC2_GetComment_Success_HasComments(){

    }

    @Test(description = "Get comment for detailed article has no comment login mode", groups = "login_mode", priority = 2)
    public void TC3_GetComment_Success_LoginMode_NoComment(){
//        Detailed article to see comment
//        Assert
    }

    @Test(description = "Get comments for detailed article has comments", groups = {"login_mode"}, priority = 3)
    public void TC4_GetComment_Success_LoginMode_HasComments(){
//        Create a comment
//        View detailed article
//        Logout
    }

    @Test(description = "Get comments for detailed article has comment from new acc", groups = {"login_mode"}, priority = 5)
    public void TC5_GetComment_Success_LoginMode_NewAcc(){
//        View detailed article --> Asser comment
//        Logout
    }
}
