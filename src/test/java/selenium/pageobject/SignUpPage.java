package selenium.pageobject;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.core.WebLocator;

import static constant.Constant.URL_SIGNIN;

public class SignUpPage extends BasePage {
    Page page;
    public SignUpPage() {
        super();
        page = new Page();
    }

    public SignUpPage wait_for_user_field(){
        page.TEXT_FIELD_USERNAME.waitUntilVisible();
        return this;
    }

    public SignUpPage enter_username(String username){
        page.TEXT_FIELD_USERNAME.waitUntilVisible().sendKeys(username);
        return this;
    }

    public SignUpPage enter_email(String email){
        page.TEXT_FIELD_EMAIL.waitUntilVisible().sendKeys(email);
        return this;
    }

    public SignUpPage enter_password(String password){
        page.TEXT_PASSWORD.waitUntilVisible().sendKeys(password);
        return this;
    }

    public SignUpPage click_signup_button(){
        page.SIGNUP_BUTTON.waitUntilVisible().click();
        return this;
    }

    public SignUpPage verify_error_msg(String expected_error){
        String actual_error = page.errorMsg().waitUntilVisible().getText();
        Assertions.assertThat(actual_error).contains(expected_error);
        return this;
    }

    public SignUpPage click_have_an_account_button(){
        page.HAVE_AN_ACCOUNT_BUTTON.waitUntilClickable().click();
        Assertions.assertThat(DriverManager.getDriver().getCurrentUrl()).isEqualTo(URL_SIGNIN);
        return this;
    }

    public SignUpPage verify_error_message(String expected_msg){
        WebElement element = DriverManager.getDriver().findElement(By.xpath("//input[@type='email']"));
        String actual_msg = (String) ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("return arguments[0].validationMessage;", element);
        System.out.println(actual_msg);
        Assertions.assertThat(actual_msg).isEqualTo(expected_msg);
        return this;
    }

    class Page{
        public final WebLocator TEXT_FIELD_USERNAME = new WebLocator(By.xpath("//input[@placeholder='Username']"));
        public final WebLocator TEXT_FIELD_EMAIL = new WebLocator(By.xpath("//input[@placeholder='Email']"));
        public final WebLocator TEXT_PASSWORD = new WebLocator(By.xpath("//input[@placeholder='Password']"));
        public final WebLocator SIGNUP_BUTTON = new WebLocator(By.xpath("//button[@type='submit']"));
        public final WebLocator HAVE_AN_ACCOUNT_BUTTON = new WebLocator(By.xpath("//a[normalize-space()='Have an account?']"));
        public final WebLocator errorMsg(){
            return new WebLocator(By.xpath("//ul[@class='error-messages']"));
        }
    }

}
