package selenium.pageobject;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import selenium.core.BasePage;
import selenium.core.WebLocator;

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

  public HomePage click_on_button_sign_in() throws InterruptedException {
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

  public HomePage verify_article_author(String title, String author) {
    page.sectionArticle(title)
        .findElement(page.articleAuthor())
        .waitUntilVisible()
        .waitUntilElementTextContains(author);
    return this;
  }

  public HomePage verify_article_favorite_count(String title, String count) {
    String value =
        page.sectionArticle(title)
            .findElement(page.articleFavoriteCount())
            .waitUntilVisible()
            .getText();
    value = value.trim();
    Assertions.assertThat(value).as("Expect favorite count is: " + count).isEqualTo(count);
    return this;
  }

  public HomePage verify_article_tag(String title, String tag) {
    page.sectionArticle(title).findElement(page.articleTag(tag)).waitUntilVisible();
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
      return new WebLocator(
          By.xpath("//div[@class='article-preview' and .//h1[text()='" + title + "']]"));
    }

    public WebLocator articleAuthor() {
      return new WebLocator(By.xpath(".//a[@class='author']"));
    }

    public WebLocator articleFavoriteCount() {
      return new WebLocator(By.xpath(".//button[./i[@class='ion-heart']]"));
    }

    public WebLocator articleTag(String tag) {
      return new WebLocator(
          By.xpath("//li[contains(@class, 'tag') and normalize-space()='" + tag + "']"));
    }
  }
}
