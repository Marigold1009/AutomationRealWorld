package testsuite;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ViewAccPageTestSuite {
    @BeforeMethod
    public void beforeMethod(){
//        Create acc successfully
//        Login --> Go to Home Page
    }

    @AfterMethod
    public void afterMethod(){
//        Delete the acc
    }

    @Test(description = "View acc page from Update acc page")
    public void View_Acc_From_Update_Acc_Page(){
//        Go to update account page
//        Assert the detailed data

    }

    @Test(description = "View acc page from detailed page")
    public void View_Acc_From_Detail_Page(){
//        Go to detailed account
//        Assert the detailed data
    }
}
