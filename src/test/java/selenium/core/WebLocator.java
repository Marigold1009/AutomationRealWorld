package selenium.core;

import static org.testng.AssertJUnit.assertEquals;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testhelper.TestParams;

public class WebLocator {

    private final WebDriver driver;
    private final By locator;
    private final WebDriverWait wait;
    private WebLocator parent = null;

    private List<WebLocator> parents = new ArrayList<>();

    public WebLocator(By locator) {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        this.locator = locator;
    }

    /**
     * Wait until element is visible on browser using configuration timeout For setting timeout,
     * please type in command line with argument browsertimeout e.g.: -Dbrowsertimeout=30
     *
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilVisible() {
        if (_hasParent())
            wait.until(CustomWait.visibilityOfNestedElementLocatedBy(getParents(), locator));
        else wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return this;
    }

    /**
     * Wait until element is visible on browser using specific timeout
     *
     * @param duration
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilVisible(Duration duration) {
        WebDriverWait wait = new WebDriverWait(driver, duration);
        if (_hasParent())
            wait.until(CustomWait.visibilityOfNestedElementLocatedBy(getParents(), locator));
        else wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return this;
    }

    /**
     * Wait until element text contains specific value
     *
     * @param text
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilElementTextContains(String text) {
        wait.until(ExpectedConditions.textToBePresentInElement(_findElement(), text));
        return this;
    }

    /**
     * Wait until element text
     *
     * @param duration, text
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilElementTextContains(Duration duration, String text) {
        WebDriverWait wait = new WebDriverWait(driver, duration);
        wait.until(ExpectedConditions.textToBePresentInElement(_findElement(), text));
        return this;
    }

    /**
     * Wait until element has attribute contains value
     *
     * <p>Mostly wait for Ajax to load data into element.
     *
     * <p>E.g.: Css class is changing, Text field value e.t.c...
     *
     * @param attribute
     * @param value
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilElementAttributeContainsValue(String attribute, String value) {
        wait.until(
                CustomWait.waitUntilElementAttributeValueContains(driver, locator, attribute, value));
        return this;
    }

    /**
     * Wait until element has attribute contains value using specific timeout
     *
     * <p>Mostly wait for Ajax to load data into element.
     *
     * <p>E.g.: Css class is changing, Text field value e.t.c...
     *
     * @param duration
     * @param attribute
     * @param value
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilElementAttributeContainsValue(
            Duration duration, String attribute, String value) {
        WebDriverWait wait = new WebDriverWait(driver, duration);
        wait.until(
                CustomWait.waitUntilElementAttributeValueContains(driver, locator, attribute, value));
        return this;
    }

    /**
     * Wait until element has attribute value
     *
     * <p>Mostly wait for Ajax to load data into element.
     *
     * <p>E.g.: Css class is changing, Text field value e.t.c...
     *
     * @param attribute
     * @param value
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilElementAttributeValue(String attribute, String value) {
        wait.until(CustomWait.waitUntilElementAttributeValue(driver, locator, attribute, value));
        return this;
    }

    /**
     * Wait until element has attribute value using specific timeout
     *
     * <p>Mostly wait for Ajax to load data into element.
     *
     * <p>E.g.: Css class is changing, Text field value e.t.c...
     *
     * @param duration
     * @param attribute
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilElementAttributeValue(
            Duration duration, String attribute, String value) {
        WebDriverWait wait = new WebDriverWait(driver, duration);
        wait.until(CustomWait.waitUntilElementAttributeValue(driver, locator, attribute, value));
        return this;
    }

    /**
     * Wait until element is clickable on browser using configuration timeout For setting timeout,
     * please type in command line with argument browsertimeout e.g.: -Dbrowsertimeout=30
     *
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilClickable() {
        if (_hasParent()) wait.until(CustomWait.nestedElementToBeClickable(getParents(), locator));
        else wait.until(ExpectedConditions.elementToBeClickable(locator));
        return this;
    }

    /**
     * Wait until element is clickable on browser using specific timeout
     *
     * @param duration
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilClickable(Duration duration) {
        if (_hasParent()) wait.until(CustomWait.nestedElementToBeClickable(getParents(), locator));
        else wait.until(ExpectedConditions.elementToBeClickable(locator));
        return this;
    }

    /**
     * Wait until element is not visible or not present on the DOM using configuration timeout For
     * setting timeout, please type in command line with argument browsertimeout e.g.:
     * -Dbrowsertimeout=30
     *
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilInvisible() {
        wait.until(ExpectedConditions.invisibilityOf(_findElement()));
        return this;
    }

    /**
     * Wait until element is not visible or not present on the DOM using specific timeout
     *
     * @param duration
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilInvisible(Duration duration) {
        WebDriverWait wait = new WebDriverWait(driver, duration);
        wait.until(ExpectedConditions.invisibilityOf(_findElement()));
        return this;
    }

    /**
     * Wait until element is present on the DOM using configuration timeout For setting timeout,
     * please type in command line with argument browsertimeout e.g.: -Dbrowsertimeout=30
     *
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilPresent() {
        if (_hasParent()) {
            wait.until(
                    CustomWait.waitUntilNestedElementPresentInDom(
                            driver, Objects.requireNonNull(_findParent())._findElement(), locator));
        } else wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return this;
    }

    /**
     * Wait until element is present on the DOM using specific timeout
     *
     * @param duration
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilPresent(Duration duration) {
        WebDriverWait wait = new WebDriverWait(driver, duration);
        if (_hasParent()) {
            wait.until(
                    CustomWait.waitUntilNestedElementPresentInDom(
                            driver, Objects.requireNonNull(_findParent())._findElement(), locator));
        } else wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return this;
    }

    /**
     * Wait until element has text value
     *
     * @param text
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilElementText(String text) {
        wait.until(ExpectedConditions.textToBePresentInElement(_findElement(), text));
        return this;
    }

    /**
     * Wait until element has text value
     *
     * @param duration
     * @param text
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilElementText(Duration duration, String text) {
        WebDriverWait wait = new WebDriverWait(driver, duration);
        wait.until(ExpectedConditions.textToBePresentInElement(_findElement(), text));
        return this;
    }

    /**
     * Move to element using {@link Actions}
     *
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator moveToElement() {
        Actions action = new Actions(driver);
        action.moveToElement(_findElement()).perform();
        return this;
    }

    public WebLocator scrollToView() {
        JavascriptSupport.moveToElement(driver, _findElement());
        return this;
    }

    public WebLocator delay(Duration duration) throws InterruptedException {
        Thread.sleep(duration.toMillis());
        return this;
    }

    /**
     * Move to element using {@link Actions}
     *
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator moveToElement(WebElement element) {
        Actions action = new Actions(driver);
        action.moveToElement(element).perform();
        return this;
    }

    /**
     * Typing on the element, or set its value
     *
     * @param value character sequence to send to the element
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator sendKeys(CharSequence value) {
        _findElement().sendKeys(value);

        return this;
    }

    /**
     * Clear text value
     *
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator clear() {
        _findElement().clear();
        return this;
    }

    /**
     * Click on element
     *
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator click() {
        _findElement().click();
        return this;
    }

    public WebLocator clickByJs() {
        // Try to find element and click on it up to 3 times to prevent case page
        // refresh element
        for (int i = 0; i < 3; i++) {
            try {
                WebElement element = _findElement();
                JavascriptSupport.clickElement(driver, element);
                break;
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                if (i < 2) {
                    continue;
                } else {
                    throw e;
                }
            }
        }
        return this;
    }

    /**
     * Return Text value of element This method will not return Text value of Text field
     *
     * @return The innerText of this element.
     * @author thangnguyen
     */
    public String getText() {
        return _findElement().getText();
    }

