

# 🧪 Selenium Test Automation Framework – Usage & Configuration

This documentation outlines the structure, configuration, and usage of the Selenium-based automation framework used in the RealWorld testing project.

---

## 📦 Project Structure

```
src/
├── selenium/
│   ├── core/                  # Core framework components
│   │   ├── BasePage.java
│   │   ├── Browser.java
│   │   ├── CustomWait.java
│   │   ├── DriverManager.java
│   │   ├── JavascriptSupport.java
│   │   └── WebLocator.java
│   ├── pageobject/            # Page Object classes
│   │   ├── HomePage.java
│   │   └── SignInPage.java
├── testsuite/
│   ├── api/                   # API test cases (if any)
│   ├── config/                # Test configurations
│   └── ui/
│       ├── articles/
│       ├── authorpage/
│       ├── home/
│       │   └── TestHomePage.java
│       ├── profiles/
│       ├── signin/
│       └── signup/
├── utils/
│   ├── logging/
│   │   ├── LogFactory.java
│   │   └── LoggingAspect.java
│   └── report/
│       ├── ApiReportListener.java
│       ├── UiReportListener.java
│       └── ConfigLoader.java
```

---

## ⚙️ Configuration Details

### `DriverManager.java`
- Manages `WebDriver` using `ThreadLocal`.
- Supports Chrome, Firefox, Edge.
- Reads environment config for browser setup.
- Headless mode toggle via `TestParams.IS_HEADLESS`.

### `Browser.java`
- Enum that maps to available browsers.
- Dynamically chosen via config/env.

### `BasePage.java`
- Superclass for all page objects.
- Contains shared utilities: waits, scrolls, navigation, highlights, etc.

### `CustomWait.java`
- Reusable wait methods (e.g., waitForElement, waitForWindows).

### `JavascriptSupport.java`
- JS-based utilities (scroll, click, highlight).

### `WebLocator.java`
- Wrapper around `By` selectors for better abstraction and maintainability.

---

## 🧩 Logging & Reporting

### `LogFactory.java` + `LoggingAspect.java`
- Captures method-level logs.
- Logs are thread-safe and forwarded to report files.

### `UiReportListener.java`
- Custom TestNG listener using **ExtentReports**.
- Auto-captures:
  - Screenshots (on success/failure/skip)
  - Logs from `LogFactory`

### `ApiReportListener.java`
- Handles API-related logging/reporting (if applicable).

---

## 🧪 Writing UI Tests

### Sample: `TestHomePage.java`

```java
public class TestHomePage {

    @BeforeClass
    public void setup() {
        LogFactory.init();
        driver = DriverManager.startBrowser(Browser.CHROME);
    }

    @BeforeMethod
    public void openApp() {
        DriverManager.getDriver().get(TestParams.BASE_URL);
        new CustomWait().waitForTitle("Conduit");
    }

    @Test
    public void testNavigateToSignInPage() {
        new HomePage()
            .clickSignInLink()
            .verifySignInPageOpened();
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
```

---

## 🖼️ Element Highlighting

- Controlled by `TestParams.DEBUGGING` or `SCREEN_RECORDING`.
- Highlights elements during interaction for visibility.
- Done via `highlight(WebElement)` in `BasePage`.

---

## 📷 Screenshot Handling

- Auto-attached in report on test status (pass/fail/skip).
- Captured via `TakesScreenshot` as Base64 for portability.
- Managed in `UiReportListener`.

---

## 🚀 Running the Tests

### IntelliJ:
- Right-click on test → **Run**

### CLI:
```bash
mvn clean test  -Dsurefire.suiteXmlFiles=test-config/ui_sample.xml -Dbrowser=chrome -Dheadless=true
```

---

## 📝 Notes

- WebDriver binaries handled via WebDriverManager.
- Thread-local support ensures isolation in parallel tests.
- Reports and logs are saved per test method.

---

## ✅ Summary

This Selenium framework is modular and robust for UI test automation with features like:

- Page Object Model (POM)
- Rich logging with `LogFactory`
- ExtentReports for visual reporting
- Element highlighting & screenshots
- Multi-browser, headless execution support
