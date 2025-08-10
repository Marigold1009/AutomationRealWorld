package selenium.pageobject;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.core.WebLocator;

public class UpdateArticlePage extends BasePage {
    Page page;
    public UpdateArticlePage(){
        super();
        page = new Page();
    }

    public void enter_article_title(String articletitle){
        page.article_title_field().waitUntilVisible().clear();
        page.article_title_field().waitUntilVisible().sendKeys(articletitle);
    }

    public void enter_article_body(String body){
        page.article_body_field().waitUntilVisible().clear();
        page.article_body_field().waitUntilVisible().sendKeys(body);
    }

    public void enter_article_description(String description){
        page.article_description_field().waitUntilVisible().clear();
        page.article_description_field().waitUntilVisible().sendKeys(description);
    }

    public void click_publish_button(){
        page.PUBLISH_BUTTON.waitUntilVisible().click();
    }

    public void verify_after_updating_success(String expected_url) throws InterruptedException {
        Thread.sleep(3000);
        String actual_url = DriverManager.getDriver().getCurrentUrl();
        Thread.sleep(3000);
        Assertions.assertThat(actual_url).isEqualTo(expected_url);
    }

    class Page{
        public WebLocator article_title_field(){
            return new WebLocator(By.xpath("//input[@placeholder='Article Title']"));
        }

        public WebLocator article_description_field(){
            return new WebLocator(By.xpath("//input[@placeholder=\"What's this article about?\"]"));
        }

        public WebLocator article_body_field(){
            return new WebLocator(By.xpath("//textarea[@placeholder=\"Write your article (in markdown)\"]"));
        }

        public WebLocator article_tag_field(){
            return new WebLocator(By.xpath("//input[@placeholder='Enter tags']"));
        }

        public final WebLocator PUBLISH_BUTTON = new WebLocator(By.xpath("//button[normalize-space()='Publish Article']"));
    }
}
