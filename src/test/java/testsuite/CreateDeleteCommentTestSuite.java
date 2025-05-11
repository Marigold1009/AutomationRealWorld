package testsuite;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CreateDeleteCommentTestSuite {

    @BeforeGroups({"create_comment"})
    public void beforeGroup_CreateComment(){
//        Create an acc --> Login
//        Create an article
//        Go to Detailed article
    }

    @AfterGroups({"delete_comment"})
    public void afterGroup_DeleteComment(){
//        Delete the article
//        Delete the acc
//        Close Browser
    }

    @Test(description = "Create comment successfully for detailed article", groups = {"create_comment"})
    public void TC1_CreateComment_Success(){
//       Post comment
    }

    @Test(description = "Delete comment successfully for detailed article", groups = {"delete_comment"})
    public void TC2_DeleteComment_Success(){
//        Delete comment
    }
}
