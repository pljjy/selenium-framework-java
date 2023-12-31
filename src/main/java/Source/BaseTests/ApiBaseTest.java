package Source.BaseTests;

import Source.ApiValidation;
import Utils.Reporter;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

public class ApiBaseTest {
    public Reporter log = new Reporter();
    public ApiValidation apiVal = new ApiValidation(log);

    @AfterMethod
    public void finishReport(ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.FAILURE -> log.testFailed(result);
            case ITestResult.SKIP -> log.testSkipped(result);
            default -> log.testSucceed(result);
        }

        Allure.addAttachment("Log-File", log.getReport(false));
    }
}
