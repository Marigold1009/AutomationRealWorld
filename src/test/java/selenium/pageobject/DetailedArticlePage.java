package selenium.pageobject;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.core.WebLocator;

import java.util.List;

public class DetailedArticlePage extends BasePage {
    Page page;
    public DetailedArticlePage(){
        super();
        page = new Page();
    }

    public void verify_article_title(String expected_title){
        String actual_title = page.TITLE_FIELD.waitUntilVisible().getText();
        Assertions.assertThat(actual_title).isEqualTo(expected_title);
    }

    public void verify_author(String expected_author){
        String actual_author = page.author_field().waitUntilVisible().getText();
        Assertions.assertThat(actual_author).isEqualTo(expected_author);
    }

    public void click_author_link(String expected_url){
        page.author_field().waitUntilVisible().click();
        Assertions.assertThat(DriverManager.getDriver().getCurrentUrl()).isEqualTo(expected_url);
    }

    public void verify_createdDate(String expectedDate){
        String actual_date = page.date_field().waitUntilVisible().getText();
        Assertions.assertThat(actual_date).isEqualTo(expectedDate);
    }

    public void verify_article_body(String expected_body){
        String actual_body = page.article_body().getText();
        Assertions.assertThat(actual_body).isEqualTo(expected_body);
    }

    public void verify_tag(int i, List<String> tagList){
        for (int j=1; j<=i; j++){
            String actual_tag = page.article_tag(j).getText();
            Assertions.assertThat(actual_tag).isEqualTo(tagList.get(j-1));
        }
    }

    public void verify_login_msg(String expected_msg){
        String actual_msg = page.login_msg().waitUntilVisible().getText();
        Assertions.assertThat(actual_msg).isEqualTo(expected_msg);
    }

    public void verify_no_comment(){
        if(page.comment_card(1).isDisplayed()){
            Assert.assertTrue(true);
        }else{
            Assert.fail();
        }
    }

    public void verify_content_comment(String expected_content){
        String actual_content = page.content_comment().waitUntilVisible().getText();
        Assertions.assertThat(actual_content).isEqualTo(expected_content);
    }

    public void verify_author_comment(String expected_author){
        String actual_author = page.comment_author().waitUntilVisible().getText();
        Assertions.assertThat(actual_author).isEqualTo(expected_author);
    }

    public void click_author_comment(String expected_url) throws InterruptedException {
        page.comment_author().waitUntilVisible().click();
        Thread.sleep(3000);
        String actual_url = DriverManager.getDriver().getCurrentUrl();
        Assertions.assertThat(actual_url).isEqualTo(expected_url);
    }

    public void verify_date_comment(String expected_date){
        String actual_date = page.comment_date().waitUntilVisible().getText();
        Assertions.assertThat(actual_date).isEqualTo(expected_date);
    }

    public void click_delete_comment_button(){
        page.comment_delete_button().waitUntilVisible().click();
    }

    public void click_sign_in_button(String expected_url) throws InterruptedException {
        page.login_button().waitUntilVisible().click();
        String actual_url = DriverManager.getDriver().getCurrentUrl();
        Thread.sleep(3000);
        Assertions.assertThat(actual_url).isEqualTo(expected_url);
    }

    public void click_sign_up_button(String expected_url) throws InterruptedException {
        page.signup_button().waitUntilVisible().click();
        String actual_url = DriverManager.getDriver().getCurrentUrl();
        Thread.sleep(3000);
        Assertions.assertThat(actual_url).isEqualTo(expected_url);
    }

    public void click_edit_article_button(String expected_url){
        page.edit_article_button().waitUntilVisible().click();
        String actual_url = DriverManager.getDriver().getCurrentUrl();
        Assertions.assertThat(actual_url).isEqualTo(expected_url);
    }

    public void click_delete_article_button(String expected_url) throws InterruptedException {
        page.delete_article_button().waitUntilVisible().click();
        Thread.sleep(3000);
        String actual_url = DriverManager.getDriver().getCurrentUrl();
        Assertions.assertThat(actual_url).isEqualTo(expected_url);
    }

    public void input_comment(String comment){
        page.input_comment_area().waitUntilVisible().sendKeys(comment);
    }

    public void click_publish_button(){
        page.publish_comment_button().waitUntilVisible().click();
    }

    class Page{
        public final WebLocator TITLE_FIELD = new WebLocator(By.xpath("//div[@class='banner']//h1"));
        public WebLocator author_field(){
            return new WebLocator(By.xpath("//a[@class='author']"));
        }

        public WebLocator date_field(){
            return new WebLocator(By.xpath("//span[@class='date']"));
        }

        public WebLocator article_body(){
            return new WebLocator(By.xpath("//div[@class='row article-content']"));
        }

        public WebLocator article_tag(int i){
            return new WebLocator(By.xpath("//ul[@class='tag-list']["+i+"]"));
        }

        public WebLocator input_comment_area(){
            return new WebLocator(By.xpath("//div[@class='card-block']/textarea[@placeholder='Write a comment...']"));
        }

        public WebLocator publish_comment_button(){
            return new WebLocator(By.xpath("//button[normalize-space()='Post Comment']"));
        }

        public WebLocator comment_card(int i){
            return new WebLocator(By.xpath("//div[@class='card']["+i+"]"));
        }
        public WebLocator content_comment(){
            return new WebLocator(By.xpath(".//div[@class='card-block']/p"));
        }

        public WebLocator comment_author(){
            return new WebLocator(By.xpath(".//div[@class='card-footer']/a[2]"));
        }

        public WebLocator comment_date(){
            return new WebLocator(By.xpath(".//div[@class='card-footer']/span[@class='date-posted']"));
        }

        public WebLocator comment_delete_button(){
            return new WebLocator(By.xpath(".//div[@class='card-footer']/span[@class='mod-options']/i"));
        }

        public WebLocator login_msg(){
            return new WebLocator(By.xpath("//div[@class=\"col-xs-12 col-md-8 offset-md-2\"]/p"));
        }

        public WebLocator login_button(){
            return new WebLocator(By.xpath("//div[@class='container page']//a[normalize-space()='Sign in']"));
        }

        public WebLocator signup_button(){
            return new WebLocator(By.xpath("//div[@class='container page']//a[normalize-space()='sign up']"));
        }

        public WebLocator edit_article_button(){
            return new WebLocator(By.xpath("//a[@class='btn btn-outline-secondary btn-sm']"));
        }

        public WebLocator delete_article_button(){
            return new WebLocator(By.xpath("//button[@class='btn btn-outline-danger btn-sm']"));
        }
    }
}
