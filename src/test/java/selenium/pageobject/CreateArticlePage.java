package selenium.pageobject;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.core.WebLocator;

import java.util.List;

public class CreateArticlePage extends BasePage {
    Page page;
    public CreateArticlePage(){
        super();
        page = new Page();
    }

    public void enter_article_title(String article_title){
        page.TITLE_FIELD.waitUntilVisible().sendKeys(article_title);
    }

    public void enter_article_summary(String article_summary){
        page.TITLE_SUMMARY.waitUntilVisible().sendKeys(article_summary);
    }

    public void enter_article_body(String article_content){
        page.ARTICLE_BODY.waitUntilVisible().sendKeys(article_content);
    }

    public void enter_tag(List<String> tags){
        for(String tag: tags){
            page.TAG_FIELD.waitUntilVisible().sendKeys(tag);
            page.TAG_FIELD.waitUntilVisible().sendKeys(";");
        }

    }

    public void click_publish_button(){
        page.PUBLISH_BUTTON.waitUntilVisible().click();
    }

    public void verify_after_publish_article_success(String expected_url){
        String actual_url = DriverManager.getDriver().getCurrentUrl();
        Assertions.assertThat(actual_url).isEqualTo(expected_url);
    }

    class Page{
        public final WebLocator TITLE_FIELD = new WebLocator(By.xpath("//input[@placeholder='Article Title']"));
        public final WebLocator TITLE_SUMMARY = new WebLocator(By.xpath("//input[@placeholder=\"What's this article about?\"]"));
        public final WebLocator ARTICLE_BODY = new WebLocator(By.xpath("//textarea[@placeholder=\"Write your article (in markdown)\"]"));
        public final WebLocator TAG_FIELD = new WebLocator(By.xpath("//input[@placeholder='Enter tags']"));
        public final WebLocator PUBLISH_BUTTON = new WebLocator(By.xpath("//button"));

    }
}