    /**
     * Return Text values of multiple element
     *
     * @return List<String> elements text values
     * @author thangnguyen
     */
    public List<String> getTexts() {
        List<WebElement> elements = _findElements();
        List<String> textValues = new ArrayList<String>();
        for (WebElement elem : elements) {
            textValues.add(elem.getText());
        }
        return textValues;
    }

    /**
     * Get Attribute value of element
     *
     * @return The attribute/property's current value or null if the value is not set.
     * @author thangnguyen
     */
    public String getAttributeValue(String attributeName) {
        return _findElement().getAttribute(attributeName);
    }

    /**
     * Find element
     *
     * @return The first matching {@link WebElement} on the current page
     * @author thangnguyen
     */
    private WebElement _findElement() {
        return _hasParent()
                ? wait.until(
                ExpectedConditions.presenceOfNestedElementLocatedBy(
                        Objects.requireNonNull(_findParent()).getCachedElement(), locator))
                : wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public WebLocator findElement(WebLocator nestedLocator) {
        nestedLocator.setParent(this);
        wait.until(
                CustomWait.waitUntilNestedElementPresentInDom(driver, getParents(), nestedLocator.locator));
        return nestedLocator;
    }

    private List<WebLocator> getParents() {
        WebLocator current = this;
        List<WebLocator> parents = new ArrayList<>();

        // Traverse up the parent hierarchy
        while (current.parent != null) {
            parents.add(current.parent);
            current = current.parent;
        }

        // Reverse the list to get parents in correct order
        Collections.reverse(parents);
        this.parents = parents;
        return this.parents;
    }

    private WebLocator _findParent() {
        if (getParents().isEmpty()) {
            return null;
        } else {
            // Need to traverse up the parent hierarchy again
            WebLocator currentLocator = getParents().get(0);
            for (int i = 0; i < parents.size() - 1; i++) {
                // Find the next parent
                currentLocator = currentLocator.findElement(parents.get(i + 1));
            }
            return currentLocator;
        }
    }

    private boolean _hasParent() {
        return parent != null;
    }

    /**
     * Find list of elements
     *
     * @return A list of all {@link WebElement}, or an empty list if nothing matches
     * @author thangnguyen
     */
    private List<WebElement> _findElements() {
        return _hasParent()
                ? Objects.requireNonNull(_findParent()).getCachedElement().findElements(locator)
                : driver.findElements(locator);
    }

    /**
     * Get Css value of element
     *
     * @param propName
     * @return The current, computed value of the property.
     * @author thangnguyen
     */
    public String getCssValue(String propName) {
        return _findElement().getCssValue(propName);
    }

    /**
     * Check whether element is selected or not For check box and radio button e.t.c...
     *
     * @return True if the element is currently selected or checked, false otherwise.
     * @author thangnguyen
     */
    public boolean isSelected() {
        return _findElement().isSelected();
    }

    /**
     * Move to and highlight element for easier tracing Be careful, this method might break the
     * element
     *
     * @return {@link WebLocator}
     * @throws InterruptedException
     * @author thangnguyen
     */
    public WebLocator hightlight() throws InterruptedException {
        // Element not displayed then return
        try {
            if (!this._findElement().isDisplayed()) {
                return this;
            }
            // move to element before change style
            moveToElement();

            // Not recording or debugging then does not need highlight
            if (!TestParams.SCREEN_RECORDING && !TestParams.DEBUGGING) {
                return this;
            }
            if (TestParams.IS_HEADLESS) {
                return this;
            }

            // Change style
            String originalStyle = this.getAttributeValue("style");
            String newStyle = "background: yellow; border: 2px solid red;";
            // change to new style
            JavascriptSupport.applyStyle(driver, this._findElement(), newStyle);
            Thread.sleep(300);
            // change back
            JavascriptSupport.applyStyle(driver, this._findElement(), originalStyle);
            return this;
        } catch (NoSuchElementException e) {
            // Element not existed then return
            return this;
        } catch (StaleElementReferenceException e) {
            // Stale element
            return this;
        }
    }

    public WebLocator changeAttribute(String attribute, String value) {
        JavascriptSupport.applyStyle(driver, this._findElement(), attribute, value);
        return this;
    }

    /**
     * Clear text field by action
     *
     * @return {@link WebLocator}
     * @throws InterruptedException
     * @author thangnguyen
     */
    public WebLocator clearTextFieldAction() throws InterruptedException {
        this.clear();
        Actions action = new Actions(driver);
        action.sendKeys(Keys.SPACE).sendKeys(Keys.BACK_SPACE).perform();
        Thread.sleep(1000);
        return this;
    }

    /**
     * Set value of text field by action
     *
     * @return {@link WebLocator}
     * @throws InterruptedException
     * @author thangnguyen
     */
    public WebLocator setValuesAction(CharSequence value) {
        Actions action = new Actions(driver);
        if (System.getProperty("os.name").startsWith("Mac")) {
            action
                    .moveToElement(_findElement())
                    .click()
                    .sendKeys(Keys.chord(Keys.COMMAND, "a"), value)
                    .perform();
        } else {
            action
                    .moveToElement(_findElement())
                    .click()
                    .sendKeys(Keys.chord(Keys.CONTROL, "a"), value)
                    .perform();
        }
        return this;
    }

    /**
     * Send keys action
     *
     * @return {@link WebLocator}
     * @throws InterruptedException
     * @author thangnguyen
     */
    public WebLocator sendKeysAction(CharSequence value) {
        Actions action = new Actions(driver);
        action.moveToElement(_findElement()).click().sendKeys(value).perform();
        return this;
    }

    /**
     * Wait until number of elements present on web page
     *
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilNumberOfElements(int num) {
        wait.until(CustomWait.waitUntilNumberOfElement(driver, locator, num));
        return this;
    }

    /**
     * Wait until number of elements present on web page
     *
     * @param duration
     * @return {@link WebLocator}
     * @author thangnguyen
     */
    public WebLocator waitUntilNumberOfElements(int num, Duration duration) {
        new WebDriverWait(driver, duration)
                .until(CustomWait.waitUntilNumberOfElement(driver, locator, num));
        return this;
    }

    public WebLocator verifyCheckbox(boolean isSelected) {
        assertEquals(
                "Element: " + locator + " is not selected", this._findElement().isSelected(), isSelected);
        return this;
    }

    public WebElement findChildElement(WebElement element) {
        return element.findElement(this.locator);
    }

    public WebLocator waitUntilChildElementVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(element, this.locator));
        return this;
    }

    /**
     * Check that is element displayed
     *
     * @return boolean
     */
    public boolean isDisplayed() {
        return _findElement().isDisplayed();
    }

    public WebLocator hightlightChildElement(WebElement element) throws InterruptedException {
        // Element not displayed then return
        try {
            if (!this.findChildElement(element).isDisplayed()) {
                return this;
            }
            // move to element before change style
            moveToElement(this.findChildElement(element));

            // Not recording or debugging then does not need highlight
            if (!TestParams.SCREEN_RECORDING && !TestParams.DEBUGGING) {
                return this;
            }

            // Change style
            String originalStyle = this.findChildElement(element).getAttribute("style");
            String newStyle = "background: yellow; border: 2px solid red;";
            // change to new style
            JavascriptSupport.applyStyle(driver, this.findChildElement(element), newStyle);
            Thread.sleep(300);
            // change back
            JavascriptSupport.applyStyle(driver, this.findChildElement(element), originalStyle);
            return this;
        } catch (NoSuchElementException e) {
            // Element not existed then return
            return this;
        } catch (StaleElementReferenceException e) {
            // Stale element
            return this;
        }
    }

    public void removeElement() {
        JavascriptSupport.removeElement(driver, _findElement());
    }

    public WebLocator captureScreenshot() throws IOException {
        File file = _findElement().getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file, new File("screenshots.png"));
        return this;
    }

    public WebLocator waitUntilElementIsSelected() {
        wait.until(ExpectedConditions.elementToBeSelected(this.locator));
        return this;
    }

    public WebLocator selectOptionByVisibleText(String option) {
        Select select = new Select(driver.findElement(this.locator));
        select.selectByVisibleText(option);
        return this;
    }

    public String getSelectedOption() {
        Select select = new Select(driver.findElement(this.locator));
        return select.getFirstSelectedOption().getText();
    }

    public WebLocator getParent() {
        return parent;
    }

    public void setParent(WebLocator parent) {
        this.parent = parent;
    }

    public WebElement getCachedElement() {
        return _findElement();
    }

    public By getLocator() {
        return this.locator;
    }
}