package Source.BaseTests;

import Source.CustomDriver;
import Source.DriverAddons.DriverFactory;
import Utils.Reporter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static Utils.Constants.projectDir;

/*

This class should be inherited by every test
Override _cleanLogs and set it to FALSE if you have tests that are related to each other

 */
public class DriverBaseTest {
    boolean _cleanLogs = true;
    public CustomDriver driver;
    public Reporter log = new Reporter();

    @BeforeMethod
    public void setUpDriver() {
        String jsonFilePath = projectDir + "\\src\\main\\resources\\cfg.json";
        Map<String, Object> args;
        int timeout = 10;
        String browserName = "chrome";
        boolean headless = true;
        try (FileReader reader = new FileReader(jsonFilePath)) {
            Type type = new TypeToken<HashMap<String, Object>>() {
            }.getType();
            Gson gson = new Gson();
            args = gson.fromJson(reader, type);

            browserName = args.get("browser").toString();
            timeout = (int) ((double) args.get("timeout"));
            headless = (boolean) args.get("headless");

        } catch (Exception ignored) {
            log.warn("couldn't find \\src\\main\\resources\\cfg.json OR couldn't get values from it");
        }

        var _driver = DriverFactory.GetBrowser(DriverFactory.stringToEtypeDriver(browserName), timeout, headless);
        driver = new CustomDriver(_driver, log);
    }

    @AfterMethod
    public void Logs(ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.FAILURE -> {
                driver.takeScreenshot(result.getTestName());
                log.testFailed(result);
            }
            case ITestResult.SKIP -> log.testSkipped(result);
            default -> log.testSucceed(result);
        }

        Allure.addAttachment("Log-File", log.getReport(_cleanLogs));
    }
}
