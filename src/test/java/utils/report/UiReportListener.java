package utils.report;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.concurrent.ThreadLocalRandom;
import net.bytebuddy.utility.RandomString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import selenium.core.DriverManager;
import utils.logging.LogFactory;

public class UiReportListener implements ITestListener {
  private static final Logger logger = LogManager.getLogger(UiReportListener.class);

  private static ExtentReports extent;
  private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
  private final RandomString random = new RandomString(8, ThreadLocalRandom.current());

  public void attachScreenshot() {
    if (DriverManager.getDriver() != null) {
      try {
        byte[] screenshot =
            ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
        String base64Screenshot = java.util.Base64.getEncoder().encodeToString(screenshot);
        extentTest.get().addScreenCaptureFromBase64String(base64Screenshot, "Screenshot");
      } catch (Exception e) {
        logger.error("Failed to capture screenshot", e);
      }
    }
  }

  @Override
  public void onTestFailure(ITestResult result) {
    attachLogs("FAILED");

    ExtentTest test = extentTest.get();
    if (test != null) {
      Throwable throwable = result.getThrowable();
      String logs =
          LogFactory.getWriter() != null ? LogFactory.getWriter().toString() : "No captured logs.";
      test.fail("Request/Response Log:<br>" + logs.replaceAll("\n", "<br>"));
      if (throwable != null) {
        test.fail(throwable);
      }
      attachScreenshot();
    } else {
      logger.error(
          "ExtentTest is null in onTestFailure for {}", result.getMethod().getMethodName());
    }
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    attachLogs("PASSED");
    extentTest.get().pass("Test passed");
    attachScreenshot();
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    attachLogs("SKIPPED");

    ExtentTest test = extentTest.get();
    if (test != null) {
      Throwable throwable = result.getThrowable();
      if (throwable != null) {
        test.skip("Test skipped with reason:<br>" + throwable.getMessage());
        test.skip(throwable);
      } else {
        test.skip("Test skipped without explicit reason.");
      }
      attachScreenshot();
    } else {
      logger.warn("ExtentTest is null in onTestSkipped for {}", result.getMethod().getMethodName());
    }
  }

  private void attachLogs(String status) {
    StringWriter requestResponseLog = LogFactory.getWriter();
    PrintStream requestResponseCaptureStream = LogFactory.getStream();
    try {
      requestResponseCaptureStream.flush();
      String logs = requestResponseLog.toString();
      if (!logs.isEmpty()) {
        Reporter.log("<br><b>[" + status + "] Request/Response Log:</b><br>");
        Reporter.log(logs.replaceAll("\n", "<br>"));
        logger.info(
            "\n==== [{}] Request/Response Log Start ====\n{}\n==== Request/Response Log End ====",
            status,
            logs);
        extentTest.get().info("Request/Response Log:<br>" + logs.replaceAll("\n", "<br>"));
      } else {
        Reporter.log(
            "<br><b>[" + status + "] Request/Response Log:</b> No captured logs available.<br>");
        logger.info("No RestAssured logs captured for this test [{}].", status);
      }
    } catch (Exception e) {
      logger.error("Failed to flush or attach logs [{}]", status, e);
    } finally {
      requestResponseLog.getBuffer().setLength(0);
    }
  }

  // You can leave other listener methods empty
  @Override
  public void onTestStart(ITestResult result) {
    LogFactory.init();
    ExtentTest test = extent.createTest(result.getMethod().getMethodName());
    extentTest.set(test);
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

  @Override
  public void onStart(ITestContext context) {
    ExtentSparkReporter htmlReporter = new ExtentSparkReporter("target/extent-report.html");
    htmlReporter.config().setDocumentTitle("Automation API Test Report");
    htmlReporter.config().setReportName("UI Test Results");
    htmlReporter.config().setTheme(Theme.STANDARD);

    extent = new ExtentReports();
    extent.attachReporter(htmlReporter);
  }

  @Override
  public void onFinish(ITestContext context) {
    extent.flush();
  }
}
