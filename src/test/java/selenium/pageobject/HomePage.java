package selenium.pageobject;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import selenium.core.BasePage;
import selenium.core.DriverManager;
import selenium.core.WebLocator;

import java.util.List;

public class HomePage extends BasePage {
    Page page;
    public HomePage() {
        super();
        page = new Page();
    }

    public HomePage click_on_button_home() {
        page.BUTTON_HOME.waitUntilClickable().click();
        return this;
    }

    public HomePage click_on_button_sign_in() {
        page.BUTTON_SIGN_IN.waitUntilClickable().click();
        return this;
    }

    public HomePage click_on_button_sign_up() {
        page.BUTTON_SIGN_UP.waitUntilClickable().click();
        return this;
    }

    public HomePage click_on_feed_tab(String tabName) {
        page.feedTab(tabName).waitUntilClickable().click();
        return this;
    }
    public HomePage click_readmore_button(String title, String expectedUrl){
        page.sectionArticle(title).findElement(page.readmorebtn()).waitUntilVisible().click();
        Assertions.assertThat(DriverManager.getDriver().getCurrentUrl()).isEqualTo(expectedUrl);
        return this;
    }
    public HomePage verify_readmore_button(String title){
        String btnName = page.sectionArticle(title).findElement(page.readmorebtn()).waitUntilVisible().getText();
        Assertions.assertThat(btnName).as("Expected text: Read more").isEqualTo("Read more...");
        return this;
    }

    public HomePage verify_article_author(String title, String author) {
        page.sectionArticle(title).findElement(page.articleAuthor()).waitUntilVisible()
                .waitUntilElementTextContains(author);
        return this;
    }

    public HomePage click_author_link(String title, String expectedurl){
        page.sectionArticle(title).findElement(page.articleAuthor())
                .waitUntilVisible().click();
        Assert.assertEquals(DriverManager.getDriver().getCurrentUrl(), expectedurl);
        return this;
    }

    public HomePage verify_createdAt(String title, String createdAt){
        String actual_createdAt = page.sectionArticle(title).findElement(page.createdDate()).waitUntilVisible().getText();
        Assertions.assertThat(actual_createdAt).isEqualTo(createdAt);
        return this;
    }

    public HomePage verify_article_favorite_count(String title, String count) {
        String value = page.sectionArticle(title).findElement(page.articleFavoriteCount()).waitUntilVisible()
                .getText();
        value = value.trim();
        Assertions.assertThat(value).as("Expected value: "+ count).isEqualTo(count);
        return this;
    }

    public HomePage verify_article_title(String title, String expected_title){
        String actual_title = page.sectionArticle(title).findElement(page.articleTitle()).waitUntilVisible().getText();
        Assertions.assertThat(actual_title).as("Expected title: "+ title).isEqualTo(expected_title);
        return this;
    }

    public HomePage verify_article_body(String title, String expected_body){
        String actual_body = page.sectionArticle(title).findElement(page.articleBody()).waitUntilVisible().getText();
        Assertions.assertThat(actual_body).as("Expected body "+expected_body).contains(expected_body);
        return this;
    }

    public HomePage verify_article_tag(String title, String tag) {
        page.sectionArticle(title).findElement(page.articleTag(tag)).waitUntilVisible();
        return this;
    }


    public HomePage verify_all_tag_list(int i, String tag){
        String tagUI = page.alltagList(i).waitUntilVisible().getText();
        Assertions.assertThat(tagUI).as("Expected tag is: "+tag).isEqualTo(tag);
        return this;
    }

    public HomePage verify_aricle_tag_list(String title, int i, String expectedTag){
        String articleTag = page.sectionArticle(title).findElement(page.tag_button_in_article_tagList(i))
                .waitUntilVisible().getText();
        Assertions.assertThat(articleTag).isEqualTo(expectedTag);
        return this;
    }

    public HomePage click_home_button(){
        page.home_button().waitUntilVisible().click();
        return this;
    }

    public HomePage click_new_post_button(){
        page.new_post_button().waitUntilVisible().click();
        return this;
    }

    public HomePage click_setting_button(){
        page.setting_button().waitUntilVisible().click();
        return this;
    }

    public HomePage click_profile_button(){
        page.profile_btn().waitUntilVisible().click();
        return this;
    }

    public HomePage click_article_tag(String title,int i, String expectedUrl){
        page.sectionArticle(title).findElement(page.tag_button_in_article_tagList(i)).waitUntilVisible().click();
        Assertions.assertThat(DriverManager.getDriver().getCurrentUrl()).isEqualTo(expectedUrl);
        return this;
    }

