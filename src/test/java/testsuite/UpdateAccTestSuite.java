package testsuite;

import org.testng.IResultMap;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.Arrays;

public class UpdateAccTestSuite {
    @BeforeGroups({"existing_data"})
    public void beforeGroupExistingData() {
//        Create an account01
    }

    @AfterGroups({"existing_data"})
    public void afterGroupExistingData() {
//        Delete the acc
    }

    @BeforeMethod
    public void beforeMethod(ITestResult result) {
        String groupName[] = result.getMethod().getGroups();
//        Create an acc to update
        if (Arrays.asList(groupName).contains("from_acc_page")) {
//            Go to my acc page --> Update page
        } else {
//            Go to update page
        }
    }

    @AfterMethod
    public void afterMethod() {
//        Delete the account
    }

    @Test(description = "Update URL Sucess")
    public void TC1_Update_URL_Success() {

    }

    @Test(description = "Update UserName Sucess")
    public void TC2_Update_UserName_Success() {

    }

    @Test(description = "Update Bio Sucess")
    public void TC3_Update_Bio_Success() {

    }

    @Test(description = "Update Email Sucess")
    public void TC4_Update_Email_Success() {

    }

    @Test(description = "Update Password Sucess")
    public void TC5_Update_Password_Success() {

    }

    @Test(description = "Update All Fields Sucess")
    public void TC6_Update_All_Fields_Success() {

    }

    @Test(description = "Update Fail_ExistingEmail", groups = "existing_data")
    public void TC7_Update_Fail_ExistingEmail() {

    }

    @Test(description = "Update InvalidEmail")
    public void TC8_Update_Fail_InvalidEmail() {

    }

    @Test(description = "Update Fail_EmptyEmail")
    public void TC9_Update_Fail_EmptyEmail() {

    }

    @Test(description = "Update Fail_EmtyUserName")
    public void TC10_Update_Fail_EmptyUserName() {

    }

    @Test(description = "Update Fail_ExistingUserName", groups = {"existing_data"})
    public void TC11_Update_Fail_ExistingUserName() {

    }

    @Test(description = "Update_Sucess_From_Acc_Page", groups = {"from_acc_page"})
    public void TC12_Update_Success_From_Acc_Page() {

    }
}
