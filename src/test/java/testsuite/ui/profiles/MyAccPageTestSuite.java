package testsuite.ui.profiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.user.MUser;
import model.user.MUserDetail;
import org.testng.annotations.*;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.pageobject.HomePage;
import selenium.pageobject.SignInPage;
import selenium.pageobject.UpdateProfilePage;
import testsuite.config.ApiDataFactory;
import utils.logging.LogFactory;

import java.net.MalformedURLException;

import static constant.Constant.*;
import static io.restassured.RestAssured.with;

public class MyAccPageTestSuite {
    ObjectMapper mapper = new ObjectMapper();
    BasePage basePage;
    HomePage homePage;
    SignInPage signInPage;
    UpdateProfilePage updateProfilePage;
    MUser user = new MUser();
    MUserDetail userDetail = new MUserDetail();
    @BeforeClass
    public void beforeClass() throws JsonProcessingException, MalformedURLException, InterruptedException {
//       Create new acc
        RestAssured.baseURI = ApiDataFactory.API_URL;
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
        userDetail.setEmail("testui_update@gmail.com");
        userDetail.setPassword(PASSWORD);
        user.setUser(userDetail);
        JsonNode node = mapper.valueToTree(user);
        Response response = with().header("Content-Type","application/json")
                .when().body(node).request("POST","/users/login");
        user = mapper.readValue(response.getBody().prettyPrint(), MUser.class);
        userDetail = user.getUser();


//        Init log
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

    @AfterClass
    public void AfterClass(){
        DriverManager.quitDriver();
    }

    @Test(description = "Verify UserName")
    public void TC1_VerifyUserName(){
//        Base on API response
        updateProfilePage.verify_username(userDetail.getUsername());
    }

    @Test(description = "Verify bio Profile")
    public void TC2_VerifyBioProfile(){
//        Base on Bio Profile
        updateProfilePage.verify_description(userDetail.getBio());
    }

    @Test(description = "Verify url Profile")
    public void TC3_VerifyURLprofile() throws InterruptedException {
//        Base on URL Profile
        updateProfilePage.verify_image_url(userDetail.getImage());
    }

    @Test(description = "Verify email")
    public void TC4_Verify_email(){
//        Click Edit Profile Settings
        updateProfilePage.verify_email(userDetail.getEmail());
    }
}