package testsuite.utils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.io.StringWriter;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ApiLogListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(ApiLogListener.class);

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onTestFailure(ITestResult result) {
        attachLogs("FAILED");

        ExtentTest test = extentTest.get();
        if (test != null) {
            Throwable throwable = result.getThrowable();
            String logs = ApiLogFactory.getWriter() != null ? ApiLogFactory.getWriter().toString() : "No captured logs.";
            test.fail("Request/Response Log:<br>" + logs.replaceAll("\n", "<br>"));
            if (throwable != null) {
                test.fail(throwable);
            }
        } else {
            logger.error("ExtentTest is null in onTestFailure for {}", result.getMethod().getMethodName());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        attachLogs("PASSED");
        extentTest.get().pass("Test passed");
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
        } else {
            logger.warn("ExtentTest is null in onTestSkipped for {}", result.getMethod().getMethodName());
        }
    }

    private void attachLogs(String status) {
        StringWriter requestResponseLog = ApiLogFactory.getWriter();
        PrintStream requestResponseCaptureStream = ApiLogFactory.getStream();
        try {
            requestResponseCaptureStream.flush();
            String logs = requestResponseLog.toString();
            if (!logs.isEmpty()) {
                Reporter.log("<br><b>[" + status + "] Request/Response Log:</b><br>");
                Reporter.log(logs.replaceAll("\n", "<br>"));
                logger.info("\n==== [{}] Request/Response Log Start ====\n{}\n==== Request/Response Log End ====", status, logs);
                extentTest.get().info("Request/Response Log:<br>" + logs.replaceAll("\n", "<br>"));
            } else {
                Reporter.log("<br><b>[" + status + "] Request/Response Log:</b> No captured logs available.<br>");
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
        ApiLogFactory.init();
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    @Override
    public void onStart(ITestContext context) {
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("target/extent-report.html");
        htmlReporter.config().setDocumentTitle("Automation API Test Report");
        htmlReporter.config().setReportName("API Test Results");
        htmlReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}