package testsuite;

import org.testng.annotations.*;

public class SubmitUnSubmitFavoriteTestSuite {
    @BeforeClass
    public void beforeClass(){
//        Create an acc
//        Create an article
//        Go to My acc page
     }

     @BeforeGroups({"other_author"})
     public void beforeGroup_OtherAuthor(){
//        Go to Home Page
     }

     @AfterClass
     public void afterClass(){
//        Delete the account
//         Close Broswer
     }

    @Test(description = "Submit an own article successfully",priority = 1)
    public void TC1_SubmitOwnArticle_Success(){
//        Submit a article
    }

    @Test(description = "Submit an other article successfully", groups = {"other_author"}, priority = 3)
    public void TC2_SubmitArticle_Success(){
//        Submit articles
    }

    @Test(description = "Unsubmit an own article successfully", priority = 2)
    public void TC3_UnSubmitOwnArticle_Success(){
//        Unsubmit the article
    }

    @Test(description = "Unsubmit an other article successfully", groups = {"other_author"}, priority = 4)
    public void TC4_UnSubmitArticle_Success(){
//        Submit articles
    }
}
