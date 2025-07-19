package selenium.core;

import java.time.Duration;
import java.util.ArrayList;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testhelper.TestParams;

public class BasePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private String currentWindow = null;
    private String mainWindowHandle;

    public BasePage() {
        driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void openPage(String url) {
        driver.get(url);

        //    // Clear cookies, local storage, and session storage
        //    driver.manage().deleteAllCookies();
        //    // Cast driver to JavascriptExecutor
        //    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        //    jsExecutor.executeScript("window.localStorage.clear();");
        //    jsExecutor.executeScript("window.sessionStorage.clear();");
    }

    public String getPageTitle(){
        return driver.getTitle();
    }

    public BasePage waitForPageTitle(String title){
        wait.until(ExpectedConditions.titleIs(title));
        return this;
    }

    // Get current url
    public String getUrl() {
        return driver.getCurrentUrl();
    }

    // Refresh page
    public void refreshPage() {
        driver.navigate().refresh();
    }

    // Maximize browser
    public void maximizeBrowser() {
        driver.manage().window().maximize();
    }

    // Move browser to -2000, 0 to minimize it
    public void minimizeBrowser() {
        driver.manage().window().setPosition(new Point(-2000, 0));
    }

    // Bring back browser
    public void maximizeBrowser(boolean isMinimized) {
        driver.manage().window().setPosition(new Point(0, 0));
    }

    public void switchToNewTab() {
        currentWindow = driver.getWindowHandle();
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        // Switch to nearest tab
        for (String tab : tabs) {
            if (!tab.equals(currentWindow)) {
                driver.switchTo().window(tab);
                break;
            }
        }
    }

    public void sendKeysActiveElement(CharSequence value) {
        if (Browser.getBrowser() == Browser.FIREFOX) {
            driver.findElement(By.tagName("body")).sendKeys(value);
        } else {
            driver.switchTo().activeElement().sendKeys(value);
        }
    }

    public void sendActionsActiveElement(CharSequence value) {
        Actions action = new Actions(driver);
        action.moveToElement(driver.switchTo().activeElement()).sendKeys(value).perform();
        if (Browser.getBrowser() == Browser.FIREFOX) {
            driver.findElement(By.tagName("body")).sendKeys(value);
        } else {
            driver.switchTo().activeElement().sendKeys(value);
        }
    }

    /**
     * Wait until new windows is opened
     *
     * @param timeout
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public BasePage waitUntilNewWindows(int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(CustomWait.waitUntilNewWindowIsOpened(driver));
        return this;
    }

    /**
     * Wait until new windows is opened
     *
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public BasePage waitUntilNewWindows() {
        wait.until(CustomWait.waitUntilNewWindowIsOpened(driver));
        return this;
    }

    public BasePage switchToNewWindow() {
        // Set the current one so that we can switch back
        this.mainWindowHandle = driver.getWindowHandle();

        // Switch to different window handles
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(this.mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
            }
        }

        return this;
    }

    public BasePage switchToMainWindow() {
        driver.switchTo().window(this.mainWindowHandle);
        return this;
    }

    /**
     * Wait until new windows is closed
     *
     * @param timeout
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public BasePage waitUntilNewWindowIsClosed(int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(CustomWait.waitUntilNewWindowIsCLosed(driver));
        return this;
    }

    /**
     * Wait until new windows is closed
     *
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public BasePage waitUntilNewWindowIsClosed() {
        wait.until(CustomWait.waitUntilNewWindowIsCLosed(driver));
        return this;
    }

    public void hightlight(WebElement element) throws InterruptedException {
        // Not recording or debugging then does not need highlight
        if (!TestParams.SCREEN_RECORDING && !TestParams.DEBUGGING) {
            return;
        }
        // Element not displayed then return
        try {
            if (!element.isDisplayed()) {
                return;
            }

            // Change style
            String originalStyle = element.getAttribute("style");
            String newStyle = "background: yellow; border: 2px solid red;";
            // move to element before change style
            new Actions(driver).moveToElement(element);
            // change to new style
            JavascriptSupport.applyStyle(driver, element, newStyle);
            Thread.sleep(300);
            // change back
            JavascriptSupport.applyStyle(driver, element, originalStyle);
            return;
        } catch (NoSuchElementException e) {
            // Element not existed then return
            return;
        } catch (StaleElementReferenceException e) {
            // Stale element
            return;
        }
    }

    public void delay(int timeout) throws InterruptedException {
        Thread.sleep(timeout * 1000);
    }

    public void scrollToEndPage() {
        JavascriptSupport.scrollToBottomPage(driver);
    }

    public void scrollToTopPage() {
        JavascriptSupport.scrollToTopPage(driver);
    }

    public WebDriver getDriver() {
        return driver;
    }

    //    back to previous page
    public void backToPreviousPage() {
        driver.navigate().back();
        refreshPage();
    }

    public BasePage switchToIframe(String frameId) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameId));
        //    driver.switchTo().frame(frameId);
        return this;
    }

    public BasePage switchToMainContent() {
        driver.switchTo().defaultContent();
        return this;
    }
}