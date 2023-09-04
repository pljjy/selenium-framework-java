package Utils;

import org.testng.ITestResult;

public class Reporter {
    StringBuilder sb = new StringBuilder();

    public Reporter() {
        info("Test started");
    }

    public String getReport(boolean clean) {
        String res = sb.toString();
        if (clean)
            sb.setLength(0);
        // clear sb so other tests can use it

        return res;
    }

    public void info(String text) {
//        sb.append("[INFO ] ").append(text).append("\n");
    }

    public void info(String text, Object... args) {
        sb.append("[INFO ] ").append(String.format(text, args)).append("\n");
    }

    public void debug(String text) {
        sb.append("[DEBUG] ").append(text).append("\n");
    }

    public void debug(String text, Object... args) {
        sb.append("[DEBUG] ").append(String.format(text, args)).append("\n");
    }

    public void warn(String text) {
        sb.append("[WARN ] ").append(text).append("\n");
    }

    public void warn(String text, Object... args) {
        sb.append("[WARN ] ").append(String.format(text, args)).append("\n");
    }

    public void error(String text) {
        sb.append("[ERROR] ").append(text).append("\n");
    }

    public void error(String text, Object... args) {
        sb.append("[ERROR] ").append(String.format(text, args)).append("\n");
    }


    public void testFailed(ITestResult result) {
        String stacktrace = Constants.stackTraceElementArrayToString(result.getThrowable().getStackTrace());
        sb.append(String.format("\nTest FAILED\n%s\n\nStack trace:\n%s", result.getThrowable().getMessage(), stacktrace));
    }

    public void testSkipped(ITestResult result) {
        sb.append(String.format("\nTest SKIPPED, reason - '%s'", result.getSkipCausedBy()));
    }

    public void testSucceed(ITestResult result) {
        sb.append("\nTest SUCCEED");
    }
}