    public HomePage click_tag_button_in_alltagList(String tagName){
        page.tag_button_in_alltagList(tagName).waitUntilVisible().click();
        return this;
    }

    public HomePage click_article_title(String title, String expectedUrl){
        page.sectionArticle(title).findElement(page.articleTitle()).waitUntilVisible().click();
        Assertions.assertThat(DriverManager.getDriver().getCurrentUrl()).isEqualTo(expectedUrl);
        return this;
    }

    public HomePage verify_no_article_feed(){
        String msg = page.no_article_feed().getText();
        Assertions.assertThat(msg).isEqualTo("No articles are here... yet.");
        return this;
    }

    public HomePage verify_marked_favorited_button(String title){

        try{
            page.sectionArticle(title).findElement(page.markedFavorited());
            Assert.assertTrue(true);
        }catch(NoSuchElementException e){
            Assert.fail();
        }
        return this;
    }

    public HomePage verify_unmarked_favorited_button(String title){

        try{
            page.sectionArticle(title).findElement(page.unmark_favorite_button());
            Assert.assertTrue(true);
        }catch(NoSuchElementException e){
            Assert.fail();
        }
        return this;
    }

    public HomePage verify_article_tag_in_tag_tab(String tag, List<String>tagList){
        boolean checkTag = true;
        for(String tag1: tagList){
            if(tag.equals(tag1)){
                checkTag = true;
                break;
            }else {
                checkTag = false;
            }
        }
        Assert.assertTrue(checkTag);
        return this;
    }

    class Page {
        public final WebLocator BUTTON_HOME = new WebLocator(By.partialLinkText("Home"));
        public final WebLocator BUTTON_SIGN_IN = new WebLocator(By.partialLinkText("Sign in"));
        public final WebLocator BUTTON_SIGN_UP = new WebLocator(By.partialLinkText("Sign up"));

        public WebLocator feedTab(String tabName) {
            return new WebLocator(By.partialLinkText(tabName));
        }

        public WebLocator sectionArticle(String title) {
            return new WebLocator(By.xpath("//div[@class='article-preview' and .//h1[text()='" + title + "']]"));
        }

        public WebLocator createdDate (){
            return new WebLocator(By.xpath(".//div[@class='info']/span"));
        }

        public WebLocator articleTitle(){
            return new WebLocator(By.xpath(".//h1"));
        }

        public WebLocator articleBody(){
            return new WebLocator(By.xpath(".//p"));
        }

        public WebLocator readmorebtn(){
            return new WebLocator(By.xpath(".//span[text()='Read more...']"));
        }

        public WebLocator articleAuthor() {
            return new WebLocator(By.xpath(".//a[@class='author']"));
        }

        public WebLocator articleFavoriteCount() {
            return new WebLocator(By.xpath(".//button[./i[@class='ion-heart']]"));
        }

        public WebLocator markedFavorited(){
            return new WebLocator(By.xpath(".//button[@class='btn btn-sm btn-primary']"));
        }

        public WebLocator articleTag(String tag) {
            return new WebLocator(By.xpath("//li[contains(@class, 'tag') and normalize-space()='"+ tag +"']"));
        }

        public WebLocator alltagList(int i){
            return new WebLocator(By.xpath("//div[@class='tag-list']/a["+i+"]"));
        }

        public WebLocator tag_button_in_alltagList(String tagname){
            return new WebLocator(By.xpath("//div[@class='tag-list']/a[text()='"+tagname+"']"));
        }

        public WebLocator submit_favorite_btn(){
            return new WebLocator(By.xpath("//button[i[contains(@class, 'ion-heart')]]"));
        }

        public WebLocator tag_button_in_article_tagList(int i){
            return new WebLocator(By.xpath(".//ul/li["+i+"]"));
        }

        public WebLocator profile_btn(){
            return new WebLocator(By.xpath("//ul[@class='nav navbar-nav pull-xs-right']/li[4]"));
        }

        public WebLocator home_button(){
            return new WebLocator(By.xpath("//ul[@class='nav navbar-nav pull-xs-right']/li[1]"));
        }

        public WebLocator new_post_button(){
            return new WebLocator(By.xpath("//ul[@class='nav navbar-nav pull-xs-right']/li[2]"));
        }

        public WebLocator setting_button(){
            return new WebLocator(By.xpath("//ul[@class='nav navbar-nav pull-xs-right']/li[3]"));
        }

        public WebLocator no_article_feed(){
            return new WebLocator(By.xpath("//div[@class='article-preview']"));
        }

        public WebLocator unmark_favorite_button(){
            return new WebLocator(By.xpath(".//button[@class='btn btn-sm btn-outline-primary']"));
        }
    }
}
