package selenium.pageobject;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.core.WebLocator;

import static constant.Constant.URL_SIGNUP;

public class SignInPage extends BasePage {
    Page page;

    public SignInPage(){
        super();
        page = new Page();
    }

    public SignInPage wait_for_email_field(){
        page.TEXT_FIELD_EMAIL.waitUntilVisible();
        return this;
    }

    public SignInPage enter_email(String email){
        page.TEXT_FIELD_EMAIL.waitUntilClickable().sendKeys(email);
        return this;
    }

    public SignInPage wait_for_password_field(){
        page.TEXT_FIELD_PASSWORD.waitUntilVisible();
        return this;
    }

    public SignInPage enter_password(String password){
        page.TEXT_FIELD_PASSWORD.waitUntilClickable().sendKeys(password);
        return this;
    }

    public SignInPage click_on_sign_in_button(){
        page.BUTTON_SIGN_IN.waitUntilClickable().click();
        return this;
    }

    public SignInPage verify_after_click_on_sign_in_button(){
        page.BUTTON_SIGN_IN.waitUntilClickable().click();
        return this;
    }


    public SignInPage click_need_an_account(){
        page.BUTTON_NEED_AN_ACCOUNT.waitUntilClickable().click();
        return this;
    }

    public SignInPage verify_error_message(String expected_msg){
        WebElement element = DriverManager.getDriver().findElement(By.xpath("//input[@type='email']"));
        String actual_msg = (String) ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("return arguments[0].validationMessage;", element);
        Assertions.assertThat(actual_msg).isEqualTo(expected_msg);
        return this;
    }

    class Page {
        public final WebLocator TEXT_FIELD_EMAIL = new WebLocator(By.xpath("//input[@type='email']"));
        public final WebLocator TEXT_FIELD_PASSWORD = new WebLocator(By.xpath("//input[@type='password']"));
        public final WebLocator BUTTON_SIGN_IN = new WebLocator(By.xpath("//button[text()='Sign in']"));
        public final WebLocator BUTTON_NEED_AN_ACCOUNT = new WebLocator(By.xpath("//a[normalize-space()='Need an account?']"));
        public final WebLocator errorMsg(){
            return new WebLocator(By.xpath("//ul[@class='error-messages']"));
        }
    }

}
