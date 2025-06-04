package testsuite.ui.articles;

import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.Arrays;

public class CreateArticleTestSuite {
    @BeforeGroups(groups = {"existing_data"})
    public void beforeGroup_ExistingData() {
//        Create an acc
//        Create an article
//        Go to Create articles panel
    }

    @AfterGroups({"existing_data"})
    public void afterGroup_ExistingData(){
//        Delete the article
//        Delete the acc
    }

    @BeforeMethod
    public void beforeMethod(ITestResult result) {
        String groupName[] = result.getMethod().getGroups();
        if (Arrays.asList(groupName).contains("existing_data")) {
        } else {
//            Create an account
//            Go to create article page
//            Close Browser
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        String groupName[] = result.getMethod().getGroups();
        if(Arrays.asList(groupName).contains("existing_data")){

        }else {
//            Delete the article
//            Delete the acc
//            Close Browser
        }
//        Delete the acc
    }

    @Test(description = "Create article successfully full fields")
    public void TC1_CreateArticle_Success_FullFields() {
//        input value --> Publish button
    }

    @Test(description = "Create article success with empty description")
    public void TC2_CreateArticle_Success_EmptyDes() {

    }

    @Test(description = "Create article success with empty body")
    public void TC3_CreateArticle_Success_EmptyBody() {

    }

    @Test(description = "Create article success with empty tag")
    public void TC4_CreateArticle_Success_EmptyTag() {

    }
    @Test(description = "Create article success with existing description", groups = {"existing_data"})
    public void TC5_CreateArticle_Success_ExistingDes() {

    }

    @Test(description = "Create article success with existong body", groups = {"existing_data"})
    public void TC6_CreateArticle_Success_ExistingBody() {

    }

    @Test(description = "Create article success with existing tag", groups = {"existing_data"})
    public void TC7_CreateArticle_Success_ExistingTag() {

    }

    @Test(description = "Create article fail_Empty all fields")
    public void TC8_CreateArticle_Fail_EmptyAllFields() {

    }

    @Test(description = "Create article fail_Empty title")
    public void TC9_CreateArticle_Fail_EmptyTitle() {

    }

    @Test(description = "Create article fail_existing title", groups = "existing_data")
    public void TC10_CreateArticle_Fail_ExistingTitle() {

    }
}
