

# 📓 Log Capture and Reporting in API Automation

This document explains the **log-capture design**, including **why we use it**, **how it works**, and **how to maintain it**.

## 🎯 Why We Need This
- We want **detailed RestAssured request/response logs** for every API test, so we can debug and analyze failures.
- RestAssured logs normally print to the console (`System.out`) — but we need to:

- ✅ Attach them to TestNG HTML reports  
  ✅ Log them via log4j: https://logging.apache.org/log4j/2.x/index.html
- 
  ✅ Support parallel test execution without log contamination  
- We also want to insert **custom logs** (e.g., “Step 1: Prepare payload”) into the same log stream to create a full picture of each test's execution.

## 🔥 Key Components

### 1️⃣ ApiLogFactory
- A **centralized factory** that manages log capture using `ThreadLocal`:
  - `ThreadLocal<StringWriter>` → holds the log text buffer.
  - `ThreadLocal<PrintStream>` → RestAssured writes logs to this.
- The factory ensures **each test thread has its own writer & stream**, avoiding cross-test contamination in parallel runs.
- **API**:
  ```java
  ApiLogFactory.init(); // initializes per-thread log buffer
  ApiLogFactory.getWriter(); // returns current thread's StringWriter
  ApiLogFactory.getStream(); // returns current thread's PrintStream
  ApiLogFactory.clear(); // clears buffer after test
  ```
- `init()` is **idempotent**: it only creates a new buffer if none exists for the current thread, avoiding accidental overwriting if called multiple times.

### 2️⃣ Test Initialization (@BeforeClass / @BeforeMethod)
- We call `ApiLogFactory.init()` early (e.g., in `@BeforeClass`) **before setting RestAssured filters**, to guarantee the log buffers are ready.
- We then set `RestAssured.requestSpecification` to include:
  ```java
  RestAssured.requestSpecification = RestAssured.given()
      .filters(
          new RequestLoggingFilter(ApiLogFactory.getStream()),
          new ResponseLoggingFilter(ApiLogFactory.getStream())
      );
  ```
- **Why**: RestAssured writes request/response logs to the provided `PrintStream`.

### 3️⃣ Logging Custom Steps
- During your test steps, you can write custom entries directly into the same log buffer:
  ```java
  ApiLogFactory.getWriter().write("\n==== Step 1: Preparing data ====\n");
  ```
- This integrates custom steps with RestAssured logs in the same final output.

### 4️⃣ Attaching Logs After Each Test
- We created a **TestNG listener** (e.g., `ApiLogListener`) implementing `ITestListener`.
- In `onTestSuccess()`, `onTestFailure()`, and `onTestSkipped()`, we:
  - Call `ApiLogFactory.getStream().flush()` to ensure all logs are written.
  - Read the log text with `ApiLogFactory.getWriter().toString()`.
  - Attach logs to:
    - **TestNG reports** via `Reporter.log(...)`
    - **Log4j** via `logger.info(...)`.
  - Always call `ApiLogFactory.clear()` to avoid leaks or cross-test contamination.

### 4.1️⃣ Integrating ExtentReports for HTML Reports
- We use **ExtentReports** to generate an interactive HTML report summarizing all test results.
- In the TestNG listener (`ApiLogListener`), we:
  - Initialize ExtentReports in `onStart()` and configure an HTML reporter with a custom theme.
  - Create an `ExtentTest` node for each test method in `onTestStart()`.
  - Log captured request/response details and error stack traces on success, failure, or skip.
  - Flush the Extent report in `onFinish()`.
- The generated report is available after the test run at:
  ```
  target/extent-report.html
  ```
- Example ExtentReports setup in the listener:
  ```java
  ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("target/extent-report.html");
  htmlReporter.config().setDocumentTitle("API Test Report");
  htmlReporter.config().setReportName("API Results");
  htmlReporter.config().setTheme(Theme.STANDARD);

  extent = new ExtentReports();
  extent.attachReporter(htmlReporter);

  ExtentTest test = extent.createTest(result.getMethod().getMethodName());
  extentTest.set(test);
  ```
- By combining captured logs and ExtentReports, each test entry includes detailed steps, request/response information, and any errors for easier analysis.

### 5️⃣ Thread Safety
- Because we use `ThreadLocal`, each parallel test thread gets its own log buffer.
- This ensures logs remain **isolated**, even with TestNG’s parallel execution enabled.

## ✅ Example Test Flow

1️⃣ Listener’s `onTestStart()` runs → calls `ApiLogFactory.init()`.  
2️⃣ Test `@BeforeMethod` sets up RestAssured filters with `ApiLogFactory.getStream()`.  
3️⃣ Test executes → RestAssured writes logs into the thread’s `StringWriter`.  
4️⃣ Test inserts custom logs via `ApiLogFactory.getWriter().write(...)`.  
5️⃣ Listener’s `onTestSuccess()/onTestFailure()` reads logs → attaches to reports & logger → clears buffers.

## 💡 Tips

- Always ensure **ApiLogFactory.init() is called before filters are set**, otherwise RestAssured filters may bind to `null` streams.
- Always clear logs **after each test** to avoid memory leaks or mixing logs between tests.
- Avoid setting filters in `@BeforeClass` if you rely on listener `onTestStart()` — listener hooks come *after* `@BeforeClass`, which could cause uninitialized logs. If you need filters in `@BeforeClass`, call `ApiLogFactory.init()` directly there.

## 🛠 Maintenance Notes

- `ApiLogFactory` can be reused for other RestAssured-based tests or extended for UI logs.
- Adding more filters or log enrichment is straightforward: update `RestAssured.requestSpecification` in your setup.
- Be cautious if changing parallel settings in `testng.xml` — but ThreadLocal ensures safety.