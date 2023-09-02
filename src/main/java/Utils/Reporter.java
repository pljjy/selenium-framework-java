package Utils;

import org.testng.ITestResult;

import java.util.Arrays;

public class Reporter {
    StringBuilder sb = new StringBuilder();

    public Reporter() {
        info("Test started");
    }

    public String getReport(boolean clean) {
        String res = sb.toString();
        if(clean)
            sb.setLength(0); // clear sb so other tests can use it
        return res;
    }

    public void info(String text) {
        sb.append("[INFO ] ").append(text).append("\n");
    }

    public void debug(String text) {
        sb.append("[DEBUG] ").append(text).append("\n");
    }

    public void warn(String text) {
        sb.append("[WARN ] ").append(text).append("\n");
    }

    public void error(String text) {
        sb.append("[ERROR] ").append(text).append("\n");
    }

    public void testFailed(ITestResult result) {
        String stacktrace = Arrays.stream(result.getThrowable().getStackTrace())
                .map(StackTraceElement::toString)
                .reduce("", (s1, s2) -> s1 + s2 + "\n");
        // StackTraceElement[] to String
        sb.append(String.format("\nTest FAILED\n%s\n\nStack trace:\n%s", result.getThrowable().getMessage(), stacktrace));
    }

    public void testSkipped(ITestResult result) {
        sb.append(String.format("\nTest SKIPPED, reason - '%s'", result.getSkipCausedBy()));
    }

    public void testSucceed(ITestResult result) {
        sb.append("\nTest SUCCEED");
    }
}
