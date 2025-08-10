package testsuite.ui.profiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.user.MUser;
import model.user.MUserDetail;
import org.assertj.core.api.Assertions;
import org.testng.ITestResult;
import org.testng.annotations.*;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.pageobject.HomePage;
import selenium.pageobject.SignInPage;
import selenium.pageobject.UpdateProfilePage;
import testsuite.config.ApiDataFactory;
import utils.logging.LogFactory;

import java.net.MalformedURLException;
import java.util.Arrays;

import static constant.Constant.HOME_URL;
import static constant.Constant.PASSWORD;
import static io.restassured.RestAssured.with;

public class SettingPageTestSuite {

    ObjectMapper mapper = new ObjectMapper();
    BasePage basePage;
    HomePage homePage;
    SignInPage signInPage;
    UpdateProfilePage updateProfilePage;
    MUser user = new MUser();
    MUserDetail userDetail = new MUserDetail();
    String token;
    String email_update;
    String password_update;
    String email = "testui_update@gmail.com";
    String password = "123456";
//    @BeforeClass
//    public void beforeClass() throws JsonProcessingException, MalformedURLException, InterruptedException {
//       Create new acc
//        RestAssured.baseURI = ApiDataFactory.API_URL;
//        userDetail.setEmail(BASE_EMAIL + System.currentTimeMillis() + EMAIL_DOMAIN);
//        userDetail.setPassword(PASSWORD);
//        userDetail.setUsername(BASE_USER_NAME+"testupdate");
//        user.setUser(userDetail);
//        JsonNode node = mapper.valueToTree(user);
//        Response response = with().header("Content-Type","application/json")
//                .when().body(node).request("POST","/users");
//        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
//        userDetail = user.getUser();

//        Login and store data
//        RestAssured.baseURI = ApiDataFactory.API_URL;
//        userDetail.setEmail("testui_update@gmail.com");
//        userDetail.setPassword(PASSWORD);
//        user.setUser(userDetail);
//        JsonNode node = mapper.valueToTree(user);
//        Response response = with().header("Content-Type","application/json")
//                .when().body(node).request("POST","/users/login");
//        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
//        userDetail = user.getUser();
//    }

    @BeforeMethod
    public void BeforeMethod() throws InterruptedException, MalformedURLException, JsonProcessingException {
        //        Init log
        RestAssured.baseURI = ApiDataFactory.API_URL;
        userDetail.setEmail(email);
        userDetail.setPassword(password);
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = with().header("Content-Type","application/json")
                        .when().body(node).request("POST","/users/login");
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        userDetail = user.getUser();

        LogFactory.init();
        DriverManager.startBrowser();
        basePage = new BasePage();
        homePage = new HomePage();
        signInPage = new SignInPage();
        updateProfilePage = new UpdateProfilePage();
        basePage.openPage(HOME_URL);
        homePage.click_on_button_sign_in();
        signInPage.enter_email(userDetail.getEmail());
        signInPage.enter_password(PASSWORD);
        signInPage.click_on_sign_in_button();
        Thread.sleep(3000);
        homePage.click_setting_button();
    }

    @AfterMethod
    public void AfterMethod() throws JsonProcessingException {
        MUser user1 = new MUser();
        MUserDetail userDetail1 = new MUserDetail();
        userDetail1.setEmail(email_update);
        userDetail1.setPassword(password_update);
        user1.setUser(userDetail1);
        JsonNode node = mapper.valueToTree(user1);
        Response response = with().header("Content-Type","application/json")
                .when().body(node).request("POST","/users/login");
        user1 = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        userDetail1 = user1.getUser();
        token = userDetail1.getToken();

        userDetail.setPassword(password);
        JsonNode node01 = mapper.valueToTree(userDetail);
        Response response01 = with().headers("Content-Type","application/json","Authorization","Token "+token)
                .when().body(node01).request("PUT","/user");
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        DriverManager.quitDriver();
    }

    @Test(description = "Update URL Sucess")
    public void TC1_Update_URL_Success() throws InterruptedException {
        String update_url = "https://www.google.com/";
        updateProfilePage.enter_image_url(update_url);
        Thread.sleep(2000);
        updateProfilePage.click_update_button();
        Thread.sleep(2000);
        homePage.click_setting_button();
        updateProfilePage.verify_username(userDetail.getUsername());
        updateProfilePage.verify_email(userDetail.getEmail());
        updateProfilePage.verify_image_url(update_url);
        updateProfilePage.verify_description(userDetail.getBio());
        email_update = userDetail.getEmail();
        password_update = PASSWORD;
    }

    @Test(description = "Update UserName Success")
    public void TC2_Update_UserName_Success() throws InterruptedException, MalformedURLException {
        String username_update = userDetail.getUsername()+System.currentTimeMillis();
        updateProfilePage.enter_username(username_update);
        updateProfilePage.click_update_button();
        updateProfilePage.refreshPage();
//        updateProfilePage.click_button_to_escape();
        Thread.sleep(2000);
        homePage.click_on_button_sign_in();
        signInPage.enter_email(userDetail.getEmail());
        signInPage.enter_password(password);
        signInPage.click_on_sign_in_button();
        homePage.click_setting_button();
        Thread.sleep(3000);
        updateProfilePage.verify_username(username_update);
        updateProfilePage.verify_email(userDetail.getEmail());
        updateProfilePage.verify_image_url(userDetail.getImage());
        updateProfilePage.verify_description(userDetail.getBio());
        email_update = userDetail.getEmail();
        password_update = PASSWORD;
    }

    @Test(description = "Update Bio Sucess", enabled = false)
    public void TC3_Update_Bio_Success() {

    }

    @Test(description = "Update Email Sucess",enabled = false)
    public void TC4_Update_Email_Success() {

    }

    @Test(description = "Update Password Sucess",enabled = false)
    public void TC5_Update_Password_Success() {

    }

    @Test(description = "Update All Fields Sucess",enabled = false)
    public void TC6_Update_All_Fields_Success() {

    }

    @Test(description = "Update Fail_ExistingEmail", enabled = false)
    public void TC7_Update_Fail_ExistingEmail() {

    }

    @Test(description = "Update InvalidEmail",enabled = false)
    public void TC8_Update_Fail_InvalidEmail() {

    }

    @Test(description = "Update Fail_EmptyEmail",enabled = false)
    public void TC9_Update_Fail_EmptyEmail() {

    }

    @Test(description = "Update Fail_EmtyUserName",enabled = false)
    public void TC10_Update_Fail_EmptyUserName() {

    }

    @Test(description = "Update Fail_ExistingUserName", enabled = false)
    public void TC11_Update_Fail_ExistingUserName() {

    }

    @Test(description = "Click Logout button ",enabled = false)
    public void TC12_ClickLogoutButton(){
//        Click Logout button
    }
}
