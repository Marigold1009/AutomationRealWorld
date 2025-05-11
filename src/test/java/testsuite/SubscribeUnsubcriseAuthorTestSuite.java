package testsuite;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SubscribeUnsubcriseAuthorTestSuite {
    @BeforeClass
    public void beforeClass(){
//        Create an acc
//        Click author in detailed article
    }

    @AfterClass
    public void afterClass(){
//        Delete the acc
//        Close Browser
    }
    @Test(description = "Subcribe an author successfully", priority = 1)
    public void TC1_SubcribeAuthor_Success(){
//        Click Follow button

    }

    @Test(description = "Unsubcribe an author successfully", priority = 2)
    public void TC2_UnsubcribeAuthor_Success(){
//        Click unfollow button
    }
}
