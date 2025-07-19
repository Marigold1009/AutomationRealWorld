package selenium.pageobject;

import org.openqa.selenium.By;
import selenium.core.BasePage;
import selenium.core.WebLocator;

public class SignInPage extends BasePage {

    public SignInPage(){
        super();
    }

    public SignInPage wait_for_email_field(){
        Page.TEXT_FIELD_EMAIL.waitUntilVisible();
        return this;
    }

    public SignInPage enter_email(String email){
        Page.TEXT_FIELD_EMAIL.waitUntilClickable().sendKeys(email);
        return this;
    }

    public SignInPage enter_password(String password){
        Page.TEXT_FIELD_PASSWORD.waitUntilClickable().sendKeys(password);
        return this;
    }

    public SignInPage click_on_sign_in_button(){
        Page.BUTTON_SIGN_IN.waitUntilClickable().click();
        return this;
    }

    static class Page {
        public static final WebLocator TEXT_FIELD_EMAIL = new WebLocator(By.xpath("//input[@type='email']"));
        public static final WebLocator TEXT_FIELD_PASSWORD = new WebLocator(By.xpath("//input[@type='password']"));
        public static final WebLocator BUTTON_SIGN_IN = new WebLocator(By.xpath("//button[text()='Sign in']"));
    }

}
