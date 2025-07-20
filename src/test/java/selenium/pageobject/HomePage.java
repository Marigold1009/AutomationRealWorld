package selenium.pageobject;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import selenium.core.BasePage;
import selenium.core.WebLocator;

public class HomePage extends BasePage {
    public HomePage() {
        super();
    }

    public HomePage click_on_button_home() {
        Page.BUTTON_HOME.waitUntilClickable().click();
        return this;
    }

    public HomePage click_on_button_sign_in() throws InterruptedException {
        Page.BUTTON_SIGN_IN.waitUntilClickable().click();
        return this;
    }

    public HomePage click_on_button_sign_up() {
        Page.BUTTON_SIGN_UP.waitUntilClickable().click();
        return this;
    }

    public HomePage click_on_feed_tab(String tabName) {
        Page.feedTab(tabName).waitUntilClickable().click();
        return this;
    }

    public HomePage verify_article_author(String title, String author) {
        Page.sectionArticle(title).findElement(Page.articleAuthor()).waitUntilVisible()
                .waitUntilElementTextContains(author);
        return this;
    }

    public HomePage verify_article_favorite_count(String title, String count) {
        String value = Page.sectionArticle(title).findElement(Page.articleFavoriteCount()).waitUntilVisible().getText();
        value = value.trim();
        Assertions.assertThat(value).as("Expect favorite count is: " + count).isEqualTo(count);
        return this;
    }

    public HomePage verify_article_tag(String title, String tag) {
        Page.sectionArticle(title).findElement(Page.articleTag(tag)).waitUntilVisible();
        return this;
    }

    static class Page {
        public final static WebLocator BUTTON_HOME = new WebLocator(By.partialLinkText("Home"));
        public final static WebLocator BUTTON_SIGN_IN = new WebLocator(By.partialLinkText("Sign in"));
        public final static WebLocator BUTTON_SIGN_UP = new WebLocator(By.partialLinkText("Sign up"));

        public static WebLocator feedTab(String tabName) {
            return new WebLocator(By.partialLinkText(tabName));
        }

        public static WebLocator sectionArticle(String title) {
            return new WebLocator(By.xpath("//div[@class='article-preview' and .//h1[text()='" + title + "']]"));
        }

        public static WebLocator articleAuthor() {
            return new WebLocator(By.xpath(".//a[@class='author']"));
        }

        public static WebLocator articleFavoriteCount() {
            return new WebLocator(By.xpath(".//button[./i[@class='ion-heart']]"));
        }

        public static WebLocator articleTag(String tag) {
            return new WebLocator(By.xpath("//li[contains(@class, 'tag') and normalize-space()='"+ tag +"']"));
        }
        
    }
}
