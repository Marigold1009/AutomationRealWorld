package selenium.pageobject;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import selenium.core.BasePage;
import selenium.core.WebLocator;

public class AuthorPage extends BasePage {
    Page page;
    public AuthorPage(){
        super();
        page = new Page();
    }

    public AuthorPage verify_author_name(String expected_username){
        String actual_username = page.USER_NAME_TEXT.waitUntilVisible().getText();
        Assertions.assertThat(actual_username).isEqualTo(expected_username);
        return this;
    }

    public AuthorPage click_follow_button(){
        page.BUTTON_FOLLOW_USER.waitUntilVisible().click();
        try{
            page.BUTTON_UNFOLLOW_USER.waitUntilVisible();
            Assert.assertTrue(true);
        }catch(NoSuchElementException e){
            Assert.fail();
        }
        return this;
    }

    public AuthorPage click_unfollow_button(){
        page.BUTTON_UNFOLLOW_USER.waitUntilVisible().click();
        try{
            page.BUTTON_FOLLOW_USER.waitUntilVisible();
            Assert.assertTrue(true);
        }catch(NoSuchElementException e){
            Assert.fail();
        }
        return this;
    }

    class Page{
        public final WebLocator BUTTON_FOLLOW_USER = new WebLocator(By.xpath("//button[@class='btn btn-sm action-btn btn-outline-secondary']"));
        public final WebLocator BUTTON_UNFOLLOW_USER = new WebLocator(By.xpath("//button[@class='btn btn-sm action-btn btn-secondary']"));
        public final WebLocator USER_NAME_TEXT = new WebLocator(By.xpath("//div[@class='user-info']//h4"));

    }
}
