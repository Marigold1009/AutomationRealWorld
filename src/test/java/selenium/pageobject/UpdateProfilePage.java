package selenium.pageobject;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import selenium.core.BasePage;
import selenium.core.WebLocator;

public class UpdateProfilePage extends BasePage {
    Page page;
    public UpdateProfilePage(){
        super();
        page = new Page();
    }

    public void verify_image_url(String expected_url) throws InterruptedException {
        Thread.sleep(3000);
        String actual_url = page.image_field().waitUntilVisible().getAttributeValue("value");
        Assertions.assertThat(actual_url).isEqualTo(expected_url);
    }

    public void verify_username(String expected_username){
        String actual_username = page.username_field().waitUntilVisible().getAttributeValue("value");
        Assertions.assertThat(actual_username).isEqualTo(expected_username);
    }

    public void verify_description(String expected_description){
        String actual_des = page.description_field().waitUntilVisible().getAttributeValue("value");
        Assertions.assertThat(actual_des).isEqualTo(expected_description);
    }
    public void verify_email(String expected_email){
        String actual_email = page.email_field().waitUntilVisible().getAttributeValue("value");
        Assertions.assertThat(actual_email).isEqualTo(expected_email);
    }

    public void enter_image_url(String image_url) throws InterruptedException {
        page.image_field().waitUntilVisible().clear();
        Thread.sleep(2000);
        page.image_field().sendKeys(image_url);
        Thread.sleep(2000);
    }

    public void enter_username(String username){
        page.username_field().waitUntilVisible().clear();
        page.username_field().waitUntilVisible().sendKeys(username);
    }

    public void enter_description(String des){
        page.description_field().waitUntilVisible().clear();
        page.description_field().waitUntilVisible().sendKeys(des);
    }

    public void enter_email(String email){
        page.email_field().waitUntilVisible().clear();
        page.email_field().waitUntilVisible().sendKeys(email);
    }

    public void enter_password(String password){
        page.password_field().waitUntilVisible().sendKeys(password);
    }

    public void click_update_button(){
        page.UPDATE_BUTTON.waitUntilVisible().click();
    }

    public void click_logout_button(){
        page.LOG_OUT_BUTTON.waitUntilVisible().click();
    }

    public void click_button_to_escape() throws InterruptedException {
        Thread.sleep(2000);
        if(page.escape_element().isDisplayed()){
            page.escape_element().click();
        }
    }

    class Page{
        public final WebLocator UPDATE_BUTTON = new WebLocator(By.xpath("//button[normalize-space()='Update Settings']"));
        public final WebLocator LOG_OUT_BUTTON = new WebLocator(By.xpath("//button[normalize-space()='Or click here to logout.']"));
        public WebLocator title_page(){
            return new WebLocator(By.xpath("//div[@class='container page']//h1"));
        }
        public WebLocator image_field(){
            return new WebLocator(By.xpath("//input[@placeholder='URL of profile picture']"));
        }
        public WebLocator username_field(){
            return new WebLocator(By.xpath("//input[@placeholder='Username']"));
        }
        public WebLocator description_field(){
            return new WebLocator(By.xpath("//textarea[@placeholder='Short bio about you']"));
        }
        public WebLocator email_field(){
            return new WebLocator(By.xpath("//input[@placeholder='Email']"));
        }
        public WebLocator password_field(){
            return new WebLocator(By.xpath("//input[@placeholder='New Password']"));
        }

        public WebLocator escape_element(){
            return new WebLocator(By.xpath("//span[@title='Click or press Escape to dismiss.']"));
        }
    }
}
